package cz.vse.java.utils.lists.stringLists;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class StringBlackListTest {

    @Test
    public void check() {

        StringBlackList list = new StringBlackList();
        list.add("abcd");

        assertFalse(list.check("abcd"));
        assertTrue(list.check("dcba"));
    }


    @Test
    public void managingValues() {

        StringBlackList list = new StringBlackList();
        list.add("abcd");
        list.add("bcda");

        assertEquals(list.size(), 2);

        list.remove(1);
        list.remove("abcd");

        assertEquals(list.size(), 0);
    }

    @Test
    public void addAll() {

        ArrayList<String> values = new ArrayList<>();
        values.add("abcd");
        values.add("dcba");

        StringBlackList list = new StringBlackList(values);

        values.clear();
        values.add("adfubaaga");
        values.add("afhbawihaerg");
        values.add("asfhbawivouwen");

        list.addAll(values);

        assertEquals(list.size(), 5);
    }

    @Test
    public void addIfDoesNotContain() {

        ArrayList<String> values = new ArrayList<>();
        values.add("abcd");
        values.add("abcd");

        assertEquals(new StringBlackList(values).size(), 1);

        StringBlackList list = new StringBlackList(values);
        list.addAll(values);

        assertEquals(list.size(), 1);
    }

}