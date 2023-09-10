package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.exception.DaoException;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcTransferDao implements TransferDao {
    private final JdbcTemplate jdbcTemplate;

    public JdbcTransferDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Transfer> getAllTransfers() {
        List<Transfer> transfers = new ArrayList<>();
        String sql = "SELECT transfer_id, account_transfer_from, account_transfer_to, transfer_type, transfer_status, amount FROM transfer";
        try{
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql);
            while(results.next()){
                transfers.add(mapRowToTransfer(results));
            }
        } catch (CannotGetJdbcConnectionException e){
            throw new DaoException("Unable to connect to server or database", e);
        }
        return transfers;
    }

    @Override
    public Transfer getTransferById(int transferId) {
        Transfer transfer = null;
        String sql = "SELECT transfer_id, account_transfer_from, account_transfer_to, transfer_type, transfer_status, amount FROM transfer WHERE transfer_id = ?";
        try{
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, transferId);
            if(results.next()){
                transfer = mapRowToTransfer(results);
            }
        } catch (CannotGetJdbcConnectionException e){
            throw new DaoException("Unable to connect to server or database", e);
        }
        return transfer;
    }


    @Override
    public Transfer getCurrentUserTransferById(int transferId, int userId) {
        Transfer transfer = null;
        String sql = "SELECT transfer_id, account_transfer_from, account_transfer_to, transfer_type, transfer_status, amount " +
                "FROM transfer JOIN account ON account_transfer_from = account_id OR account_transfer_to = account_id WHERE user_id = ? AND transfer_id = ?";
        try{
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userId, transferId);
            if(results.next()){
                transfer = mapRowToTransfer(results);
            }
        } catch (CannotGetJdbcConnectionException e){
            throw new DaoException("Unable to connect to server or database", e);
        }
        return transfer;
    }

    @Override
    public List<Transfer> getTransfersByUserId(int userId) {
        List<Transfer> transfers = new ArrayList<>();
        String sqlForSending = "SELECT transfer_id, account_transfer_from, account_transfer_to, transfer_type, transfer_status, amount" +
                " FROM transfer JOIN account ON transfer.account_transfer_from = account_id WHERE user_id = ?";
        String sqlForReceiving = "SELECT transfer_id, account_transfer_from, account_transfer_to, transfer_type, transfer_status, amount" +
                " FROM transfer JOIN account ON transfer.account_transfer_to = account_id WHERE user_id = ?";
        try{
            SqlRowSet result1 = jdbcTemplate.queryForRowSet(sqlForSending, userId);
            while(result1.next()){
                transfers.add(mapRowToTransfer(result1));
            }
            SqlRowSet result2 = jdbcTemplate.queryForRowSet(sqlForReceiving, userId);
            while(result2.next()){
                transfers.add(mapRowToTransfer(result2));
            }
        } catch (CannotGetJdbcConnectionException e){
            throw new DaoException("Unable to connect to server or database", e);
        }
        return transfers;
    }

    @Override
    public List<Transfer> getTransfersByAccountId(int accountId) {
        List<Transfer> transfers = new ArrayList<>();
        String sql = "SELECT transfer_id, account_transfer_from, account_transfer_to, transfer_type, transfer_status, amount \n" +
                "FROM transfer WHERE account_transfer_from = ? OR account_transfer_to = ?\n";
        try{
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, accountId, accountId);
            while(results.next()){
                transfers.add(mapRowToTransfer(results));
            }
        } catch (CannotGetJdbcConnectionException e){
            throw new DaoException("Unable to connect to server or database", e);
        }
        return transfers;
    }

    @Override
    public Boolean createTransfer(Transfer transferToCreate) {
        boolean success = false;
        String sql = "INSERT INTO transfer (account_transfer_from, account_transfer_to, transfer_type, transfer_status, amount)\n" +
                "VALUES (?, ?, ?, ?, ?) RETURNING transfer_id";
        try{
            int transfer_id = jdbcTemplate.queryForObject(sql, int.class, transferToCreate.getTransferFromAccount(), transferToCreate.getTransferToAccount(), transferToCreate.getTransferType(), transferToCreate.getTransferStatus(), transferToCreate.getAmount());
            transferToCreate.setTransferId(transfer_id);
            success = true;
        } catch (CannotGetJdbcConnectionException e){
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e){
            throw new DaoException("Data integrity violation", e);
        }
        return success;
    }

    @Override
    public Boolean updateTransfer(Transfer transferToUpdate, int transferId) {
        boolean success = false;
        String sql = "UPDATE transfer SET account_transfer_from = ?, account_transfer_to = ?, transfer_type = ?, transfer_status = ?, amount = ?\n" +
                "WHERE transfer_id = ?";
        try{
            int numberOfRows = jdbcTemplate.update(sql, transferToUpdate.getTransferFromAccount(), transferToUpdate.getTransferToAccount(), transferToUpdate.getTransferType(), transferToUpdate.getTransferStatus(), transferToUpdate.getAmount(), transferId);
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
    public Boolean deleteTransfer(int TransferId) {
        boolean success = false;
        String sql = "DELETE FROM transfer WHERE transfer_id = ?";
        try{
            int numberOfRows = jdbcTemplate.update(sql, TransferId);
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

    private Transfer mapRowToTransfer(SqlRowSet rowSet){
        Transfer transfer = new Transfer();
        transfer.setTransferId(rowSet.getInt("transfer_id"));
        transfer.setTransferFromAccount(rowSet.getInt("account_transfer_from"));
        transfer.setTransferToAccount(rowSet.getInt("account_transfer_to"));
        transfer.setTransferType(rowSet.getString("transfer_type"));
        transfer.setTransferStatus(rowSet.getString("transfer_status"));
        transfer.setAmount(rowSet.getBigDecimal("amount"));
        return transfer;
    }



}
