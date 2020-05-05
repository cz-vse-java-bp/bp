package cz.vse.java.messages;


import cz.vse.java.messages.utils.AMessage;
import cz.vse.java.messages.utils.IDataContainer;
import cz.vse.java.services.references.ServiceReference;

import java.util.logging.Logger;


/*********************************************************************
 * <p>The class of {@code ServiceReferenceContainer} is used to abstractly define
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
public class ServiceReferenceContainer extends AMessage implements IDataContainer {


    /* *****************************************************************/
    /* Instance variables **********************************************/

    private ServiceReference sr;

    /* *****************************************************************/
    /* Static variables ************************************************/

    /**
     * <p>Private static instance of the {@link Logger}
     * - the logger of the {@link ServiceReferenceContainer class</p>
     */
    private static final Logger LOG =
            Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    /* *****************************************************************/
    /* Constructors ****************************************************/

    public ServiceReferenceContainer(ServiceReference sr) {

        this.sr = sr;
    }

    /* *****************************************************************/
    /* Instance methods ************************************************/

    /**
     * <p>Returns the content of the message.</p>
     *
     * @return the content the message contains.
     */
    @Override
    public ServiceReference getContent() {

        return this.sr;
    }

    /* *****************************************************************/
    /* Static methods **************************************************/



    /* *****************************************************************/
    /* Getters *********************************************************/



    /* *****************************************************************/
    /* Setters *********************************************************/



}
