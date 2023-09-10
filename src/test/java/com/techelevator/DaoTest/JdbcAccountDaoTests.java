package com.techelevator.DaoTest;

import com.techelevator.tenmo.dao.JdbcAccountDao;
import com.techelevator.tenmo.model.Account;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import java.math.BigDecimal;
import java.util.List;

public class JdbcAccountDaoTests extends BaseDaoTests {
    private static final Account ACCOUNT_1 = new Account(2001, 1001, new BigDecimal("1000.00"));
    private static final Account ACCOUNT_2 = new Account(2002, 1002, new BigDecimal("2000.00"));
    private static final Account ACCOUNT_3 = new Account(2003, 1003, new BigDecimal("1000.00"));
    private static final Account ACCOUNT_4 = new Account(2004, 1004, new BigDecimal("2000.00"));

    private JdbcAccountDao sut;

    @Before
    public void setup() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        sut = new JdbcAccountDao(jdbcTemplate);
    }

    @Test
    public void getAllAccountsExceptCurrentUser_returns_correct_list_of_accounts() {
        List<Account> accounts = sut.getAllAccountsExceptCurrentUser(1004);
        Assert.assertEquals(3, accounts.size());
        assertAccountMatch(ACCOUNT_1, accounts.get(0));
        assertAccountMatch(ACCOUNT_2, accounts.get(1));
        assertAccountMatch(ACCOUNT_3, accounts.get(2));
    }

    @Test
    public void getAllAccounts_returns_correct_list_of_accounts() {
        List<Account> accounts = sut.getAllAccounts();
        Assert.assertEquals(4, accounts.size());
        assertAccountMatch(ACCOUNT_1, accounts.get(0));
        assertAccountMatch(ACCOUNT_2, accounts.get(1));
        assertAccountMatch(ACCOUNT_3, accounts.get(2));
        assertAccountMatch(ACCOUNT_4, accounts.get(3));
    }

    @Test
    public void getAccountsByAccountId_returns_correct_account_for_account_id() {
        Account accounts = sut.getAccountsByAccountId(2001);
        assertAccountMatch(ACCOUNT_1, accounts);

        accounts = sut.getAccountsByAccountId(2002);
        assertAccountMatch(ACCOUNT_2, accounts);

        accounts = sut.getAccountsByAccountId(2003);
        assertAccountMatch(ACCOUNT_3, accounts);

        accounts = sut.getAccountsByAccountId(2004);
        assertAccountMatch(ACCOUNT_4, accounts);

        Assert.assertNull("Should return null if the account doesn't exist", sut.getAccountsByAccountId(2009));
    }

    @Test
    public void getAccountsByUserId_returns_correct_account_for_user_id() {
        Account accounts = sut.getAccountsByUserId(1001);
        assertAccountMatch(ACCOUNT_1, accounts);

        accounts = sut.getAccountsByUserId(1002);
        assertAccountMatch(ACCOUNT_2, accounts);

        accounts = sut.getAccountsByUserId(1003);
        assertAccountMatch(ACCOUNT_3, accounts);

        accounts = sut.getAccountsByUserId(1004);
        assertAccountMatch(ACCOUNT_4, accounts);
    }

    @Test
    public void getBalanceByUserId_returns_correct_balance_for_user_id() {
        BigDecimal balance = sut.getBalanceByUserId(1001);
        Assert.assertEquals(ACCOUNT_1.getBalance(), balance);

        balance = sut.getBalanceByUserId(1002);
        Assert.assertEquals(ACCOUNT_2.getBalance(), balance);

        balance = sut.getBalanceByUserId(1003);
        Assert.assertEquals(ACCOUNT_3.getBalance(), balance);

        balance = sut.getBalanceByUserId(1004);
        Assert.assertEquals(ACCOUNT_4.getBalance(), balance);
    }

    @Test
    public void getBalanceByAccountId_returns_correct_balance_for_account_id() {
        BigDecimal balance = sut.getBalanceByAccountId(2001);
        Assert.assertEquals(ACCOUNT_1.getBalance(), balance);

        balance = sut.getBalanceByAccountId(2002);
        Assert.assertEquals(ACCOUNT_2.getBalance(), balance);

        balance = sut.getBalanceByAccountId(2003);
        Assert.assertEquals(ACCOUNT_3.getBalance(), balance);

        balance = sut.getBalanceByAccountId(2004);
        Assert.assertEquals(ACCOUNT_4.getBalance(), balance);
    }

    @Test
    public void getUsernameByAccountId_returns_correct_username_for_account_id() {
        String username = sut.getUsernameByAccountId(2001);
        Assert.assertEquals("bob", username);

        username = sut.getUsernameByAccountId(2002);
        Assert.assertEquals("user", username);

        username = sut.getUsernameByAccountId(2003);
        Assert.assertEquals("testUser1", username);

        username = sut.getUsernameByAccountId(2004);
        Assert.assertEquals("testUser2", username);
    }

    @Test
    public void depositToAccountBalance_returns_correct_increased_balance_for_account_id() {
        sut.depositToAccountBalance(new BigDecimal("100.00"), 2001);
        Assert.assertEquals(new BigDecimal("1100.00"), sut.getBalanceByAccountId(2001));

        sut.depositToAccountBalance(new BigDecimal("500.00"), 2002);
        Assert.assertEquals(new BigDecimal("2500.00"), sut.getBalanceByAccountId(2002));

        sut.depositToAccountBalance(new BigDecimal("500.00"), 2003);
        Assert.assertEquals(new BigDecimal("1500.00"), sut.getBalanceByAccountId(2003));
    }

    @Test
    public void withdrawFromAccountBalance_returns_correct_decreased_balance_for_account_id() {
        sut.withdrawFromAccountBalance(new BigDecimal("100.00"), 2001);
        Assert.assertEquals(new BigDecimal("900.00"), sut.getBalanceByAccountId(2001));

        sut.withdrawFromAccountBalance(new BigDecimal("500.00"), 2002);
        Assert.assertEquals(new BigDecimal("1500.00"), sut.getBalanceByAccountId(2002));

        sut.withdrawFromAccountBalance(new BigDecimal("500.00"), 2003);
        Assert.assertEquals(new BigDecimal("500.00"), sut.getBalanceByAccountId(2003));
    }

    @Test
    public void createAccount_has_expected_values_when_retrieved(){
        Account testAccount = new Account(0000, 1005, new BigDecimal("2000.00"));
        Assert.assertTrue(sut.createAccount(testAccount));

        Account createdAccount = sut.getAccountsByUserId(1005);
        assertAccountMatch(testAccount, createdAccount);
    }

    @Test
    public void updateAccount_has_expected_values_when_retrieved(){
        Account updatedAccount = sut.getAccountsByAccountId(2001);

        updatedAccount.setBalance(new BigDecimal("5000.00"));
        updatedAccount.setUserId(1003);

        sut.updateAccount(updatedAccount.getAccountId(), updatedAccount);
        Account retrievedAccount = sut.getAccountsByAccountId(2001);
        assertAccountMatch(updatedAccount, retrievedAccount);
    }

    @Test
    public void delete_account_can_no_longer_be_retrieved(){
        sut.deleteAccount(2001);
        Account retrievedAccount = sut.getAccountsByAccountId(2001);
        Assert.assertNull(retrievedAccount);
    }

    private void assertAccountMatch(Account expected, Account actual){
        Assert.assertEquals(expected.getAccountId(), actual.getAccountId());
        Assert.assertEquals(expected.getUserId(), actual.getUserId());
        Assert.assertEquals(expected.getBalance(), actual.getBalance());
    }
}
