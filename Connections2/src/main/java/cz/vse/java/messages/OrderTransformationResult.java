package cz.vse.java.messages;


import cz.vse.java.messages.utils.AMessage;
import cz.vse.java.messages.utils.IDataContainer;

import java.util.logging.Logger;


/*********************************************************************
 * <p>The class of {@code OrderTransformationResult} is used to abstractly define
 * the type of the instances.</p>
 *
 *
 * <i>Written for project "Connections2".</i>
 * @author Vojtěch Pavlů
 * @version 16. 04. 2020
 *
 *
 * @see cz.vse.java.messages
 */
public class OrderTransformationResult extends AMessage implements IDataContainer {


    /* *****************************************************************/
    /* Instance variables **********************************************/

    private Boolean result;

    /* *****************************************************************/
    /* Static variables ************************************************/

    /**
     * <p>Private static instance of the {@link Logger}
     * - the logger of the {@link OrderTransformationResult class</p>
     */
    private static final Logger LOG =
            Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    /* *****************************************************************/
    /* Constructors ****************************************************/

    public OrderTransformationResult(boolean result) {

        this.result = result;
    }

    /* *****************************************************************/
    /* Instance methods ************************************************/



    /* *****************************************************************/
    /* Static methods **************************************************/



    /* *****************************************************************/
    /* Getters *********************************************************/

    /**
     * <p>Returns the content of the message.</p>
     *
     * @return the content the message contains.
     */
    @Override
    public Boolean getContent() {

        return this.result;
    }

    /* *****************************************************************/
    /* Setters *********************************************************/



}
