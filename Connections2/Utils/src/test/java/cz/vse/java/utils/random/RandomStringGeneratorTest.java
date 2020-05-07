package cz.vse.java.utils.random;

import org.junit.Test;

import static org.junit.Assert.*;

public class RandomStringGeneratorTest {

    @Test
    public void randomStringOfSpecLength() {

        for (int i = 1; i < 300; i++) {

            RandomStringGenerator rsg = new RandomStringGenerator();
            String rs = rsg.generateRandomString(i);

            if(rs.length() != i) {

                fail();
            }
        }
    }

    @Test
    public void generateRandomStringInRange() {

        for (int i = 1; i < 300; i++) {

            RandomStringGenerator rsg = new RandomStringGenerator();
            String rs = rsg.generateRandomString(5, 50);

            if(rs.length() > 50 || rs.length() < 5) {

                fail();
            }
        }
    }
}