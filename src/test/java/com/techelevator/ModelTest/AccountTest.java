package com.techelevator.ModelTest;

import com.techelevator.tenmo.model.Account;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

public class AccountTest {
    private Account sut;

    @Before
    public void setup(){
        this.sut = new Account(2001, 1001, new BigDecimal("1000.00"));
    }

    @Test
    public void getAccountId_returns_the_correct_account_id(){
        int expectedAccountId = 2001;
        Assert.assertEquals(expectedAccountId, sut.getAccountId());
    }

    @Test
    public void getUserId_returns_the_correct_user_id(){
        int expectedUserId = 1001;
        Assert.assertEquals(expectedUserId, sut.getUserId());
    }

    @Test
    public void getBalance_returns_the_correct_balance(){
        BigDecimal expectedBalance = new BigDecimal("1000.00");
        Assert.assertEquals(expectedBalance, sut.getBalance());
    }

    @Test
    public void setAccountId_has_expected_values_when_retrieved(){
        int expectedAccountId = 2222;
        sut.setAccountId(2222);
        Assert.assertEquals(expectedAccountId, sut.getAccountId());
    }

    @Test
    public void setUserId_has_expected_values_when_retrieved(){
        int expectedUserId = 1111;
        sut.setUserId(1111);
        Assert.assertEquals(expectedUserId, sut.getUserId());
    }

    @Test
    public void setBalance_has_expected_values_when_retrieved(){
        BigDecimal expectedBalance = new BigDecimal("3000.00");
        sut.setBalance(new BigDecimal("3000.00"));
        Assert.assertEquals(expectedBalance, sut.getBalance());
    }

    @Test
    public void toString_has_expected_values_when_retrieved(){
        String expectedString = "Account{accountId=2001, userId=1001, balance=1000.00}";
        Assert.assertEquals(expectedString, sut.toString());
    }

}
