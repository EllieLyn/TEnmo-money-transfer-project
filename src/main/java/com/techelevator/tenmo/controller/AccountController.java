package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.exception.DaoException;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.security.Principal;
import java.util.*;


/**
 * Controller to handle accounts.
 */
@RestController
@PreAuthorize("isAuthenticated()")
@RequestMapping(path = "/api/accounts")
public class AccountController {
    private AccountDao accountDao;
    private UserDao userDao;

    public AccountController(AccountDao accountDao, UserDao userDao) {
        this.accountDao = accountDao;
        this.userDao = userDao;
    }

    @RequestMapping(path = "", method = RequestMethod.GET)
    public List<Account> list(){
        return accountDao.getAllAccounts();
    }

    @RequestMapping(path = "/{accountId}", method = RequestMethod.GET)
    public Account getAccountById(@PathVariable int accountId){
        return accountDao.getAccountsByAccountId(accountId);
    }

    @RequestMapping(path = "/user/{userId}", method = RequestMethod.GET)
    public Account getAccountByUserId(@PathVariable int userId){
        return accountDao.getAccountsByUserId(userId);
    }

    @RequestMapping(path = "/{accountId}/username", method = RequestMethod.GET)
    public String getUsernameByAccountId(@PathVariable int accountId){
        return accountDao.getUsernameByAccountId(accountId);
    }

    @RequestMapping(path = "/{accountId}/balance", method = RequestMethod.GET)
    public Map<String, String> getBalanceAndUsernameByAccountId(@PathVariable int accountId) {
        Map<String, String> returnList = new LinkedHashMap<>();
        String username = accountDao.getUsernameByAccountId(accountId);
        BigDecimal balance = accountDao.getBalanceByAccountId(accountId);

        returnList.put("username", username);
        returnList.put("balance", balance.toString());
        return returnList;
    }

    @RequestMapping(path = "/get-balance", method = RequestMethod.GET)
    public Map<String, String> getCurrentUserBalance(Principal user){
        Map<String, String> returnList = new LinkedHashMap<>();
        String username = user.getName();
        int currentUserId = userDao.findIdByUsername(username);
        BigDecimal balance = accountDao.getBalanceByUserId(currentUserId);

        returnList.put("username", username);
        returnList.put("balance", balance.toString());
        return returnList;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(path = "", method = RequestMethod.POST)
    public boolean create(@Valid @RequestBody Account account) {
        return accountDao.createAccount(account);
    }

    @RequestMapping(path = "/{accountId}", method = RequestMethod.PUT)
    public boolean update(@Valid @RequestBody Account accountToUpdate, @PathVariable int accountId) {
        accountToUpdate.setAccountId(accountId);
        try {
            return accountDao.updateAccount(accountId, accountToUpdate);
        } catch (DaoException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Account Not Found");
        }
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @RequestMapping(path = "/{accountId}", method = RequestMethod.DELETE)
    public boolean delete(@Valid @PathVariable int accountId) {
        return accountDao.deleteAccount(accountId);
    }

}
