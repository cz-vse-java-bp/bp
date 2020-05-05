package cz.vse.java.messages.utils.future;


import cz.vse.java.connections.utils.IConnection;
import cz.vse.java.messages.ServiceReferenceRequest;
import cz.vse.java.messages.TextMessage;
import cz.vse.java.messages.utils.IMessage;
import cz.vse.java.services.clientSide.Client;
import cz.vse.java.services.serverSide.EServiceType;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.Level;
import java.util.logging.Logger;


/*********************************************************************
 * <p>The class of {@code MessageTask} is used to abstractly define
 * the type of the instances.</p>
 *
 * <p>This class is sealing the {@link IMessage} implementing instance
 * in an object containing more useful information about it's execution.</p>
 *
 * <p>These message tasks have specified time of expiration (for preventing
 * the not executable message tasks), the receiver of the message and the
 * message itself.</p>
 *
 * <i>Written for project "Connections2".</i>
 * @author Vojtěch Pavlů
 * @version 05. 04. 2020
 *
 *
 * @see cz.vse.java.messages.utils
 * @see IMessage
 * @see MessageTaskContainer
 */
public class MessageTask {


    /* *****************************************************************/
    /* Instance variables **********************************************/

    /** Message to be sent in future */
    private IMessage message;

    /** The receiver of the message */
    private EServiceType service;

    /** Date and time of expiration */
    private LocalDateTime expiration;

    /* *****************************************************************/
    /* Static variables ************************************************/

    /**
     * <p>Private static instance of the {@link Logger}
     * - the logger of the {@link MessageTask class</p>
     */
    private static final Logger LOG =
            Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    /**
     * <p>Default expiration time - value was set to 60 by default.
     * It means the message task has 60 seconds to be executed,
     * after that it's expired, removed and won't be executed anymore.</p>
     *
     * <p>This prevents storing non-executables.</p>
     */
    private static Long EXPIRATION = 60L;

    /* *****************************************************************/
    /* Constructors ****************************************************/


    /**
     * <p>Constructor defining the message, the receiver and
     * the expiration is got from default static field of
     * {@code EXPIRATION} (60 seconds in the future by default).</p>
     *
     * @param message   to be sent
     * @param service   the receiver of the message
     */
    public MessageTask(IMessage message, EServiceType service) {

        this.message = message;
        this.service = service;
        this.expiration = LocalDateTime.now().plusSeconds(EXPIRATION);
    }


    /**
     * <p>Constructor defining the message, the receiver and
     * the expiration by custom {@code long} value.</p>
     *
     * <p>The parameter of {@code seconds} has to be 1 at least, otherwise
     * it returns {@link IllegalArgumentException}.</p>
     *
     * @param message   to be sent
     * @param service   the receiver of the message
     * @param seconds   time to stay not-expired. After this period of time,
     *                  it get's expired won't be executed. At the same time,
     *                  it stays waiting for it's execution, so it's not a good
     *                  idea to set it to high values
     */
    public MessageTask(IMessage message, EServiceType service, long seconds) {

        if(seconds < 1) {

            throw new IllegalArgumentException("The waiting before expiration should " +
                    "be 1 at least. Given value: " + seconds);
        }

        this.message = message;
        this.service = service;
        this.expiration = LocalDateTime.now().plusSeconds(seconds);
    }


    /**
     * <p>Constructor of {@link MessageTask} defining the {@link IMessage},
     * the {@link EServiceType} (the receiver) and the actual time of expiration
     * by {@link LocalDateTime} instance.</p>
     *
     * @param message   to be sent
     * @param service   receiver of the message
     * @param expires   time, when the {@link MessageTask} expires and won't be
     *                  executed anymore
     */
    public MessageTask(IMessage message, EServiceType service, LocalDateTime expires) {

        if(expires.isBefore(LocalDateTime.now())) {

            throw new IllegalArgumentException("Given date has to be in the future!");
        }

        this.message = message;
        this.service = service;
        this.expiration = expires;
    }

    /* *****************************************************************/
    /* Instance methods ************************************************/


