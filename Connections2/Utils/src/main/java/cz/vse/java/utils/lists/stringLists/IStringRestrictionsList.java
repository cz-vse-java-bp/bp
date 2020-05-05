package cz.vse.java.utils.lists.stringLists;


import cz.vse.java.utils.lists.IRestrictionsList;

import java.util.ArrayList;

/**************************************************************
 * <p>Interface declaring methods of String-valued lists.</p>
 *
 * Written for project "Utils".
 * @author Vojtěch Pavlů
 * @version 06. 02. 2020
 *
 * @see StringWhiteList
 * @see StringBlackList
 */
public interface IStringRestrictionsList extends IRestrictionsList {

    /**
     * <p>Checks all the given String values if they are
     * accepted by the list of restrictions</p>
     *
     * @param values to be checked
     *
     * @return boolean result of the check
     */
    boolean checkAll(ArrayList<String> values);

    /**
     * <p>Checks if the String formed value is accepted
     * by the list of restrictions</p>
     *
     * @param value checked value
     *
     * @return      the boolean result
     */
    boolean check(String value);


    /**
     * Adds value to the list
     *
     * @param value to be added
     */
    void add(String value);


    /**
     * Adds all given values to the list
     *
     * @param values to be added
     */
    void addAll(ArrayList<String> values);


    /**
     * Method for removing the given value
     *
     * @param value to be removed
     */
    void remove(String value);


    /**
     * Method for clearing the list
     */
    void removeAll();


    /**
     * Method for removing one value from the list with
     * given index
     *
     * @param index of the value to be removed
     */
    void remove(int index);


    /**
     * Returns size of the list - number of elements.
     *
     * @return the size of the list
     */
    int size();
}
