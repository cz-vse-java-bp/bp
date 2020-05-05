package cz.vse.java.messages.utils.past;


import cz.vse.java.messages.utils.IMessage;

import java.time.LocalDateTime;
import java.util.logging.Logger;


/*********************************************************************
 * <p>The class of {@code ReceivedMessageContainer} is used to abstractly define
 * the type of the instances.</p>
 *
 *
 * <i>Written for project "Connections2".</i>
 * @author Vojtěch Pavlů
 * @version 15. 04. 2020
 *
 *
 * @see cz.vse.java.messages.utils.past
 */
public class ReceivedMessageContainer {


    /* *****************************************************************/
    /* Instance variables **********************************************/

    private IMessage message;
    private LocalDateTime timeOfReceiving;

    /* *****************************************************************/
    /* Static variables ************************************************/

    /**
     * <p>Private static instance of the {@link Logger}
     * - the logger of the {@link ReceivedMessageContainer class</p>
     */
    private static final Logger LOG =
            Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    /* *****************************************************************/
    /* Constructors ****************************************************/

    public ReceivedMessageContainer(IMessage message) {

        this.message = message;
        this.timeOfReceiving = LocalDateTime.now();
    }

    /* *****************************************************************/
    /* Instance methods ************************************************/



    /* *****************************************************************/
    /* Static methods **************************************************/



    /* *****************************************************************/
    /* Getters *********************************************************/

    /**
     * Getter for {@link IMessage} formed {@code message}
     * of the instance of {@link ReceivedMessageContainer}
     *
     * @return the value of {@code message}
     * @see IMessage
     * @see ReceivedMessageContainer
     */
    public IMessage getMessage() {

        return message;
    }

    /**
     * Getter for {@link LocalDateTime} formed {@code timeOfReceiving}
     * of the instance of {@link ReceivedMessageContainer}
     *
     * @return the value of {@code timeOfReceiving}
     * @see LocalDateTime
     * @see ReceivedMessageContainer
     */
    public LocalDateTime getTimeOfReceiving() {

        return timeOfReceiving;
    }

    /* *****************************************************************/
    /* Setters *********************************************************/


}
