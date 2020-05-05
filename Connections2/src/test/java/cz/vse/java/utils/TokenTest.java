package cz.vse.java.utils;

import cz.vse.java.util.Token;
import org.junit.Test;

import static org.junit.Assert.*;

public class TokenTest {


    @Test
    public void generateRandomToken() {

        boolean indicator = true;

        for (int i = 0; i < 100; i++) {

            Token token = new Token();
            System.out.printf("%-4d %-50s length: %d%n", i, token.getToken(), token.length());
            if(token.length() < 30) {

                indicator = false;
            }
        }

        assertTrue(indicator);
    }
}