package com.example.api;

import com.example.User;
import com.example.DatabaseConnection;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<User> users = getUsersFromDatabase();

        // Set the response content type to JSON
        response.setContentType("application/json");

        try (PrintWriter out = response.getWriter()) {
            // Convert the list of users to JSON format
            String usersJson = convertUsersToJson(users);
            out.println(usersJson);
        }
    }

    private List<User> getUsersFromDatabase() {
        List<User> users = new ArrayList<>();

        try (Connection connection = DatabaseConnection.getConnection()) {
            String query = "SELECT * FROM users";
            try (PreparedStatement statement = connection.prepareStatement(query);
                 ResultSet resultSet = statement.executeQuery()) {

                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String username = resultSet.getString("username");
                    String email = resultSet.getString("email");

                    User user = new User(id, username, email);
                    users.add(user);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return users;
    }

    private String convertUsersToJson(List<User> users) {
        // Create a StringBuilder to construct the JSON response
        StringBuilder jsonBuilder = new StringBuilder("[");
        
        for (User user : users) {
            jsonBuilder.append("{");
            jsonBuilder.append("\"id\": ").append(user.getId()).append(",");
            jsonBuilder.append("\"username\": \"").append(user.getUsername()).append("\",");
            jsonBuilder.append("\"email\": \"").append(user.getEmail()).append("\"");
            jsonBuilder.append("},");
        }
        
        // Remove the trailing comma and close the JSON array
        if (!users.isEmpty()) {
            jsonBuilder.deleteCharAt(jsonBuilder.length() - 1);
        }
        jsonBuilder.append("]");
        
        return jsonBuilder.toString();
    }
}
