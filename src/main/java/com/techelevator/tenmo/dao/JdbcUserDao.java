package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.exception.DaoException;
import com.techelevator.tenmo.model.User;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcUserDao implements UserDao {
    private static final BigDecimal INITIAL_BALANCE = new BigDecimal("1000.00");

    private JdbcTemplate jdbcTemplate;

    public JdbcUserDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public int findIdByUsername(String username) {
        String sql = "SELECT user_id FROM tenmo_user WHERE username ILIKE ?;";
        Integer id = jdbcTemplate.queryForObject(sql, Integer.class, username);
        if (id != null) {
            return id;
        } else {
            return -1;
        }
    }

    @Override
    public List<User> findAll() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT user_id, username, password_hash FROM tenmo_user;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql);
        while(results.next()) {
            User user = mapRowToUser(results);
            users.add(user);
        }
        return users;
    }

    @Override
    public User findUserById(int userId) {
        User user = null;
        String sql = "SELECT user_id, username, password_hash FROM tenmo_user WHERE user_id = ?";
        try{
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userId);
            if(results.next()){
                user = mapRowToUser(results);
            }
        } catch (CannotGetJdbcConnectionException e){
            throw new DaoException("Unable to connect to server or database", e);
        }
        return user;
    }

    @Override
    public User findUserByUsername(String username) {
        String sql = "SELECT user_id, username, password_hash FROM tenmo_user WHERE username ILIKE ?;";
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, username);
        if (rowSet.next()){
            return mapRowToUser(rowSet);
        }
        throw new UsernameNotFoundException("User " + username + " was not found.");
    }

    @Override
    public boolean create(String username, String password) {

        // create user
        String sql = "INSERT INTO tenmo_user (username, password_hash) VALUES (?, ?) RETURNING user_id";
        String password_hash = new BCryptPasswordEncoder().encode(password);
        Integer newUserId;
        try {
            newUserId = jdbcTemplate.queryForObject(sql, Integer.class, username, password_hash);
        } catch (DataAccessException e) {
            return false;
        }

        // create account
        sql = "INSERT INTO account (user_id, balance) VALUES (?, ?)";
        try{
            jdbcTemplate.update(sql, newUserId, INITIAL_BALANCE);
        } catch (DataAccessException e){
            return false;
        }
        return true;
    }

    @Override
    public boolean update(int userId, User userToUpdate) {
        boolean success = false;
        String sql = "UPDATE tenmo_user SET username = ?, password_hash = ? WHERE user_id = ?";
        try{
            int numberOfRows = jdbcTemplate.update(sql, userToUpdate.getUsername(), userToUpdate.getPassword(), userId);
            if(numberOfRows==0){
                throw new DaoException("Zero rows affected, expected at least one");
            }
            success = true;
        } catch (CannotGetJdbcConnectionException e){
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e){
            throw new DaoException("Data integrity violation", e);
        }
        return success;
    }

    @Override
    public boolean delete(int userId) {
        boolean success = false;
        String deleteFromTransferTable = "DELETE FROM transfer WHERE account_transfer_from IN (SELECT account_id FROM account WHERE user_id = ?) \n" +
                "OR account_transfer_to IN (SELECT account_id FROM account WHERE user_id = ?);";
        String deleteFromAccountTable = "DELETE FROM account WHERE user_id = ?;";
        String sql = "DELETE FROM tenmo_user WHERE user_id = ?";
        try{
            jdbcTemplate.update(deleteFromTransferTable, userId, userId);
            jdbcTemplate.update(deleteFromAccountTable, userId);
            int numberOfRows = jdbcTemplate.update(sql, userId);
            if(numberOfRows==0){
                throw new DaoException("Zero rows affected, expected at least one");
            }
            success = true;
        } catch (CannotGetJdbcConnectionException e){
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e){
            throw new DaoException("Data integrity violation", e);
        }
        return success;
    }

    private User mapRowToUser(SqlRowSet rs) {
        User user = new User();
        user.setId(rs.getInt("user_id"));
        user.setUsername(rs.getString("username"));
        user.setPassword(rs.getString("password_hash"));
        user.setActivated(true);
        user.setAuthorities("USER");
        return user;
    }
}
