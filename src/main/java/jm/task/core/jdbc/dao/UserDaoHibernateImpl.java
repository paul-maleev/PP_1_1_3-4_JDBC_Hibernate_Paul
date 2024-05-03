package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserDaoHibernateImpl implements UserDao {

    private final static UserDao INSTANCE = new UserDaoHibernateImpl();

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
    private static final Logger LOGGER = Logger.getLogger("DAO LOGGER");
    private final Connection connection = Util.open();


    public UserDaoHibernateImpl() {

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

    }

    @Override
    public void removeUserById(long id) {

    }

    @Override
    public List<User> getAllUsers() {
        return null;
    }

    @Override
    public void cleanUsersTable() {

    }
}
