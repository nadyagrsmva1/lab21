package com.lab21;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SQLiteAuthService implements AuthService {
    private final static String connectionUrl = "jdbc:sqlite:identifier.sqlite";
    private static Connection connection;
    private static Statement stmt;
    @Override
    public void start() {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection(connectionUrl);
            stmt = connection.createStatement();
            System.out.println("Сервис аутентификации запущен");
            FillDummyUsersIfEmpty();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            System.out.println("Сервис аутентификации не запущен");
        }
    }
    @Override
    public void stop() {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("Сервис аутентификации остановлен");
    }
    public SQLiteAuthService() {
    }
    @Override
    public String getNickByLoginPass(String login, String pass) {
        try {
            String query = String.format("SELECT * FROM users WHERE login='%s' AND pass = '%s'", login, pass);
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                return rs.getString("nick");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void FillDummyUsersIfEmpty() {
        try {
            stmt.execute("CREATE TABLE IF NOT EXISTS users (login TEXT NOT NULL, pass TEXT NOT NULL, nick TEXT NOT NULL)");
            if (!stmt.executeQuery("SELECT * FROM users").next()) {
                for (int i = 1; i <= 3; i++)
                    stmt.addBatch(String.format("INSERT INTO users (login, pass, nick) VALUES (\"login%d\", \"pass%d\", \"nick%d\")", i, i, i));
                stmt.executeBatch();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
