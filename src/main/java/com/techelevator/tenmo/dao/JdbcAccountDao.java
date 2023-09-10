package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.exception.DaoException;
import com.techelevator.tenmo.model.Account;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcAccountDao implements AccountDao {

    private final JdbcTemplate jdbcTemplate;

    public JdbcAccountDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Account> getAllAccountsExceptCurrentUser(int userId) {
        List<Account> results = new ArrayList<>();
        String sql = "Select account_id, user_id, balance FROM account\n" +
                "WHERE user_id <> ?";
        try{
            SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, userId);
            while (rowSet.next()){
                results.add(mapRowToAccount(rowSet));
            }
        } catch (CannotGetJdbcConnectionException e){
            throw new DaoException( "cannot connect to server or database", e);
        }
        return results;
    }

    @Override
    public List<Account> getAllAccounts() {
        List<Account> results = new ArrayList<>();
        String sql = " Select account_id, user_id, balance FROM account";
        try{
            SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql);
            while (rowSet.next()){
                results.add(mapRowToAccount(rowSet));
            }
        } catch (CannotGetJdbcConnectionException e){
          throw new DaoException( "cannot connect to server or database", e);
        }
        return results;
    }

    @Override
    public Account getAccountsByAccountId(int accountId) {
        Account account = null;
        String sql = " Select account_id, user_id, balance FROM account WHERE account_id = ? ";
        try {
            SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, accountId);
            if (rowSet.next()){
                account = mapRowToAccount(rowSet);
            }

        } catch (CannotGetJdbcConnectionException e){
            throw new DaoException( "cannot connect to server or database", e);
        }
        return account;
    }

    @Override
    public Account getAccountsByUserId(int userId) {
        Account account = null;
        String sql = " Select account_id, user_id, balance FROM account WHERE user_id = ? ";
        try {
            SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, userId);
            if (rowSet.next()){
                account = mapRowToAccount(rowSet);
            }

        } catch (CannotGetJdbcConnectionException e){
            throw new DaoException( "cannot connect to server or database", e);
        }
        return account;
    }

    @Override
    public BigDecimal getBalanceByUserId(int userId) {
        BigDecimal balance = null;
        String sql = "SELECT balance FROM account WHERE user_id = ?";
        try {
            SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, userId);
            if (rowSet.next()){
                balance = rowSet.getBigDecimal("balance");
            }

        } catch (CannotGetJdbcConnectionException e){
            throw new DaoException( "cannot connect to server or database", e);
        }
        return balance;
    }

    @Override
    public BigDecimal getBalanceByAccountId(int accountId) {
        BigDecimal balance = null;
        String sql = "SELECT balance FROM account WHERE account_id = ?";
        try {
            SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, accountId);
            if (rowSet.next()){
                balance = rowSet.getBigDecimal("balance");
            }

        } catch (CannotGetJdbcConnectionException e){
            throw new DaoException( "cannot connect to server or database", e);
        }
        return balance;
    }

    @Override
    public String getUsernameByAccountId(int accountId) {
        String username = "";
       String sql = "SELECT username FROM account JOIN tenmo_user ON tenmo_user.user_id = account.user_id WHERE account_id = ?";
        try {
            SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, accountId);
            if (rowSet.next()){
                username = rowSet.getString("username");
            }

        } catch (CannotGetJdbcConnectionException e){
            throw new DaoException( "cannot connect to server or database", e);
        }
        return username;
    }


    @Override
    public boolean depositToAccountBalance(BigDecimal amount, int accountId) {
        boolean success = false;
        String sql = "UPDATE account SET balance = balance + ? WHERE account_id = ?";
        try {
            int numberOfRows = jdbcTemplate.update(sql, amount, accountId);

            if (numberOfRows == 0 ){
                throw new DaoException("Zero rows affected, expected at least one");
            }
            success = true;
        } catch (CannotGetJdbcConnectionException e){
            throw new DaoException( "cannot connect to server or database", e);
        } catch (DataIntegrityViolationException e){
            throw new DaoException("data integrity violation", e);
        }
        return success;


    }

    @Override
    public boolean withdrawFromAccountBalance(BigDecimal amount, int accountId) {
        boolean success = false;
        String sql = "UPDATE account SET balance = balance - ? WHERE account_id = ?";
        try {
            int numberOfRows = jdbcTemplate.update(sql, amount, accountId);

            if (numberOfRows == 0 ){
                throw new DaoException("Zero rows affected, expected at least one");
            }
            success = true;
        } catch (CannotGetJdbcConnectionException e){
            throw new DaoException( "cannot connect to server or database", e);
        } catch (DataIntegrityViolationException e){
            throw new DaoException("data integrity violation", e);
        }
        return success;
    }

    @Override
    public boolean createAccount(Account accountToCreate) {
        boolean success = false;
        String sql = "INSERT INTO account (user_id, balance) VALUES (?, ?) RETURNING account_id ";
        try {
            int accountId = jdbcTemplate.queryForObject(sql, int.class, accountToCreate.getUserId(), accountToCreate.getBalance());
            accountToCreate.setAccountId(accountId);
            success = true;
        } catch (CannotGetJdbcConnectionException e){
            throw new DaoException( "cannot connect to server or database", e);
        } catch (DataIntegrityViolationException e){
            throw new DaoException("data integrity violation", e);
        }
        return success;
    }

    @Override
    public boolean updateAccount(int accountId, Account accountToUpdate) {
        boolean success = false;
        String sql = "UPDATE account SET user_id = ?, balance = ? WHERE account_id = ?";
        try {
            int numberOfRows = jdbcTemplate.update(sql, accountToUpdate.getUserId(), accountToUpdate.getBalance(), accountId);

           if (numberOfRows == 0 ){
               throw new DaoException("Zero rows affected, expected at least one");
           }
            success = true;
        } catch (CannotGetJdbcConnectionException e){
            throw new DaoException( "cannot connect to server or database", e);
        } catch (DataIntegrityViolationException e){
            throw new DaoException("data integrity violation", e);
        }
        return success;

    }

    @Override
    public boolean deleteAccount(int accountId) {
        boolean success = false;
        String deleteTransferByAccountId = "DELETE FROM transfer WHERE account_transfer_from = ? OR account_transfer_to = ? ";
        String deleteAccountByAccountId = "DELETE FROM account WHERE account_id = ?";
        try {
            jdbcTemplate.update(deleteTransferByAccountId, accountId, accountId);
            int numberOfRows = jdbcTemplate.update(deleteAccountByAccountId, accountId);

            if (numberOfRows != 0) {
                success = true;
            }

        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Cannot connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
        return success;
    }

    private Account mapRowToAccount (SqlRowSet rowSet){
        Account account = new Account();
        account.setAccountId(rowSet.getInt("account_id"));
        account.setUserId(rowSet.getInt("user_id"));
        account.setBalance(rowSet.getBigDecimal("balance"));
        return account;
    }
}
