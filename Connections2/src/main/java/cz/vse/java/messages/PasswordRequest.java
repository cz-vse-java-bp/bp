package cz.vse.java.messages;


import cz.vse.java.messages.utils.AMessage;
import cz.vse.java.messages.utils.IRequest;

import java.util.logging.Logger;


/*********************************************************************
 * <p>The class of {@code PasswordRequest} is used to abstractly define
 * the type of the instances.</p>
 *
 *
 * <i>Written for project "Connections2".</i>
 * @author Vojtěch Pavlů
 * @version 03. 04. 2020
 *
 *
 * @see cz.vse.java.messages
 */
public class PasswordRequest extends AMessage implements IRequest {


    /* *****************************************************************/
    /* Instance variables **********************************************/

    private String salt;
    private int numOfHashes;

    /* *****************************************************************/
    /* Static variables ************************************************/

    /**
     * <p>Private static instance of the {@link Logger}
     * - the logger of the {@link PasswordRequest class</p>
     */
    private static final Logger LOG =
            Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    /* *****************************************************************/
    /* Constructors ****************************************************/

    public PasswordRequest(String salt, int numOfHashes) {

        this.salt = salt;
        this.numOfHashes = numOfHashes;
    }

    /* *****************************************************************/
    /* Instance methods ************************************************/

    /**
     * <p>Overriden method of <strong>toString</strong>.</p>
     *
     * @return {@link String} interpretation of the PasswordRequest instance.
     */
    @Override
    public String toString() {

        return "PasswordRequest.toString() - NOT DEFINED YET!";
    }

    /* *****************************************************************/
    /* Static methods **************************************************/



    /* *****************************************************************/
    /* Getters *********************************************************/

    /**
     * Getter for {@link String} formed {@code salt}
     * of the instance of {@link PasswordRequest}
     *
     * @return the value of {@code salt}
     * @see String
     * @see PasswordRequest
     */
    public String getSalt() {
        return salt;
    }

    /**
     * Getter for {@link int} formed {@code numOfHashes}
     * of the instance of {@link PasswordRequest}
     *
     * @return the value of {@code numOfHashes}
     * @see int
     * @see PasswordRequest
     */
    public int getNumOfHashes() {
        return numOfHashes;
    }



    /* *****************************************************************/
    /* Setters *********************************************************/



}
