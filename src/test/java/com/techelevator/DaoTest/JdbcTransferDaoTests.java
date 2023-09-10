package com.techelevator.DaoTest;

import com.techelevator.tenmo.dao.JdbcTransferDao;
import com.techelevator.tenmo.model.Transfer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import java.math.BigDecimal;
import java.util.List;

public class JdbcTransferDaoTests extends BaseDaoTests {
    private static final Transfer TRANSFER_1 = new Transfer(3001, "request", "pending", 2001, 2002, new BigDecimal("200.00"));
    private static final Transfer TRANSFER_2 = new Transfer(3002, "send", "approved", 2001, 2002, new BigDecimal("100.00"));

    private static final Transfer TRANSFER_3 = new Transfer(3003, "request", "rejected", 2002, 2001, new BigDecimal("60.00"));

    private static final Transfer TRANSFER_4 = new Transfer(3004, "request", "rejected", 2002, 2001, new BigDecimal("300.00"));

    private static final Transfer TRANSFER_5 = new Transfer(3005, "request", "rejected", 2003, 2004, new BigDecimal("200.00"));

    private JdbcTransferDao sut;

    @Before
    public void setup() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        sut = new JdbcTransferDao(jdbcTemplate);
    }

    @Test
    public void getAllTransfers_returns_list_of_all_transfers(){
        List<Transfer> transfers = sut.getAllTransfers();
        Assert.assertEquals(5, transfers.size());
        assertTransferMatch(TRANSFER_1, transfers.get(0));
        assertTransferMatch(TRANSFER_2, transfers.get(1));
        assertTransferMatch(TRANSFER_3, transfers.get(2));
        assertTransferMatch(TRANSFER_4, transfers.get(3));
        assertTransferMatch(TRANSFER_5, transfers.get(4));
    }

    @Test
    public void getTransferById_returns_correct_transfer_for_id(){
        Transfer transfer = sut.getTransferById(3001);
        assertTransferMatch(TRANSFER_1, transfer);

        transfer = sut.getTransferById(3002);
        assertTransferMatch(TRANSFER_2, transfer);

        transfer = sut.getTransferById(3003);
        assertTransferMatch(TRANSFER_3, transfer);

        transfer = sut.getTransferById(3004);
        assertTransferMatch(TRANSFER_4, transfer);
    }

    @Test
    public void getCurrentUserTransferById_returns_correct_transfer_for_userid_and_transfer_id(){
        Transfer transfer = sut.getCurrentUserTransferById(3001, 1001);
        assertTransferMatch(TRANSFER_1, transfer);

        transfer = sut.getCurrentUserTransferById(3003, 1002);
        assertTransferMatch(TRANSFER_3, transfer);

        transfer = sut.getCurrentUserTransferById(3001, 1003);
        Assert.assertNull("Should return null if the transfer doesn't belong to the user", transfer);

        transfer = sut.getCurrentUserTransferById(3006, 1001);
        Assert.assertNull("Should return null if the transfer doesn't exist", transfer);
    }

    @Test
    public void getTransfersByUserId_returns_correct_transfer_for_user_id(){
        List<Transfer> transfers = sut.getTransfersByUserId(1001);
        Assert.assertEquals(4, transfers.size());
        assertTransferMatch(TRANSFER_1, transfers.get(0));
        assertTransferMatch(TRANSFER_2, transfers.get(1));
        assertTransferMatch(TRANSFER_3, transfers.get(2));
        assertTransferMatch(TRANSFER_4, transfers.get(3));
    }

    @Test
    public void getTransfersByAccountId_returns_correct_transfer_for_account_id(){
        List<Transfer> transfers = sut.getTransfersByUserId(1001);
        Assert.assertEquals(4, transfers.size());
        assertTransferMatch(TRANSFER_1, transfers.get(0));
        assertTransferMatch(TRANSFER_2, transfers.get(1));
        assertTransferMatch(TRANSFER_3, transfers.get(2));
        assertTransferMatch(TRANSFER_4, transfers.get(3));
    }

    @Test
    public void createTransfer_has_expected_values_when_retrieved(){
        Transfer testTransfer = new Transfer(0000, "request", "pending", 2003, 2004, new BigDecimal("200.00"));
        boolean expectedTrue = sut.createTransfer(testTransfer);
        Assert.assertTrue(expectedTrue);
    }

    @Test
    public void updateTransfer_has_expected_values_when_retrieved(){
        Transfer updatedTransfer = sut.getTransferById(3001);

        updatedTransfer.setTransferStatus("approved");
        updatedTransfer.setTransferType("send");
        updatedTransfer.setAmount(new BigDecimal("60.00"));
        updatedTransfer.setTransferToAccount(2003);

        sut.updateTransfer(updatedTransfer, updatedTransfer.getTransferId());
        Transfer retrievedTransfer = sut.getTransferById(3001);
        assertTransferMatch(updatedTransfer, retrievedTransfer);
    }

    @Test
    public void delete_transfer_can_no_longer_be_retrieved(){
        sut.deleteTransfer(3001);
        Transfer retrievedTransfer = sut.getTransferById(3001);
        Assert.assertNull(retrievedTransfer);
    }

    public void assertTransferMatch(Transfer expected, Transfer actual){
        Assert.assertEquals(expected.getTransferId(), actual.getTransferId());
        Assert.assertEquals(expected.getTransferType(), actual.getTransferType());
        Assert.assertEquals(expected.getTransferStatus(), actual.getTransferStatus());
        Assert.assertEquals(expected.getTransferFromAccount(), actual.getTransferFromAccount());
        Assert.assertEquals(expected.getTransferToAccount(), actual.getTransferToAccount());
        Assert.assertEquals(expected.getAmount(), actual.getAmount());
    }

}


