package cz.vse.java.hashing;

import cz.vse.java.utils.cryptography.hashing.Hasher;
import org.junit.Test;

import static org.junit.Assert.*;

public class HasherTest {


    @Test
    public void simpleHashing() throws Exception {

        Hasher h = new Hasher(0);
        String hash = h.generateHash("abcd");

        assertTrue(h.checkHash("abcd", hash));
    }


    @Test
    public void hashWithSalt() throws Exception {

        Hasher h = new Hasher(3);
        String[] hash = h.generateHashWithSalt("abcd", 3);

        assertTrue(h.checkHashWithSalt("abcd", hash[0], hash[1]));
    }

    @Test
    public void hashWithSaltAndPepper() throws Exception {

        Hasher h = new Hasher(3);
        String[] hash1 = h.generateHashWithSaltAndPepper("password", 1);

        assertTrue(h.checkHashWithSaltAndPepper("password", hash1[0], hash1[1], 1));
    }

    @Test
    public void hexToBytesTest() throws Exception {

        String defaultString = "ABCD1234";

        assertEquals(Hasher.bytesToHex(Hasher.hexToBytes(defaultString)), defaultString);
    }
}