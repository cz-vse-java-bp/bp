package cz.vse.java.utils.random;

import org.junit.Test;

import static org.junit.Assert.*;

public class RandomNumberGeneratorTest {

    @Test
    public void getRandomNumberInRange() {

        for (int i = 0; i < 10000; i++) {

            for (int j = 1; j < 100; j++) {

                if(j >= i) {
                    System.out.println(i + " " + j + " " + RandomNumberGenerator.getRandomNumberInRange(i, j));
                }
            }
        }
    }

    @Test
    public void correctness() {

        boolean indicator = true;
        for (int i = 0; i < 10000; i++) {

            int random = RandomNumberGenerator.getRandomNumberInRange(10, 20);

            if(random < 10 || random > 20) {

                indicator = false;
                System.err.println(i + ") ERROR: " + random);
                break;
            } else {
                System.out.println(i + ") OK: " + random);
            }
        }
        assertTrue(indicator);
    }
}