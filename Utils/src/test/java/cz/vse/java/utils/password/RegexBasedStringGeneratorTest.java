package cz.vse.java.utils.password;

import cz.vse.java.utils.random.Charsets;
import cz.vse.java.utils.random.RegexBasedStringGenerator;
import org.junit.Test;

import static org.junit.Assert.*;

public class RegexBasedStringGeneratorTest {

    @Test
    public void generate() {

        RegexBasedStringGenerator gen = new RegexBasedStringGenerator(
                Charsets.NUMBERS,
                Charsets.LETTERS,
                Charsets.LETTERS,
                Charsets.LOWER_CASES,
                Charsets.NUMBERS,
                Charsets.LETTERS,
                Charsets.LETTERS,
                Charsets.LOWER_CASES
        );

        for (int i = 0; i < 50; i++) {

            String password = gen.generate();
            assertTrue(password.matches("[0-9a-zA-Z]{8}"));
        }
    }
}