package com.salesianos.utils;

import org.junit.jupiter.api.Test;
import java.sql.Connection;
import static org.junit.jupiter.api.Assertions.*;

class DatabaseConnectionTest {

    @Test
    void testDatabaseConnection() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            assertNotNull(conn, "La conexión no debería ser null");
            System.out.println("Conexión exitosa en test JUnit");
        } catch (Exception e) {
            e.printStackTrace();
            fail("Error al conectar a la base de datos: " + e.getMessage());
        }
    }
}

