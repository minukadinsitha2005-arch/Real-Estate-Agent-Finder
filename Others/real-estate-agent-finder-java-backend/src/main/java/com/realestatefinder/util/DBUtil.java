package com.realestatefinder.util;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public final class DBUtil {
    private static final Properties PROPERTIES = new Properties();

    static {
        try (InputStream inputStream = DBUtil.class.getClassLoader().getResourceAsStream("db.properties")) {
            if (inputStream == null) {
                throw new IllegalStateException("db.properties was not found in the classpath.");
            }
            PROPERTIES.load(inputStream);
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (IOException | ClassNotFoundException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    private DBUtil() {
    }

    public static Connection getConnection() throws SQLException {
        String url = readValue("DB_URL", "db.url");
        String user = readValue("DB_USER", "db.user");
        String password = readValue("DB_PASSWORD", "db.password");
        return DriverManager.getConnection(url, user, password);
    }

    private static String readValue(String envKey, String propertyKey) {
        String envValue = System.getenv(envKey);
        if (envValue != null && !envValue.isBlank()) {
            return envValue;
        }
        return PROPERTIES.getProperty(propertyKey, "").trim();
    }
}
