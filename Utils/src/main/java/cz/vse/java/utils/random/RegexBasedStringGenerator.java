package cz.vse.java.utils.random;


import java.util.ArrayList;
import java.util.Arrays;

/*********************************************************************
 * <p>The class of {@code RegexBasedStringGenerator} is used to abstractly define
 * the type of the instances.</p>
 *
 *
 * <i>Written for project "Utils".</i>
 * @author Vojtěch Pavlů
 * @version 13. 02. 2020
 */
public class RegexBasedStringGenerator {


    /* *****************************************************************/
    /* Instance variables **********************************************/

    private ArrayList<Charsets> charsets;

    /* *****************************************************************/
    /* Constructors ****************************************************/

    /**
     * Parametric constructor setting the {@code charsets} field
     *
     * @param charsets  the {@link ArrayList} of {@link Charsets}.
     */
    public RegexBasedStringGenerator(ArrayList<Charsets> charsets) {

        this.charsets = charsets;
    }


    /**
     * Parametric constructor setting the {@code charsets} field
     *
     * @param charsets  unspecified "list" of {@link Charsets}.
     */
    public RegexBasedStringGenerator(Charsets... charsets){


        this.charsets = new ArrayList<>();
        this.charsets.addAll(Arrays.asList(charsets));
    }

    /* *****************************************************************/
    /* Instance methods ************************************************/

    /**
     * Overriden method of <strong>toString</strong>.
     *
     * @return String interpretation of the RegexBasedStringGenerator instance.
     */
    @Override
    public String toString() {
        return "RegexBasedStringGenerator{" +
                "charsets=" + charsets +
                '}';
    }

    /**
     * Method for generating random password by the <i>pseudo</i>-regex,
     * ie the list of charsets in given order
     *
     * @return  String formed random String corresponding the given
     *          schema
     */
    public String generate(){

        if(charsets.size() > 0){

            StringBuilder result = new StringBuilder();
            for (Charsets cs : charsets) {

                int random = RandomNumberGenerator.getRandomNumberInRange(0, cs.getCharset().length-1);
                result.append(cs.getCharset()[random]);
            }

            return result.toString();
        } else {

            throw new IllegalArgumentException("The length of the String is to short!");
        }
    }

    public String generateRegex() {

        StringBuilder regex = new StringBuilder();

        for (Charsets c : charsets) {

            regex.append("(").append(c.getRegex()).append(")");
        }

        return regex.toString();
    }


    /* *****************************************************************/
    /* Getters *********************************************************/

    /**
     * Getter for {@link ArrayList} formed {@code charsets}
     * of the instance of {@link RegexBasedStringGenerator}
     *
     * @return the value of {@code charsets}
     *
     * @see ArrayList
     * @see RegexBasedStringGenerator
     */
    public ArrayList<Charsets> getCharsets() {

        return charsets;
    }
}
