package cz.vse.java.messages;


import cz.vse.java.messages.utils.AMessage;
import cz.vse.java.messages.utils.IDataContainer;
import cz.vse.java.util.persistance.entities.orders.PreOrder;

import java.util.logging.Logger;


/*********************************************************************
 * <p>The class of {@code SetSubmitterToOrder} is used to abstractly define
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
public class SetSubmitterToOrder extends AMessage implements IDataContainer {


    /* *****************************************************************/
    /* Instance variables **********************************************/

    private String submitter;
    private String ident;

    /* *****************************************************************/
    /* Static variables ************************************************/

    /**
     * <p>Private static instance of the {@link Logger}
     * - the logger of the {@link SetSubmitterToOrder class</p>
     */
    private static final Logger LOG =
            Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    /* *****************************************************************/
    /* Constructors ****************************************************/

    public SetSubmitterToOrder(String submitter, String ident) {

        this.submitter = submitter;
        this.ident = ident;
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
     * @return the content the message contains. First index has
     * reference to submitter, the other one at the identificator to
     * the {@link PreOrder}
     */
    @Override
    public String[] getContent() {

        return new String[]{this.submitter, this.ident};
    }

    /* *****************************************************************/
    /* Setters *********************************************************/


}
