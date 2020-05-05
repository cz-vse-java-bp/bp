package cz.vse.java.utils.random;


/**************************************************************
 * <p>Enumeration type of {@link Charsets} is used for containing
 * all the possible basic character sets.</p>
 *
 * <p>It's divided to main sets:</p>
 * <ul>
 *     <li>Capitals (Uppercase letters)</li>
 *     <li>Lowercase letters</li>
 *     <li>Numbers</li>
 *     <li>Special charactes</li>
 *     <li>Spaces</li>
 *     <li><i>Combination of these</i></li>
 * </ul>
 *
 *
 * Written for project "Utils".
 * @author Vojtěch Pavlů
 * @version 10. 02. 2020
 */
public enum Charsets {

    CAPITALS            ("ABCDEFGHIJKLMNOPQRSTUVWXYZ", "[A-Z]"),
    LOWER_CASES         ("abcdefghijklmnopqrstuvwxyz", "[a-z]"),
    NUMBERS             ("0123456789", "[0-9]"),
    SPECIIAL            ("!?:._\"\\/)('%#&@$=-+*~}{;^<>€", "[!?:._\"\\/)('%#&@$=-+*~}{;^<>€]"),
    SPACES              ("\n\t", "\\s"),
    DOT                 (".", "[.]"), //([])
    LETTERS             (LOWER_CASES.getStringCharset() + CAPITALS.getStringCharset(), "([a-z])|([A-Z])"),
    LETT_NUM            (LETTERS.getStringCharset() + NUMBERS.getStringCharset(), "([a-Z])|([0-9])"),
    LETT_SPEC           (LETTERS.getStringCharset() + SPECIIAL.getStringCharset(), "([a-Z0-9])|([!?:._\"\\/)('%#&@$=-+*~}{;^<>€])"),
    NUM_SPEC            (NUMBERS.getStringCharset() + SPECIIAL.getStringCharset(), "([0-9])|([!?:._\"\\/)('%#&@$=-+*~}{;^<>€])"),
    LETT_SPACES         (LETTERS.getStringCharset() + SPACES.getStringCharset(), "([a-Z)|([!?:._\"\\/)('%#&@$=-+*~}{;^<>€])"),
    LETT_SPACES_SPEC    (LETT_SPACES.getStringCharset() + SPECIIAL.getStringCharset(),"([a-Z])|(\\s)|([!?:._\"\\/)('%#&@$=-+*~}{;^<>€])"),
    LETT_SPACES_NUM     (LETT_SPACES.getStringCharset() + NUMBERS.getStringCharset(), "([a-Z])|(\\s)|([0-9])"),
    CAP_NUM             (CAPITALS.getStringCharset() + NUMBERS.getStringCharset(), "([A-Z])|([0-9])"),
    LETT_SPEC_NUM       (LETTERS.getStringCharset() + NUM_SPEC.getStringCharset(), "([a-Z])|([0-9])|([!?:._\"\\/)('%#&@$=-+*~}{;^<>€])"),
    LOWERC_NUM          (LOWER_CASES.getStringCharset() + NUMBERS.getStringCharset(), "([a-z])|([0-9])"),
    LOWERC_NUM_SPEC     (LOWERC_NUM.getStringCharset() + SPECIIAL.getStringCharset(), "([a-z])|([0-9]|([!?:._\"\\/)('%#&@$=-+*~}{;^<>€])"),
    LC_NUM_SPEC_SPACE   (LOWERC_NUM_SPEC.getStringCharset() + SPACES.getStringCharset(), "(" + LOWERC_NUM_SPEC.getRegex() + ")|(\\s)"),
    ALL                 (LETT_SPACES_NUM.getStringCharset() + SPACES.getStringCharset(), "(" + LETT_SPACES_NUM.getRegex() + ")|(\\s)");


    private char[] charset;
    private String regex;

    /**
     * Basic constructor
     *
     * @param charset   charset to be used. The input
     *                  is String-formed
     */
    Charsets(String charset, String regex){

        this.charset = charset.toCharArray();
        this.regex = regex;
    }


    /**
     * Method for returning the charset
     *
     * @return char[]-formed set of chars.
     */
    public char[] getCharset() {

        return charset;
    }

    /**
     * Method for returning the charset in Stringified form
     *
     * @return  {@link String}-formed charset
     */
    public String getStringCharset(){

        StringBuilder result = new StringBuilder();

        for (char c : charset) {

            result.append(c);
        }

        return result.toString();
    }

    public String getRegex(){

        return regex;
    }
}
