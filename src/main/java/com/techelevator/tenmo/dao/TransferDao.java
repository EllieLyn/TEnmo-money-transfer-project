package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;
import org.springframework.stereotype.Component;

import java.util.List;

public interface TransferDao {

    /**
     * Retrieve all transfers in the Tenmo system
     *
     * @return the retrieved transfers
     */
    List<Transfer> getAllTransfers ();

    /**
     * Retrieve transfer by the given transfer id.
     *@param transferId the id of the transfer to retrieve
     * @return the retrieved transfer
     */
    Transfer getTransferById (int transferId);

    /**
     * Retrieve transfer by the given transfer id and user id.
     *@param transferId the id of the transfer to retrieve
     *@param userId the id of the user to retrieve
     * @return the retrieved transfer
     */
    Transfer getCurrentUserTransferById(int transferId, int userId);

    /**
     * Retrieve transfer by the given user id.
     *@param userId the user id of the transfers to retrieve
     * @return the list of retrieved transfers
     */
    List<Transfer> getTransfersByUserId (int userId);

    /**
     * Retrieve transfer by the given account id.
     *@param accountId the account id of the transfers to retrieve
     * @return the list of retrieved transfers
     */
    List<Transfer> getTransfersByAccountId (int accountId);

    /**
     * Inserts a new transfer into the Tenmo system.
     *
     * @param transferToCreate the transfer object to insert
     * @return the boolean decision of whether the transfer could be created in the system
     */
    Boolean createTransfer (Transfer transferToCreate);

    /**
     * Updates an existing transfer in the Tenmo system.
     *
     * @param transferToUpdate the transfer object to update
     * @param transferId the transfer id integer to retrieve
     * @return the boolean decision of whether the transfer could be updated in the system
     */
    Boolean updateTransfer (Transfer transferToUpdate, int transferId);

    /**
     * Removes a transfer from the Tenmo system.
     *
     * @param TransferId the id of the transfer to remove
     * @return the boolean decision of whether the transfer could be deleted in the system
     */
    Boolean deleteTransfer (int TransferId);

}
