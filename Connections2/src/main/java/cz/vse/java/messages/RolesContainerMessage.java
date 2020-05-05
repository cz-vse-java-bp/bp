package cz.vse.java.messages;


import cz.vse.java.messages.utils.AMessage;
import cz.vse.java.messages.utils.IDataContainer;
import cz.vse.java.util.userData.ERole;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Logger;


/*********************************************************************
 * <p>The class of {@code RolesContainerMessage} is used to abstractly define
 * the type of the instances.</p>
 *
 *
 * <i>Written for project "Connections2".</i>
 * @author Vojtěch Pavlů
 * @version 15. 04. 2020
 *
 *
 * @see cz.vse.java.handlers
 */
public class RolesContainerMessage extends AMessage implements IDataContainer {


    /* *****************************************************************/
    /* Instance variables **********************************************/

    private ArrayList<ERole> roles;

    /* *****************************************************************/
    /* Static variables ************************************************/

    /**
     * <p>Private static instance of the {@link Logger}
     * - the logger of the {@link RolesContainerMessage class</p>
     */
    private static final Logger LOG =
            Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    /* *****************************************************************/
    /* Constructors ****************************************************/

    public RolesContainerMessage(ArrayList<ERole> roles) {

        this.roles = roles;
    }

    public RolesContainerMessage(ERole... roles) {

        this.roles = new ArrayList<>(Arrays.asList(roles));
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
    public ArrayList<ERole> getContent() {

        return this.roles;
    }

    /* *****************************************************************/
    /* Setters *********************************************************/



}
