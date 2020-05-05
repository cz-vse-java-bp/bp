package cz.vse.java.utils.random;


/**************************************************************
 * <p>The class for generating random {@link String}s
 * from given charset. The default is generating from
 * all non-space characters.</p>
 *
 *
 * Written for project "Utils".
 * @author Vojtěch Pavlů
 * @version 10. 02. 2020
 *
 * @see RandomEmailAddressGenerator
 * @see RandomNumberGenerator
 * @see String
 * @see StringBuilder
 */
public class RandomStringGenerator {


    /* *******************************************************/
    /* Instance variables ************************************/

    /** Charset used to build the strings */
    private char[] charset;

    /* *******************************************************/
    /* Static variables **************************************/

    /** Default static charset containing all capitals, lowercases,
     * numbers and special characters. */
    private static final char[] DEFAULT_CHARSET =
            ("abcdefghijklmnopqrstuvwxyz" +
             "ABCDEFGHIJKLMNOPQRSTUVWXYZ" +
             "0123456789" +
             "!?:._\"\\/)('%#&@$=-+*~ }{;[]^<>€").toCharArray();

    /* *******************************************************/
    /* Constructors ******************************************/


    /**
     * Constructor of the generator
     *
     * @param charset charset to be used for generating
     *                the Strings. When it's empty,
     *                throws exception.
     *
     *
     * @throws IllegalArgumentException when the charset
     *                                  does not have enough
     *                                  chars inside
     */
    public RandomStringGenerator(char[] charset) throws IllegalArgumentException {

        if(charset.length < 1){

            throw new IllegalArgumentException("The charset is not long enough! There should be 1 character at least!");
        }
        else {

            this.charset = charset;
        }
    }


    /**
     * Constructor defining the charset predefined in
     * {@link Charsets} enumeration.
     *
     * @param charset   instance of {@link Charsets} enumeration
     *                  object class.
     */
    public RandomStringGenerator(Charsets charset){

        this.charset = charset.getCharset();
    }


    /**
     * Constructor defining the charsets predefined in
     * {@link Charsets} enumeration.
     *
     * @param charsets  instances of {@link Charsets} enumeration
     *                  object class.
     */
    public RandomStringGenerator(Charsets... charsets){

        int length = 0;
        int pointer = 0;

        for (Charsets charset : charsets) {

            length = length + charset.getCharset().length;
        }

        char[] chars = new char[length];

        for (Charsets charset : charsets) {

            for (int i = 0; i<charset.getCharset().length; i++){
                chars[pointer] = charset.getCharset()[i];
                pointer++;
            }
        }

        this.charset = chars;
    }


    /**
     * Constructor using private static final field
     * of {@code DEFAULT_CHARSET}.
     */
    public RandomStringGenerator() {

        this.charset = DEFAULT_CHARSET;
    }

    /* *******************************************************/
    /* Instance methods **************************************/


    /* *******************************************************/
    /* Static methods ****************************************/


    /**
     * Generates random String with given length.
     *
     * @param length    Length of the String
     *
     * @return          Random String made of characters
     *                  given in the charset.
     *
     * @throws IllegalArgumentException When the length parameter is
     *                                  less than 0 or equals to 0.
     */
    public String generateRandomString(int length) throws IllegalArgumentException {

        if(length > 0) {

            StringBuilder result = new StringBuilder();

            for (int i = 0; i < length; i++) {

                result.append(charset[
                        RandomNumberGenerator
                        .getRandomNumberInRange(0, charset.length - 1)
                        ]);
            }

            return result.toString();
        }
        else {
            throw new IllegalArgumentException("The length cannot be 0 or lower!");
        }
    }


    /**
     * Generates String with variable length in the given range.
     *
     * @param minLength     Minimal length the result should have
     * @param maxLength     Maximal length the result should have
     *
     * @return              String with length in the range
     */
    public String generateRandomString(int minLength, int maxLength){

        if(minLength > maxLength){

            throw new IllegalArgumentException("The min length shoud not be larger than max one!" +
                    "Given interval: " + minLength + " - " + maxLength);
        }
        if(minLength < 0){

            throw new IllegalArgumentException("None of the parameters can be negative! " +
                    "Given interval: " + minLength + " - " + maxLength);
        }

        return generateRandomString(

                RandomNumberGenerator.getRandomNumberInRange(minLength, maxLength)
        );
    }
}
