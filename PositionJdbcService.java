package org.levelup.job.list.jdbc.homework3;

import org.levelup.job.list.domain.Position;
import org.levelup.job.list.jdbc.JdbcUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;

public class PositionJdbcService implements PositionService{

    @Override
    public Position createPosition(String name) throws SQLException {
        try (Connection connection = JdbcUtils.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("insert into positions (name) values (?)");
            statement.setString(1, name);
            statement.executeUpdate();
            return findPositionByName(name);
        }
    }

    @Override
    public void deletePositionById(int id) throws SQLException {
        try (Connection connection = JdbcUtils.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("delete from positions where id = ?");
            statement.setInt(1, id);
            statement.executeUpdate();
        }
    }

    @Override
    public void deletePositionByName(String name) throws SQLException {
        try (Connection connection = JdbcUtils.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("delete from positions where name = ?");
            statement.setString(1, name);
            statement.executeUpdate();
        }
    }

    @Override
    public Collection<Position> findAllPositionWhichNameLike(String name) throws SQLException {
        try (Connection connection = JdbcUtils.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("select * from positions where name like ?");
            statement.setString(1, name);
            ResultSet resultSet = statement.executeQuery();
            return extractPositions(resultSet);
        }
    }

    private Collection<Position> extractPositions(ResultSet resultSet) throws SQLException {
        Collection<Position> positions = new ArrayList<>();
        while (resultSet.next()) {
            int id = resultSet.getInt("id");
            String name = resultSet.getString("name");
            positions.add(new Position(id, name));
        } return positions;
    }

    @Override
    public Position findPositionById(int id) throws SQLException {
        try (Connection connection = JdbcUtils.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("select * from positions WHERE id = ?");
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            String positionName = resultSet.getString("name");
            return new Position(id, positionName);
        }
    }

    @Override
    public Collection<Position> findAllPositions() throws SQLException {
        try (Connection connection = JdbcUtils.getConnection()) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select * from positions");
            return extractPositions(resultSet);
        }
    }

    @Override
    public Position findPositionByName(String name) throws SQLException {
        try (Connection connection = JdbcUtils.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("select * from positions where name = ?");
            statement.setString(1, name);
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            int idPosition = resultSet.getInt(1);
            return new Position(idPosition, name);
        }
    }
}
