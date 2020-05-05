package cz.vse.java.utils.lists.stringLists;


import cz.vse.java.utils.lists.IRestrictionsList;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**************************************************************
 * <p>Class of {@code StringWhiteList} is used for containing list
 * of all acceptable values.</p>
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
 * @version 06. 02. 2020
 *
 * @see IRestrictionsList
 * @see IStringRestrictionsList
 * @see StringBlackList
 * @see List
 * @see CopyOnWriteArrayList
 */
public class StringWhiteList implements IStringRestrictionsList {


    /* *******************************************************/
    /* Instance variables ************************************/

    /** Acceptable values container */
    private CopyOnWriteArrayList<String> acceptedValues;

    /* *******************************************************/
    /* Static variables **************************************/

    /** Logger */
    private static final Logger LOG = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    /* *******************************************************/
    /* Constructors ******************************************/


    /**
     * <p>The parametric constructor used for defining the {@link ArrayList}
     * of acceptable values.</p>
     *
     * @param list  List of acceptable values.
     */
    public StringWhiteList(List<String> list) {

        this.acceptedValues = new CopyOnWriteArrayList<>();

        for (String val : list) {

            if(!acceptedValues.contains(val)){

                acceptedValues.add(val);
            }
        }
    }

    /**
     * <p>The non-parametric constructor used for defining the {@link CopyOnWriteArrayList}
     * of acceptable values.</p>
     */
    public StringWhiteList(){

        this.acceptedValues = new CopyOnWriteArrayList<>();
    }

    /* *******************************************************/
    /* Instance methods **************************************/


    /**
     * Method used for adding a value to the {@link CopyOnWriteArrayList} of
     * acceptable values.
     *
     * @param value value to be added
     */
    @Override
    public void add(String value){

        synchronized (this) {
            if (!acceptedValues.contains(value)) {

                LOG.log(Level.INFO, "Value added.");
                this.acceptedValues.add(value);
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
            LOG.log(Level.INFO, "All given values added");
        }
    }


    /**
     * Method used for removing a value from the {@link CopyOnWriteArrayList} of
     * acceptable values.
     *
     * @param value value to be removed
     */
    @Override
    public void remove(String value){

        synchronized (this) {
            this.acceptedValues.remove(value);
        }
    }


    /**
     * Method used for removing a value from the {@link CopyOnWriteArrayList} of
     * acceptable values.
     *
     * @param index index of the value to be removed
     */
    @Override
    public void remove(int index){

        synchronized (this) {
            this.acceptedValues.remove(index);
        }
    }


    /**
     * Method used for removing all values from the {@link CopyOnWriteArrayList} of
     * acceptable values.
     */
    @Override
    public void removeAll(){

        synchronized (this) {
            this.acceptedValues = new CopyOnWriteArrayList<>();
        }
    }


    /**
     * For getting the number of the values in the list
     *
     * @return integer number of the size of the list
     */
    @Override
    public int size(){

        return this.acceptedValues.size();
    }


    /**
     * <p>Checks if the String formed value is accepted
     * by the list of restrictions</p>
     *
     * @param value checked value
     *
     * @return      the boolean result
     */
    @Override
    public boolean check(String value) {

        return acceptedValues.contains(value);
    }

    /**
     * <p>Checks all the given String values if they are
     * accepted by the list of restrictions</p>
     *
     * @param values    the values to be checked
     *
     * @return          boolean answer if all are ok
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
        return result;
    }


    /**
     * Overriden method of toString() from the class of {@link Object}
     *
     * @return the String formed instance of this class.
     */
    @Override
    public String toString() {
        return "StringWhiteList{" +
                "acceptedValues=" + acceptedValues +
                '}';
    }


    /* *******************************************************/
    /* Getters ***********************************************/

    /**
     * Getter for {@link List} formed {@code acceptedValues}
     * of the instance of {@link StringWhiteList}
     *
     * @return the value of {@code acceptedValues}
     * @see List
     * @see StringWhiteList
     */
    public List<String> getAcceptedValues() {

        return new CopyOnWriteArrayList<>(acceptedValues);
    }
}
