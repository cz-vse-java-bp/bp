package cz.vse.java.utils.lists.stringLists;


import cz.vse.java.utils.lists.IRestrictionsList;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/*******************************************************************
 * <p>Class of {@code StringBlackList} is used for containing list
 *  of all acceptable values.</p>
 *
 * <p>The class implement's {@link IRestrictionsList} for higher
 * variability to use it anywhere it's needed.</p>
 *
 * <p>The main reason to use it is to collect all the needed values
 * and be able to work with them interactively and intuitively as
 * much as possible.</p>
 *
 * <b>THREAD-SAFE using {@link CopyOnWriteArrayList}</b>
 *
 * Written for project "Utils".
 * @author Vojtěch Pavlů
 * @version 26. 02. 2020
 *
 * @see IRestrictionsList
 * @see IStringRestrictionsList
 * @see StringWhiteList
 * @see List
 * @see CopyOnWriteArrayList
 */
public class StringBlackList implements IStringRestrictionsList {


    /* *******************************************************/
    /* Instance variables ************************************/

    /** The rejected values container */
    private CopyOnWriteArrayList<String> rejectedValues;

    /* *******************************************************/
    /* Static variables **************************************/

    /** Logger */
    private static final Logger LOG = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    /* *******************************************************/
    /* Constructors ******************************************/

    /**
     * <p>The parametric constructor used for defining the {@link CopyOnWriteArrayList}
     * of rejected values.</p>
     *
     * @param list  List of acceptable values.
     */
    public StringBlackList(List<String> list) {

        this.rejectedValues = new CopyOnWriteArrayList<>();
        for (String val : list) {

            if(!rejectedValues.contains(val)){

                rejectedValues.add(val);
            }
        }
    }

    /**
     * <p>The non-parametric constructor used for defining the {@link CopyOnWriteArrayList}
     * of rejected values.</p>
     */
    public StringBlackList(){

        this.rejectedValues = new CopyOnWriteArrayList<>();
    }

    /* *******************************************************/
    /* Instance methods **************************************/


    /**
     * Method used for adding a value to the {@link CopyOnWriteArrayList} of
     * rejected values.
     *
     * @param value value to be added
     */
    @Override
    public void add(String value){

        synchronized (this) {

            if (!rejectedValues.contains(value)) {

                this.rejectedValues.add(value);
                LOG.log(Level.INFO, "Value added");
            }
        }
    }

    /**
     * Method for adding all given values.
     *
     * @param values to be given
     */
    @Override
    public void addAll(ArrayList<String> values){

        synchronized (this) {

            for (String value : values) {

                this.add(value);
            }
            LOG.log(Level.INFO, "Given values added.");
        }
    }


    /**
     * Method used for removing a value from the {@link CopyOnWriteArrayList} of
     * rejected values.
     *
     * @param value value to be removed
     */
    @Override
    public void remove(String value){

        synchronized (this) {

            this.rejectedValues.remove(value);
            LOG.log(Level.INFO, "Value removed.");
        }
    }


    /**
     * Method used for removing a value from the {@link CopyOnWriteArrayList} of
     * rejected values.
     *
     * @param index index of the value to be removed
     */
    @Override
    public void remove(int index){

        synchronized (this) {

            this.rejectedValues.remove(index);
            LOG.log(Level.INFO, "Value with index of " + index + " removed");
        }
    }


    /**
     * Method used for removing all values from the {@link CopyOnWriteArrayList} of
     * rejected values.
     */
    @Override
    public void removeAll(){

        synchronized (this) {

            this.rejectedValues = new CopyOnWriteArrayList<>();
            LOG.log(Level.INFO, "All values removed.");
        }
    }


    /**
     * For getting the number of the values in the list
     *
     * @return integer number of the size of the list
     */
    @Override
    public int size(){

        return this.rejectedValues.size();
    }


    /**
     * <p>Checks if the String formed value is rejected
     * by the list of restrictions</p>
     *
     * @param value checked value
     *
     * @return      the boolean result
     */
    @Override
    public boolean check(String value) {

        boolean b = !rejectedValues.contains(value);

        LOG.log(Level.INFO, "Checking value result: " + b);
        return b;
    }


    /**
     * <p>Checks all the given String values if they are
     * accepted by the list of restrictions</p>
     *
     * @param values to be checked
     *
     * @return the result of checking all the values
     */
    @Override
    public boolean checkAll(ArrayList<String> values){

        boolean result = true;

        for (String value : values) {

            if(!check(value)){
                result = false;
                break;
            }
        }

        LOG.log(Level.INFO, "Result of checking all the values: " + result);
        return result;
    }


    /**
     * Overriden method of toString() from the class of {@link Object}
     *
     * @return the String formed instance of this class.
     */
    @Override
    public String toString() {
        return "StringBlackList{" +
                "rejectedValues=" + rejectedValues +
                '}';
    }


    /* *******************************************************/
    /* Static methods ****************************************/


    /* *******************************************************/
    /* Getters ***********************************************/

    /**
     * Getter for {@link List} formed {@code rejectedValues}
     * of the instance of {@link StringBlackList}
     *
     * @return the value of {@code rejectedValues}
     * @see CopyOnWriteArrayList
     * @see List
     * @see StringBlackList
     */
    public List<String> getRejectedValues() {

        return new CopyOnWriteArrayList<>(rejectedValues);
    }
}
