package com.salesianos.controllers;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.salesianos.utils.DatabaseConnection;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/views")
public class LoginServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        // Establecer conexión con la base de datos
        try (Connection conn = DatabaseConnection.getConnection()) {
            // Preparar la consulta para verificar el correo y la contraseña
            String query = "SELECT d.email_departamento, d.password_departamento, u.rol_usuario " +
                    "FROM Departamento d " +
                    "JOIN Usuario u ON d.id_departamento = u.id_departamento " +
                    "WHERE d.email_departamento = ?";

            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, email);

                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        String storedPassword = rs.getString("password_departamento");
                        String userRole = rs.getString("rol_usuario");

                        // Verificar la contraseña (suponiendo que está almacenada en hash)
                        if (storedPassword.equals(password)) {
                            // La contraseña es correcta, establecer la sesión
                            HttpSession session = request.getSession();
                            session.setAttribute("email", email);   // Guardar el email en la sesión
                            session.setAttribute("role", userRole); // Guardar el rol en la sesión

                            // Redirigir al usuario a la página de inicio con el rol como parámetro
                            System.out.println("Redirecting to: " + request.getContextPath() + "/views/inicio.html?rol_usuario=" + userRole);
                            response.sendRedirect(request.getContextPath() + "/views/inicio.html?rol_usuario=" + userRole);
                        } else {
                            // Contraseña incorrecta
                            request.setAttribute("error", "Correo o contraseña incorrectos.");
                            request.getRequestDispatcher("index.html").forward(request, response);
                        }
                    } else {
                        // El correo no existe en la base de datos
                        request.setAttribute("error", "Correo o contraseña incorrectos.");
                        request.getRequestDispatcher("index.html").forward(request, response);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("error", "Error en la conexión a la base de datos.");
            request.getRequestDispatcher("index.html").forward(request, response);
        }
    }
}



