package cz.vse.java.utils.lists.stringLists;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class StringWhiteListTest {

    @Test
    public void check() {

        StringWhiteList list = new StringWhiteList();
        list.add("abcd");

        assertTrue(list.check("abcd"));
        assertFalse(list.check("dcba"));
    }


    @Test
    public void managingValues() {

        StringWhiteList list = new StringWhiteList();
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

        StringWhiteList list = new StringWhiteList(values);

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

        assertEquals(new StringWhiteList(values).size(), 1);

        StringWhiteList list = new StringWhiteList(values);
        list.addAll(values);

        assertEquals(list.size(), 1);
    }

}