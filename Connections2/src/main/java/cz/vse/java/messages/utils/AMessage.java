package cz.vse.java.messages.utils;


import cz.vse.java.util.FingerPrint;
import cz.vse.java.util.Token;


/*********************************************************************
 * <p>The class of {@code AMessage} is used to abstractly define
 * the type of the instances.</p>
 *
 * <p>Instances of this class stores basic data about the message.</p>
 *
 * <p>It's used for storing {@link FingerPrint} and {@link Token}.
 * Both are generated itself.</p>
 *
 * <p>Both can be used for authentication.</p>
 *
 * <i>Written for project "Connections2".</i>
 * @author Vojtěch Pavlů
 * @version 14. 03. 2020
 *
 *
 * @see cz.vse.java.messages
 * @see IMessage
 * @see FingerPrint
 * @see Token
 */
public abstract class AMessage implements IMessage {


    /* *****************************************************************/
    /* Instance variables **********************************************/

    /** {@link FingerPrint} containing data about this device */
    private FingerPrint fingerPrint;

    /** {@link Token} containing randomly generated {@link String}
     * used for checking the connection (or session by default) */
    private Token token;

    /* *****************************************************************/
    /* Static variables ************************************************/


    /* *****************************************************************/
    /* Constructors ****************************************************/

    public AMessage(Token token) {

        this.token = token;
        this.fingerPrint = new FingerPrint();
    }

    public AMessage() {

        this.token = new Token("");
        this.fingerPrint = new FingerPrint();
    }

    /* *****************************************************************/
    /* Instance methods ************************************************/



    /* *****************************************************************/
    /* Static methods **************************************************/



    /* *****************************************************************/
    /* Getters *********************************************************/

    /**
     * Getter for {@link FingerPrint} formed {@code fingerPrint}
     * of the instance of {@link AMessage}
     *
     * @return the value of {@code fingerPrint}
     * @see FingerPrint
     * @see AMessage
     */
    public FingerPrint getFingerPrint() {

        return fingerPrint;
    }

    /**
     * Getter for {@link Token} formed {@code token}
     * of the instance of {@link AMessage}
     *
     * @return the value of {@code token}
     * @see Token
     * @see AMessage
     */
    public Token getToken() {

        return token;
    }

    /* *****************************************************************/
    /* Setters *********************************************************/

    /**
     * <p>Setter for the {@code FingerPrint} formed
     * {@code fingerPrint} variable.</p>
     *
     * @param fingerPrint given FingerPrint value to
     *                    be set to the variable
     * @see FingerPrint
     * @see AMessage
     */
    @Override
    public void setFingerPrint(FingerPrint fingerPrint) {

        this.fingerPrint = fingerPrint;
    }

    /**
     * <p>Setter for the {@code Token} formed
     * {@code token} variable.</p>
     *
     * @param token given Token value to
     *              be set to the variable
     * @see Token
     * @see AMessage
     */
    @Override
    public void setToken(Token token) {

        this.token = token;
    }
}
