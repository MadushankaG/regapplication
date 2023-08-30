package com.example;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/userList")
public class MainServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<User> users = getUsersFromDatabase();

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        out.println("<html>");
        out.println("<head>");
        out.println("<title>User List</title>");
        out.println("<link rel='stylesheet' type='text/css' href='style.css'>");
        out.println("</head>");
        out.println("<body>");
        out.println("<h1>User List</h1>");
        out.println("<ul>");

        for (User user : users) {
            out.println("<li>" + user.getUsername() + " - " + user.getEmail() + "</li>");
        }

        out.println("</ul>");
        out.println("</body>");
        out.println("</html>");
    }

    public static List<User> getUsersFromDatabase() {
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
}
