package com.techelevator.DaoTest;


import com.techelevator.tenmo.dao.JdbcUserDao;
import com.techelevator.tenmo.model.User;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

public class JdbcUserDaoTests extends BaseDaoTests{

    private static final User USER_1 = new User(1001, "bob", "$2a$10$G/MIQ7pUYupiVi72DxqHquxl73zfd7ZLNBoB2G6zUb.W16imI2.W2", "Authorized");
    private static final User USER_2 = new User(1002, "user", "$2a$10$Ud8gSvRS4G1MijNgxXWzcexeXlVs4kWDOkjE7JFIkNLKEuE57JAEy", "Authorized");
    private static final User USER_3 = new User(1003, "testUser1", "$2a$10$Ud8gSvRS4G1MijNgxXWzcexeXlVs4kWDOkresfsddNLKEuE57JAEy", "Authorized");

    private JdbcUserDao sut;

    @Before
    public void setup() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        sut = new JdbcUserDao(jdbcTemplate);
    }

    @Test
    public void createNewUser() {
        boolean userCreated = sut.create("TEST_USER","test_password");
        Assert.assertTrue(userCreated);
        User user = sut.findUserByUsername("TEST_USER");
        Assert.assertEquals("TEST_USER", user.getUsername());
    }

    @Test
    public void findAll_returns_list_of_all_users(){
        List<User> users = sut.findAll();
        Assert.assertEquals(5, users.size());
        assertUserMatch(USER_1, users.get(0));
        assertUserMatch(USER_2, users.get(1));
        assertUserMatch(USER_3, users.get(2));
    }

    @Test
    public void findIdByUsername_returns_correct_id_for_username(){
        int userId = sut.findIdByUsername("bob");
        Assert.assertEquals(1001, userId);

        userId = sut.findIdByUsername("user");
        Assert.assertEquals(1002, userId);

        userId = sut.findIdByUsername("testUser1");
        Assert.assertEquals(1003, userId);
    }

    @Test
    public void findUserById_returns_correct_user_for_id(){
        User user = sut.findUserById(1001);
        assertUserMatch(USER_1, user);

        user = sut.findUserById(1002);
        assertUserMatch(USER_2, user);

        user = sut.findUserById(1003);
        assertUserMatch(USER_3, user);
    }

    @Test
    public void findUserByUsername_returns_correct_user_for_username(){
        User user = sut.findUserByUsername("bob");
        assertUserMatch(USER_1, user);

        user = sut.findUserByUsername("user");
        assertUserMatch(USER_2, user);

        user = sut.findUserByUsername("testUser1");
        assertUserMatch(USER_3, user);
    }

    @Test
    public void update_has_expected_values_when_retrieved(){
        User updatedUser = sut.findUserById(1001);
        updatedUser.setUsername("approved");
        updatedUser.setPassword("send");
        updatedUser.setActivated(false);

        sut.update(1001, updatedUser);
        User retrievedUser = sut.findUserById(1001);
        assertUserMatch(updatedUser, retrievedUser);
    }

    @Test
    public void delete_transfer_can_no_longer_be_retrieved(){
        sut.delete(1001);
        User retrievedUser = sut.findUserById(1001);
        Assert.assertNull(retrievedUser);
    }

    public void assertUserMatch(User expected, User actual){
        Assert.assertEquals(expected.getId(), actual.getId());
        Assert.assertEquals(expected.getUsername(), actual.getUsername());
        Assert.assertEquals(expected.getPassword(), actual.getPassword());
    }

}
