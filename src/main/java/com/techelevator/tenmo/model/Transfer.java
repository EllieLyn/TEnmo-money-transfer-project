package com.techelevator.tenmo.model;

import javax.validation.constraints.DecimalMin;
import java.math.BigDecimal;

public class Transfer {
    private int transferId;

    private String transferType;

    private String transferStatus;

    private int transferFromAccount;

    private int transferToAccount;

    @DecimalMin(value = "1", message = "The minimum transfer balance should be at least $1!")
    private BigDecimal amount;

    public Transfer() {

    }

    public Transfer(int transferId, String transferType, String transferStatus, int transferFromAccount, int transferToAccount, BigDecimal amount) {
        this.transferId = transferId;
        this.transferType = transferType;
        this.transferStatus = transferStatus;
        this.transferFromAccount = transferFromAccount;
        this.transferToAccount = transferToAccount;
        this.amount = amount;
    }

    public int getTransferId() {
        return transferId;
    }

    public void setTransferId(int transferId) {
        this.transferId = transferId;
    }

    public String getTransferType() {
        return transferType;
    }

    public void setTransferType(String transferType) {
        this.transferType = transferType;
    }

    public String getTransferStatus() {
        return transferStatus;
    }

    public void setTransferStatus(String transferStatus) {
        this.transferStatus = transferStatus;
    }

    public int getTransferFromAccount() {
        return transferFromAccount;
    }

    public void setTransferFromAccount(int transferFromAccount) {
        this.transferFromAccount = transferFromAccount;
    }

    public int getTransferToAccount() {
        return transferToAccount;
    }

    public void setTransferToAccount(int transferToAccount) {
        this.transferToAccount = transferToAccount;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "Transfer{" +
                "transferId=" + transferId +
                ", transferType='" + transferType + '\'' +
                ", transferStatus='" + transferStatus + '\'' +
                ", transferFromAccount=" + transferFromAccount +
                ", transferToAccount=" + transferToAccount +
                ", amount=" + amount +
                '}';
    }
}
