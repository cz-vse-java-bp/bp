package cz.vse.java.utils.random;

import cz.vse.java.utils.emailServices.EmailAddress;
import org.junit.Test;

import static org.junit.Assert.*;

public class RandomEmailAddressGeneratorTest {

    @Test
    public void generateRandomEmailAddress() {

        assertTrue(EmailAddress.validate(RandomEmailAddressGenerator.generateRandomEmailAddress()));
    }

    @Test
    public void generateRandomEmailAddress1() {

        assertTrue(EmailAddress.validate(RandomEmailAddressGenerator.generateRandomEmailAddress(10, 5, 2)));
    }
}