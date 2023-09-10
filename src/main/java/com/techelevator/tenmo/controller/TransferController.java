package com.techelevator.tenmo.controller;


import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.exception.DaoException;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.TransactionManagementConfigurationSelector;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.security.Principal;
import java.util.*;

/**
 * Controller to handle transfers.
 */
@RestController
@RequestMapping(path = "/api/transfers")
@PreAuthorize("isAuthenticated()")
public class TransferController {
    private TransferDao transferDao;
    private UserDao userDao;
    private AccountDao accountDao;

    public TransferController(TransferDao transferDao, UserDao userDao, AccountDao accountDao) {
        this.transferDao = transferDao;
        this.userDao = userDao;
        this.accountDao = accountDao;
    }


    @RequestMapping(path = "/available-accounts", method = RequestMethod.GET)
    public List<Map<String, String>> listAllRecipients(Principal principal) {
        String username = principal.getName();
        int userId = userDao.findIdByUsername(username);
        List<Account> listOfAccounts = accountDao.getAllAccountsExceptCurrentUser(userId);

        List<Map<String, String>> returnList = new ArrayList<>();
        for (Account account : listOfAccounts) {
            Map<String, String> userMap = new HashMap<>();
            userMap.put("username", accountDao.getUsernameByAccountId(account.getAccountId()));
            returnList.add(userMap);
        }
        return returnList;
    }


    @RequestMapping(path = "/get-all", method = RequestMethod.GET)
    public List<Transfer> list(){
        return transferDao.getAllTransfers();
    }

    @RequestMapping(path = "/{transferId}", method = RequestMethod.GET)
    public Map<String, String> getTransferByTransferId(@PathVariable int transferId){
        Map<String, String> returnList = new LinkedHashMap<>();

        Transfer transfer = transferDao.getTransferById(transferId);
        returnList.put("transferId", String.valueOf(transfer.getTransferId()));
        returnList.put("transferAmount", transfer.getAmount().toString());
        returnList.put("from", accountDao.getUsernameByAccountId(transfer.getTransferFromAccount()));
        returnList.put("to", accountDao.getUsernameByAccountId(transfer.getTransferToAccount()));
        return returnList;
    }

    @RequestMapping(path = "/my-transfer-list", method = RequestMethod.GET)
    public List<Map<String, String>> getCurrentUserTransfers(Principal user){
        String username = user.getName();
        int userId = userDao.findIdByUsername(username);
        List<Transfer> listOfTransfers = transferDao.getTransfersByUserId(userId);

        List<Map<String, String>> returnList = new ArrayList<>();
        for (Transfer transfer: listOfTransfers) {
            Map<String, String> userMap = new LinkedHashMap<>();
            userMap.put("transferId", String.valueOf(transfer.getTransferId()));
            userMap.put("transferAmount", transfer.getAmount().toString());
            userMap.put("from", accountDao.getUsernameByAccountId(transfer.getTransferFromAccount()));
            userMap.put("to", accountDao.getUsernameByAccountId(transfer.getTransferToAccount()));
            returnList.add(userMap);
        }
        return returnList;
    }

    @RequestMapping(path = "/my-transfers/{transferId}", method = RequestMethod.GET)
    public Transfer getCurrentUserTransferById(@PathVariable int transferId, Principal user){
        String username = user.getName();
        int userId = userDao.findIdByUsername(username);
        return transferDao.getCurrentUserTransferById(transferId, userId);
    }


    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(path = "", method = RequestMethod.POST)
    public boolean create(@Valid @RequestBody Transfer transfer){
        // initialize transfer parties and transfer amount
        Account transferFrom = accountDao.getAccountsByAccountId(transfer.getTransferFromAccount());
        Account transferTo = accountDao.getAccountsByAccountId(transfer.getTransferToAccount());
        BigDecimal amount = transfer.getAmount();
        // a sending transfer has an initial status of approved
        if(transfer.getTransferType().equals("send")){
            transfer.setTransferStatus("approved");
        }
        // handle edge cases of accounts and insufficient fund
        if(transferFrom==null || transferTo==null){
            throw new DaoException("Unable to complete the transfer due to account errors.");
        } else if (amount.compareTo(transferFrom.getBalance())>0){
            throw new DaoException("Insufficient funds, transfer amount is greater than the account balance!");
        }
        // if the transfer status is approved and also transfer was able to be created
        // transfer money then is made between two accounts
        boolean transferCompleted = transferDao.createTransfer(transfer);
        if(transfer.getTransferStatus().equals("approved")){
            accountDao.withdrawFromAccountBalance(amount, transfer.getTransferFromAccount());
            accountDao.depositToAccountBalance(amount, transfer.getTransferToAccount());
        }
        return transferCompleted;
    }

    @RequestMapping(path = "/{transferId}", method = RequestMethod.PUT)
    public boolean update(@Valid @RequestBody Transfer transferToUpdate, @PathVariable int transferId){
        transferToUpdate.setTransferId(transferId);
        boolean updateCompleted = false;
        // initialize transfer parties and transfer amount
        Account transferFrom = accountDao.getAccountsByAccountId(transferToUpdate.getTransferFromAccount());
        Account transferTo = accountDao.getAccountsByAccountId(transferToUpdate.getTransferToAccount());
        BigDecimal amount = transferToUpdate.getAmount();
        // a sending transfer has an initial status of approved
        if(transferToUpdate.getTransferType().equals("send")){
            transferToUpdate.setTransferStatus("approved");
        }
        // handle edge cases of accounts and insufficient fund
        if(transferFrom==null || transferTo==null){
            throw new DaoException("Unable to update the transfer due to account errors.");
        } else if (amount.compareTo(transferFrom.getBalance())>0){
            throw new DaoException("Insufficient funds, transfer amount is greater than the account balance!");
        }
        //if the status was already approved before updating, just return the updated transfer decision
        if(transferDao.getTransferById(transferId).getTransferStatus().equals("approved")){
            return transferDao.updateTransfer(transferToUpdate, transferId);
        }
        // handle the case updating transfer status from pending/rejected to approved
        // transfer money then is made between two accounts
        updateCompleted = transferDao.updateTransfer(transferToUpdate, transferId);
        if(transferToUpdate.getTransferStatus().equals("approved")){
            accountDao.withdrawFromAccountBalance(amount, transferToUpdate.getTransferFromAccount());
            accountDao.depositToAccountBalance(amount, transferToUpdate.getTransferToAccount());
        }
        return updateCompleted;
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @RequestMapping(path = "/{transferId}", method = RequestMethod.DELETE)
    public boolean delete(@PathVariable int transferId){
        return transferDao.deleteTransfer(transferId);
    }

}
