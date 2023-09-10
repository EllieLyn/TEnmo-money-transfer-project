package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.User;
import org.springframework.stereotype.Component;

import java.util.List;

public interface UserDao {

    /**
     * Retrieve all users in the Tenmo system
     *
     * @return the retrieved users
     */
    List<User> findAll();

    /**
     * Retrieve user by the given user id.
     *@param userId the id of the user to retrieve
     * @return the retrieved user
     */
    User findUserById(int userId);

    /**
     * Retrieve user by the given username.
     *@param username the username of the user to retrieve
     * @return the retrieved user
     */
    User findUserByUsername(String username);

    /**
     * Retrieve user id by the given username.
     *@param username the username of the user to retrieve
     * @return the retrieved user id of user
     */
    int findIdByUsername(String username);

    /**
     * Inserts a new user into the Tenmo system.
     *
     * @param username the username string to insert
     * @param password the password string to insert
     * @return the boolean decision of whether the user could be created in the system
     */
    boolean create(String username, String password);

    /**
     * Updates an existing user in the Tenmo system.
     *
     * @param userId the user id integer to retrieve
     * @param userToUpdate the user object to update
     * @return the boolean decision of whether the user could be updated in the system
     */
    boolean update(int userId, User userToUpdate);

    /**
     * Removes a user from the Tenmo system.
     *
     * @param userId the id of the user to remove
     * @return the boolean decision of whether the user could be deleted in the system
     */
    boolean delete(int userId);
}
