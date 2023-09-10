package com.techelevator.ModelTest;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

public class TransferTest {
    private Transfer sut;

    @Before
    public void setup(){
        this.sut = new Transfer(3001, "request", "pending", 2001, 2002, new BigDecimal("200.00"));
    }

    @Test
    public void getTransferId_returns_the_correct_transfer_id(){
        int expectedTransferId = 3001;
        Assert.assertEquals(expectedTransferId, sut.getTransferId());
    }

    @Test
    public void getTransferType_returns_the_correct_transfer_type(){
        String expectedTransferType = "request";
        Assert.assertEquals(expectedTransferType, sut.getTransferType());
    }

    @Test
    public void getTransferStatus_returns_the_correct_transfer_status(){
        String expectedTransferStatus = "pending";
        Assert.assertEquals(expectedTransferStatus, sut.getTransferStatus());
    }

    @Test
    public void getTransferFromAccount_returns_the__correct_account_id(){
        int expectedAccountId = 2001;
        Assert.assertEquals(expectedAccountId, sut.getTransferFromAccount());
    }

    @Test
    public void getTransferToAccount_returns_the__correct_account_id(){
        int expectedAccountId = 2002;
        Assert.assertEquals(expectedAccountId, sut.getTransferToAccount());
    }

    @Test
    public void getAmount_returns_the__correct_amount(){
        BigDecimal expectedAmount = new BigDecimal("200.00");
        Assert.assertEquals(expectedAmount, sut.getAmount());
    }

    @Test
    public void setTransferId_has_expected_values_when_retrieved(){
        int expectedTransferId = 3333;
        sut.setTransferId(3333);
        Assert.assertEquals(expectedTransferId, sut.getTransferId());
    }

    @Test
    public void setTransferType_has_expected_values_when_retrieved(){
        String expectedTransferType = "send";
        sut.setTransferType("send");
        Assert.assertEquals(expectedTransferType, sut.getTransferType());
    }

    @Test
    public void setTransferStatus_has_expected_values_when_retrieved(){
        String expectedTransferStatus = "approved";
        sut.setTransferStatus("approved");
        Assert.assertEquals(expectedTransferStatus, sut.getTransferStatus());
    }

    @Test
    public void setTransferFromAccount_has_expected_values_when_retrieved(){
        int expectedAccountId = 2221;
        sut.setTransferFromAccount(2221);
        Assert.assertEquals(expectedAccountId, sut.getTransferFromAccount());
    }

    @Test
    public void setTransferToAccount_has_expected_values_when_retrieved(){
        int expectedAccountId = 2222;
        sut.setTransferToAccount(2222);
        Assert.assertEquals(expectedAccountId, sut.getTransferToAccount());
    }

    @Test
    public void setAmount_has_expected_values_when_retrieved(){
        BigDecimal expectedAmount = new BigDecimal("600.00");
        sut.setAmount(new BigDecimal("600.00"));
        Assert.assertEquals(expectedAmount, sut.getAmount());
    }

    @Test
    public void toString_has_expected_values_when_retrieved(){
        String expectedString = "Transfer{transferId=3001, transferType='request', transferStatus='pending', transferFromAccount=2001, transferToAccount=2002, amount=200.00}";
        Assert.assertEquals(expectedString, sut.toString());
    }
}
