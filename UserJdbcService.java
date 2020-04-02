package org.levelup.job.list.jdbc.homework3;

import org.levelup.job.list.jdbc.JdbcUtils;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

public class UserJdbcService implements UserService {
    @Override
    public User createUser(String name, String lastName, String passport) throws SQLException {
        try (Connection connection = JdbcUtils.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("insert into users (name, last_name,passport) values (?, ?, ?)");
            statement.setString(1, name);
            statement.setString(2, lastName);
            statement.setString(3, passport);
            statement.executeUpdate();
            return findByPassport(passport);
        }
    }

    @Override
    public User findByPassport(String passport) throws SQLException {
        try (Connection connection = JdbcUtils.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("select * from users where passport = ?");
            statement.setString(1, passport);
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            return new User(resultSet.getInt("id"), resultSet.getString("name"),
                            resultSet.getString("last_name"), resultSet.getString("passport"));
        }
    }

    @Override
    public Collection<User> findByNameAndLastName(String name, String lastName) throws SQLException {
        try (Connection connection = JdbcUtils.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("select * from users where name = (?) and last_name = (?)");
            statement.setString(1, name);
            statement.setString(2, lastName);
            ResultSet resultSet = statement.executeQuery();
            return extractUsers(resultSet);
        }
    }

    @Override
    public Collection<User> findByLastName(String lastName) throws SQLException {
        try (Connection connection = JdbcUtils.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("select * from users where last_name = ?");
            statement.setString(1,lastName);
            ResultSet resultSet = statement.executeQuery();
            return extractUsers(resultSet);
        }
    }

    @Override
    public void deleteUserByPassport(String passport) throws SQLException {
        try (Connection connection = JdbcUtils.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("delete from users where passport = ?");
            statement.setString(1, passport);
            statement.executeUpdate();
        }

    }

    @Override
    public User updateUser(String passport, String newName, String newLastName) throws SQLException {
        try (Connection connection = JdbcUtils.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("update users set name = ?, last_name = ? where passport = ?");
            statement.setString(1, newName);
            statement.setString(2, newLastName);
            statement.setString(3, passport);
            statement.executeUpdate();
            return findByPassport(passport);
        }
    }

    private Collection<User> extractUsers(ResultSet resultSet) throws SQLException {
        Collection<User> users = new ArrayList<>();
        while (resultSet.next()) {
            int id = resultSet.getInt("id");
            String name = resultSet.getString("name");
            String lastName = resultSet.getString("last_name");
            String passport = resultSet.getString("passport");
            users.add(new User(id, name, lastName, passport));
        } return users;
    }
}
