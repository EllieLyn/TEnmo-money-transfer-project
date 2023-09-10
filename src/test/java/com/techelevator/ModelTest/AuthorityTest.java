package com.techelevator.ModelTest;

import com.techelevator.tenmo.model.Authority;
import org.junit.Assert;
import org.junit.Test;

public class AuthorityTest {

    @Test
    public void authorities_with_same_name_should_be_equal() {
        Authority authority1 = new Authority("ROLE_USER");
        Authority authority2 = new Authority("ROLE_USER");

        Assert.assertEquals(authority1, authority2);
    }

    @Test
    public void authorities_with_different_names_should_not_be_equal() {
        Authority authority1 = new Authority("ROLE_USER");
        Authority authority2 = new Authority("ROLE_ADMIN");

        Assert.assertNotEquals(authority1, authority2);
    }

    @Test
    public void authority_toString_produces_expected_output() {
        Authority authority = new Authority("ROLE_USER");
        String expectedString = "Authority{name=ROLE_USER}";

        Assert.assertEquals(expectedString, authority.toString());
    }
}