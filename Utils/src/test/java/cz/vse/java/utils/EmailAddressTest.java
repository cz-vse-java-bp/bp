package cz.vse.java.utils;


import cz.vse.java.utils.emailServices.EmailAddress;
import org.junit.Test;

import static org.junit.Assert.*;

public class EmailAddressTest {

    @Test
    public void validatePositive() throws Exception {

        assertTrue(EmailAddress.validate("abc@def.gh"));
    }

    @Test
    public void validateNegative1() throws Exception {

        assertFalse(EmailAddress.validate("@def.gh"));
    }

    @Test
    public void validateNegative2() throws Exception {

        assertFalse(EmailAddress.validate("abcdef.gh"));
    }

    @Test
    public void validateNegative3() throws Exception {

        assertFalse(EmailAddress.validate("abc@def"));
    }

    @Test
    public void validateNegative4() throws Exception {

        assertFalse(EmailAddress.validate("abc@def."));
    }
}