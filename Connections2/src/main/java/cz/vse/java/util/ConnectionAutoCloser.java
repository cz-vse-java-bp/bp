package cz.vse.java.util;


import cz.vse.java.connections.utils.IConnection;
import cz.vse.java.messages.QuitMessage;

import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;


/*********************************************************************
 * <p>The class of {@code ConnectionAutoCloser} is used to abstractly define
 * the type of the instances.</p>
 *
 *
 * <i>Written for project "Connections2".</i>
 * @author Vojtěch Pavlů
 * @version 03. 04. 2020
 *
 *
 * @see cz.vse.java.utils.userData
 */
public class ConnectionAutoCloser {


    /* *****************************************************************/
    /* Instance variables **********************************************/

    private Timer schedule = new Timer();
    private TimerTask closingProcedure;
    private final IConnection connection;
    private boolean politeness;
    private int seconds;

    /* *****************************************************************/
    /* Static variables ************************************************/

    /**
     * <p>Private static instance of the {@link Logger}
     * - the logger of the {@link ConnectionAutoCloser class</p>
     */
    private static final Logger LOG =
            Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    /* *****************************************************************/
    /* Constructors ****************************************************/

    public ConnectionAutoCloser(IConnection connection, int seconds, boolean politeness) {

        this.connection = connection;
        this.seconds = seconds;
        this.politeness = politeness;

        this.closingProcedure = new TimerTask() {

            @Override
            public void run() {

                if(politeness) {

                    connection.send(new QuitMessage());

                } else {

                    connection.close();

                }
            }
        };
    }

    /* *****************************************************************/
    /* Instance methods ************************************************/

    public void reset() {

        LOG.log(Level.FINE, "Starting - connection will be closed in " + seconds + " seconds");
        this.schedule.cancel();
        this.schedule = new Timer();
        this.schedule.schedule(closingProcedure, seconds*1000);
    }

    public void stop() {

        LOG.log(Level.FINE, "Timer stopped. Connection won't be closed this way anymore.");

        try {

            this.schedule.cancel();

        } catch (Exception e) {

            LOG.log(Level.FINE, "Exception while canceling: " + e.getMessage());
        }
    }

    /* *****************************************************************/
    /* Static methods **************************************************/



    /* *****************************************************************/
    /* Getters *********************************************************/



    /* *****************************************************************/
    /* Setters *********************************************************/


}
