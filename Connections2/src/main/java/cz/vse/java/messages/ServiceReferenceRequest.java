package cz.vse.java.messages;


import cz.vse.java.messages.utils.AMessage;
import cz.vse.java.messages.utils.IDataContainer;
import cz.vse.java.services.serverSide.EServiceType;

import java.util.logging.Logger;


/*********************************************************************
 * <p>The class of {@code ServiceReferenceRequest} is used to abstractly define
 * the type of the instances.</p>
 *
 *
 * <i>Written for project "Connections2".</i>
 * @author Vojtěch Pavlů
 * @version 05. 04. 2020
 *
 *
 * @see cz.vse.java.messages
 */
public class ServiceReferenceRequest extends AMessage implements IDataContainer {


    /* *****************************************************************/
    /* Instance variables **********************************************/

    private EServiceType type;

    /* *****************************************************************/
    /* Static variables ************************************************/

    /**
     * <p>Private static instance of the {@link Logger}
     * - the logger of the {@link ServiceReferenceRequest class</p>
     */
    private static final Logger LOG =
            Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    /* *****************************************************************/
    /* Constructors ****************************************************/

    public ServiceReferenceRequest(EServiceType type) {

        this.type = type;
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
    public EServiceType getContent() {

        return this.type;
    }

    /* *****************************************************************/
    /* Setters *********************************************************/


}
