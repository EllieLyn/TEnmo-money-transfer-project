package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

public interface AccountDao {

    /**
     * Retrieve all accounts in the Tenmo system except for current user
     *
     * @param userId the id of the user to retrieve
     * @return the retrieved accounts
     */
    List<Account> getAllAccountsExceptCurrentUser(int userId);

    /**
     * Retrieve all accounts in the Tenmo system
     *
     * @return the retrieved accounts
     */
    List<Account> getAllAccounts();

    /**
     * Retrieve account by the given account id.
     *
     * @param accountId the id of the account to retrieve
     * @return the retrieved account
     */
    Account getAccountsByAccountId(int accountId);

    /**
     * Retrieve account by the given user id.
     *
     * @param userId the id of the user to retrieve
     * @return the retrieved account
     */
    Account getAccountsByUserId(int userId);

    /**
     * Retrieve balance by the given user id.
     *
     * @param userId the id of the user to retrieve
     * @return the retrieved balance
     */
    BigDecimal getBalanceByUserId (int userId);

    /**
     * Retrieve balance by the given account id.
     *
     * @param accountId the id of the account to retrieve
     * @return the retrieved balance
     */
    BigDecimal getBalanceByAccountId (int accountId);

    /**
     * Retrieve username by the given account id.
     *
     * @param accountId the id of the account to retrieve
     * @return the retrieved username
     */
    String getUsernameByAccountId (int accountId);

    /**
     * Increase account balance by the given amount.
     *
     * @param amount the amount BigDecimal to add
     * @param accountId the id of the account to retrieve
     * @return the boolean decision of whether the account balance could be updated
     */
    boolean depositToAccountBalance (BigDecimal amount,int accountId);

    /**
     * Decrease account balance by the given amount.
     *
     * @param amount the amount BigDecimal to subtract
     * @param accountId the id of the account to retrieve
     * @return the boolean decision of whether the account balance could be updated
     */
    boolean withdrawFromAccountBalance (BigDecimal amount,int accountId);

    /**
     * Inserts a new account into the Tenmo system.
     *
     * @param accountToCreate the account object to insert
     * @return the boolean decision of whether the account could be created in the system
     */
    boolean createAccount (Account accountToCreate);

    /**
     * Updates an existing account in the Tenmo system.
     *
     * @param accountToUpdate the account object to update
     * @param accountId the account id integer to retrieve
     * @return the boolean decision of whether the account could be updated in the system
     */
    boolean updateAccount (int accountId, Account accountToUpdate);

    /**
     * Removes an account from the Tenmo system.
     *
     * @param accountId the id of the account to remove
     * @return the boolean decision of whether the account could be deleted in the system
     */
    boolean deleteAccount (int accountId);
}
