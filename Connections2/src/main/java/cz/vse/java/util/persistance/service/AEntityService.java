package cz.vse.java.util.persistance.service;


import cz.vse.java.util.database.DBConnection;
import cz.vse.java.util.database.DatabaseConnectionContainer;
import cz.vse.java.util.database.EDBUse;

import java.util.logging.Logger;


/*********************************************************************
 * <p>The class of {@code AEntityService} is used to abstractly define
 * the type of the instances.</p>
 *
 *
 * <i>Written for project "Connections2".</i>
 * @author Vojtěch Pavlů
 * @version 07. 04. 2020
 *
 *
 * @see cz.vse.java.utils.persistance.service
 */
public abstract class AEntityService {


    /* *****************************************************************/
    /* Instance variables **********************************************/

    private DBConnection connection;

    /* *****************************************************************/
    /* Static variables ************************************************/

    /**
     * <p>Private static instance of the {@link Logger}
     * - the logger of the {@link AEntityService class</p>
     */
    private static final Logger LOG =
            Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    /* *****************************************************************/
    /* Constructors ****************************************************/

    public AEntityService(EDBUse use) {

        this.connection = DatabaseConnectionContainer.getInstance().get(use);
    }

    /* *****************************************************************/
    /* Instance methods ************************************************/



    /* *****************************************************************/
    /* Static methods **************************************************/



    /* *****************************************************************/
    /* Getters *********************************************************/

    /**
     * Getter for {@link DBConnection} formed {@code connection}
     * of the instance of {@link AEntityService}
     *
     * @return the value of {@code connection}
     * @see DBConnection
     * @see AEntityService
     */
    public DBConnection getConnection() {

        return connection;
    }

    /* *****************************************************************/
    /* Setters *********************************************************/



}
