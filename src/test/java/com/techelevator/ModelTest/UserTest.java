package com.techelevator.ModelTest;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.User;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

public class UserTest {
    private User sut;
    @Before
    public void setup(){
        this.sut = new User(1001, "bob", "$2a$10$G/MIQ7pUYupiVi72DxqHquxl73zfd7ZLNBoB2G6zUb.W16imI2.W2", "authorized");
    }

    @Test
    public void getId_returns_the_correct_user_id(){
        int expectedUserId = 1001;
        Assert.assertEquals(expectedUserId, sut.getId());
    }

    @Test
    public void getUsername_returns_the_correct_user_name(){
        String expectedUserName = "bob";
        Assert.assertEquals(expectedUserName, sut.getUsername());
    }

    @Test
    public void getPassword_returns_the_correct_password(){
        String expectedPassword = "$2a$10$G/MIQ7pUYupiVi72DxqHquxl73zfd7ZLNBoB2G6zUb.W16imI2.W2";
        Assert.assertEquals(expectedPassword, sut.getPassword());
    }

    @Test
    public void setId_has_expected_values_when_retrieved(){
        int expectedUserId = 1111;
        sut.setId(1111);
        Assert.assertEquals(expectedUserId, sut.getId());
    }

    @Test
    public void setUsername_has_expected_values_when_retrieved(){
        String expectedUserName = "bobbbb";
        sut.setUsername("bobbbb");
        Assert.assertEquals(expectedUserName, sut.getUsername());
    }

    @Test
    public void setPassword_has_expected_values_when_retrieved(){
        String expectedPassword = "ABCDefg";
        sut.setPassword("ABCDefg");
        Assert.assertEquals(expectedPassword, sut.getPassword());
    }

    @Test
    public void isActivated_has_expected_values_when_retrieved(){
        Boolean expected = true;
        Assert.assertEquals(expected, sut.isActivated());
    }

    @Test
    public void setActivated_has_expected_values_when_retrieved(){
        Boolean expected = false;
        sut.setActivated(false);
        Assert.assertEquals(expected, sut.isActivated());
    }

}