    /**
     * <p>Runns the task - if it is not expired, the connection with
     * the receiver is checked for authentication state. When it passes
     * these test and gets executed (the message gets sent), returns true.
     * Otherwise it returns false.</p>
     *
     * @return  true, when gets successfully executed, otherwise it
     *          returns false.
     */
    public boolean runTask() {

        if(expiration.isAfter(LocalDateTime.now())) {

            IConnection connection =
                    Client.getInstance().getConnectionWithService(this.getService());

            if(connection == null) {

                return false;

            } else if(connection.getAmIAuthenticated()) {

                connection.send(this.message);
                return true;

            } else {

                return false;
            }

        } else {

            LOG.log(Level.INFO, "Task expired.");
        }
        return false;
    }

    /**
     * Returns a string representation of the object. In general, the
     * {@code toString} method returns a string that
     * "textually represents" this object. The result should
     * be a concise but informative representation that is easy for a
     * person to read.
     * It is recommended that all subclasses override this method.
     * <p>
     * The {@code toString} method for class {@code Object}
     * returns a string consisting of the name of the class of which the
     * object is an instance, the at-sign character `{@code @}', and
     * the unsigned hexadecimal representation of the hash code of the
     * object. In other words, this method returns a string equal to the
     * value of:
     * <blockquote>
     * <pre>
     * getClass().getName() + '@' + Integer.toHexString(hashCode())
     * </pre></blockquote>
     *
     * @return a string representation of the object.
     */
    @Override
    public String toString() {
        return "MessageTask{" +
                "message=" + message +
                ", service=" + service.name() +
                ", expiration=" + expiration.format(DateTimeFormatter.ISO_TIME) +
                '}';
    }

    /* *****************************************************************/
    /* Static methods **************************************************/



    /* *****************************************************************/
    /* Getters *********************************************************/

    /**
     * Getter for {@link IMessage} formed {@code message}
     * of the instance of {@link MessageTask}
     *
     * @return the value of {@code message}
     * @see IMessage
     * @see MessageTask
     */
    public IMessage getMessage() {

        return message;
    }

    /**
     * Getter for {@link EServiceType} formed {@code service}
     * of the instance of {@link MessageTask}
     *
     * @return the value of {@code service}
     * @see EServiceType
     * @see MessageTask
     */
    public EServiceType getService() {

        return service;
    }

    /**
     * Getter for {@link LocalDateTime} formed {@code expire}
     * of the instance of {@link MessageTask}
     *
     * @return the value of {@code expire}
     * @see LocalDateTime
     * @see MessageTask
     */
    public LocalDateTime getExpiration() {

        return expiration;
    }


    /**
     * <p>Getter for the information about current expiration state.</p>
     *
     * @return  true, when it's already expired. Otherwise it returns false.
     */
    public boolean isExpired() {

        return !(expiration.isAfter(LocalDateTime.now()));
    }

    /* *****************************************************************/
    /* Setters *********************************************************/

    /**
     * <p>Setter for the long-formed {@code EXPIRATION} variable.</p>
     *
     * <p>This does not update any of the task created before.
     * This may affect the new created tasks only.</p>
     *
     * <p>Sets default expiration period of time in seconds.
     * Only values greater than 1 are allowed.</p>
     *
     * <p>It's not recommended to use too great values, because
     * non-executables will remain stored and will be checked
     * everytime it passes through all the {@link MessageTask}s.</p>
     *
     * <p>At the same time, it should not be to little. When the message
     * task is created and the message is prepared for sending, the message
     * task will be classified as expired and won't be executed anymore.</p>
     *
     * <p>The best solution seems to be somewhere in range of 15 and 120,
     * which means the message will stay stored and waiting for execution
     * for 15 seconds up to two minutes. First bound is recommended for
     * fast-running processes using fast devices, the other bound is for
     * slower and lazier usage.</p>
     *
     * @param EXPIRATION given long value to be set to the variable
     * @see Long
     * @see MessageTask
     */
    public static void setEXPIRATION(Long EXPIRATION) {

        if(EXPIRATION < 1) {

            throw new IllegalArgumentException("Not supported value! Has to be 1 at least!");
        }
        MessageTask.EXPIRATION = EXPIRATION;
    }
}
