package cz.vse.java.utils.random;


import java.util.Random;

/**************************************************************
 * <p>Class of {@link RandomNumberGenerator} is used for
 * generating basic <i>pseudo-</i>random numbers for various
 * utility.</p>
 *
 * <p>The methods are public and static because of the purpose
 * of the "instances". Creating these is worthless, because it's
 * usually used once only.</p>
 *
 * <p>The constructor of the class is private and non-parametric
 * to prevent the creation of instances of the class.</p>
 *
 *
 * Written for project "Utils".
 * @author Vojtěch Pavlů
 * @version 08. 02. 2020
 *
 * @see Random
 */
public class RandomNumberGenerator {

    /* *******************************************************/
    /* Constructors ******************************************/

    /**
     * Private non-parametric constructor, which should be never
     * used, because it does nothing but preventing using instances
     * of this class.
     */
    private RandomNumberGenerator(){}

    /* *******************************************************/
    /* Static methods ****************************************/


    /**
     * The method for generating <i>pseudo-</i>random integer number
     * in a specified range.
     *
     * <b>Everytime should be maximum higher value than the minimum</b>,
     * otherwise it throws an {@link IllegalArgumentException} instance.
     *
     * @param min   the lower bound - minimum.
     * @param max   the upper bound - maximum.
     *
     * @return      random integer number in the specified range.
     *
     * @throws IllegalArgumentException when the minimum (lower bound) is
     *                                  not a smaller number than maximum.
     *                                  It's thrown when the minimum is
     *                                  equal or larger than the maximum.
     */
    public static int getRandomNumberInRange(int min, int max) throws IllegalArgumentException {

        if (min > max) {
            throw new IllegalArgumentException("The upper bound (maximum) " +
                    "must be greater than the lower one (minimum)! Max: " + max + ", Min: " + min);
        }

        Random r = new Random();
        return r.nextInt((max - min) + 1) + min;
    }
}
