/*
 * Copyright The WildFly Authors
 * SPDX-License-Identifier: Apache-2.0
 */
package org.jboss.as.quickstarts.servlet_security;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.sql.DataSource;

import jakarta.annotation.Resource;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.Initialized;
import jakarta.enterprise.event.Observes;

/**
 * Initializes the database schema and data for Elytron JDBC realm authentication.
 * This CDI bean runs at application startup to create tables and populate
 * test user credentials if they don't already exist.
 *
 * @author Mohammed Abourass mouhammedmax@hotmail.com
 */
@ApplicationScoped
public class DatabaseInitializer {

    private static final Logger LOGGER = Logger.getLogger(DatabaseInitializer.class.getName());

    @Resource(lookup = "java:jboss/datasources/ServletSecurityDS")
    private DataSource dataSource;

    public void initializeDatabase(@Observes @Initialized(ApplicationScoped.class) Object init) {
        LOGGER.info("Initializing database schema for servlet-security...");

        try {
            createTables();
            insertTestData();
            LOGGER.info("Database initialization completed successfully.");
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Failed to initialize database", e);
            throw new RuntimeException("Database initialization failed", e);
        }
    }

    private void createTables() throws SQLException {
        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement()) {

            // Create USERS table if not exists
            stmt.executeUpdate(
                "CREATE TABLE IF NOT EXISTS USERS (" +
                "ID INT, " +
                "USERNAME VARCHAR(20), " +
                "PASSWORD VARCHAR(20))"
            );

            // Create ROLES table if not exists
            stmt.executeUpdate(
                "CREATE TABLE IF NOT EXISTS ROLES (" +
                "ID INT, " +
                "NAME VARCHAR(20))"
            );

            // Create USERS_ROLES junction table if not exists
            stmt.executeUpdate(
                "CREATE TABLE IF NOT EXISTS USERS_ROLES (" +
                "USER_ID INT, " +
                "ROLE_ID INT)"
            );

            LOGGER.info("Database tables created or verified");
        }
    }

    private void insertTestData() throws SQLException {
        try (Connection conn = dataSource.getConnection()) {

            // Check if data already exists (avoid duplicates on redeployment)
            if (userExists(conn, "quickstartUser")) {
                LOGGER.info("Test data already exists, skipping insertion");
                return;
            }

            // Insert users
            try (PreparedStatement insertStmt = conn.prepareStatement(
                "INSERT INTO USERS (ID, USERNAME, PASSWORD) VALUES (?, ?, ?)")) {

                insertStmt.setInt(1, 1);
                insertStmt.setString(2, "quickstartUser");
                insertStmt.setString(3, "quickstartPwd1!");
                insertStmt.executeUpdate();

                insertStmt.setInt(1, 2);
                insertStmt.setString(2, "guest");
                insertStmt.setString(3, "guestPwd1!");
                insertStmt.executeUpdate();
            }

            // Insert roles
            try (PreparedStatement insertStmt = conn.prepareStatement(
                "INSERT INTO ROLES (ID, NAME) VALUES (?, ?)")) {

                insertStmt.setInt(1, 1);
                insertStmt.setString(2, "quickstarts");
                insertStmt.executeUpdate();

                insertStmt.setInt(1, 2);
                insertStmt.setString(2, "guest");
                insertStmt.executeUpdate();
            }

            // Insert user-role mappings
            try (PreparedStatement insertStmt = conn.prepareStatement(
                "INSERT INTO USERS_ROLES (USER_ID, ROLE_ID) VALUES (?, ?)")) {

                insertStmt.setInt(1, 1);
                insertStmt.setInt(2, 1);
                insertStmt.executeUpdate();

                insertStmt.setInt(1, 2);
                insertStmt.setInt(2, 2);
                insertStmt.executeUpdate();
            }

            LOGGER.info("Test data inserted successfully");
        }
    }

    private boolean userExists(Connection conn, String username) throws SQLException {
        try (PreparedStatement stmt = conn.prepareStatement("SELECT COUNT(*) FROM USERS WHERE USERNAME = ?")) {
            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
                return false;
            }
        }
    }
}
