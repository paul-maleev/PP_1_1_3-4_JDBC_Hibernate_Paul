package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserDaoJDBCImpl implements UserDao {

    private final static UserDao INSTANCE = new UserDaoJDBCImpl();
    private static final String CREATE_USERS_TABLE_SQL = """
            CREATE TABLE IF NOT EXISTS user (
            id BIGINT AUTO_INCREMENT PRIMARY KEY, 
            name VARCHAR(255) NOT NULL ,
            lastName VARCHAR(255) NOT NULL ,
            age TINYINT CHECK (age>0));
            """;
    private static final String DROP_TABLE_USERS_SQL = """
            DROP TABLE IF EXISTS user
            """;
    private static final String INSERT_USER_SQL = """
            INSERT INTO user(name, lastname, age)
            VALUES (?, ?, ?)
            """;
    private static final String REMOVE_USER_BY_ID_SQL = """
            DELETE FROM user
            WHERE id = ?
            """;
    private static final String SELECT_ALL_USERS_FROM_USER_SQL = """
            SELECT id, name, lastname, age
            FROM user
            """;
    private static final String CLEAN_TABLE_USER_SQL = """
            DELETE FROM user
            """;
    private static final Logger LOGGER = Logger.getLogger("DAO LOGGER");
    private final Connection connection = Util.open();


    public UserDaoJDBCImpl() {
    }

    public static UserDao getInstance() {
        return INSTANCE;
    }

    @Override
    public void createUsersTable() {
        try (PreparedStatement preparedStatement = connection.prepareStatement(CREATE_USERS_TABLE_SQL)) {
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, "Table already exists!");
        }

    }

    @Override
    public void dropUsersTable() {
        try (PreparedStatement preparedStatement = connection.prepareStatement(DROP_TABLE_USERS_SQL)) {
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, "Table missing!");
        }

    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(INSERT_USER_SQL)) {
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, lastName);
            preparedStatement.setInt(3, age);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void removeUserById(long id) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(REMOVE_USER_BY_ID_SQL)) {
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public List<User> getAllUsers() {
        List<User> list = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_USERS_FROM_USER_SQL)) {

            ResultSet result = preparedStatement.executeQuery();
            int counter = 0;
            while (result.next()) {
                list.add(new User(
                        result.getString("name"),
                        result.getString("lastname"),
                        result.getByte("age")));
                list.get(counter).setId(result.getLong("id"));
                counter++;
            }
            return list;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void cleanUsersTable() {
        try (PreparedStatement preparedStatement = connection.prepareStatement(CLEAN_TABLE_USER_SQL)) {
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
