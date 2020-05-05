package cz.vse.java.services.references;


import cz.vse.java.util.Token;

import java.util.logging.Level;
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
 * @see cz.vse.java.services.references
 */
public class ServiceReferenceContainer {


    /* *****************************************************************/
    /* Instance variables **********************************************/

    private ServiceReference serviceReference;
    private Token token;

    private boolean readyToCreate;
    private boolean wasOK = false;
    private boolean used = false;

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

    public ServiceReferenceContainer(ServiceReference serviceReference) {

        this.serviceReference = serviceReference;
        this.readyToCreate = false;
        this.token = new Token("");
    }

    /* *****************************************************************/
    /* Instance methods ************************************************/

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
        return "ServiceReferenceContainer{" +
                "serviceReference=" + serviceReference +
                ", isTokenSet=" + (token != null) +
                ", readyToCreate=" + readyToCreate +
                ", wasOK=" + wasOK +
                ", used=" + used +
                '}';
    }

    /* *****************************************************************/
    /* Static methods **************************************************/



    /* *****************************************************************/
    /* Getters *********************************************************/

    /**
     * Getter for {@link ServiceReference} formed {@code serviceReference}
     * of the instance of {@link ServiceReferenceContainer}
     *
     * @return the value of {@code serviceReference}
     * @see ServiceReference
     * @see ServiceReferenceContainer
     */
    public ServiceReference getServiceReference() {

        return serviceReference;
    }

    /**
     * Getter for {@link Token} formed {@code token}
     * of the instance of {@link ServiceReferenceContainer}
     *
     * @return the value of {@code token}
     * @see Token
     * @see ServiceReferenceContainer
     */
    public Token getToken() {

        return token;
    }

    /**
     * Getter for {@link boolean} formed {@code used}
     * of the instance of {@link ServiceReferenceContainer}
     *
     * @return the value of {@code used}
     * @see boolean
     * @see ServiceReferenceContainer
     */
    public boolean wasUsed() {

        return used;
    }

    /**
     * Getter for boolean formed {@code readyToCreate}
     * of the instance of {@link ServiceReferenceContainer}
     *
     * @return the value of {@code readyToCreate}
     * @see boolean
     * @see ServiceReferenceContainer
     */
    public boolean isReadyToCreate() {

        return readyToCreate;
    }

    /**
     * Getter for {@link boolean} formed {@code wasOK}
     * of the instance of {@link ServiceReferenceContainer}
     *
     * @return the value of {@code wasOK}
     * @see boolean
     * @see ServiceReferenceContainer
     */
    public boolean isWasOK() {

        return wasOK;
    }

    /* *****************************************************************/
    /* Setters *********************************************************/


    /**
     * <p>Setter for the {@code Token} formed
     * {@code token} variable.</p>
     *
     * @param token given Token value to
     *              be set to the variable
     * @see Token
     * @see ServiceReferenceContainer
     */
    public void setToken(Token token) {

        this.token = token;

        if(this.token != null) {

            if(serviceReference != null) {

                this.readyToCreate = true;

            } else {

                LOG.log(Level.SEVERE, "Service reference is not ready yet!");
            }

        } else {

            LOG.log(Level.SEVERE, "Given token is not valid - it's null!");
        }
    }

    /**
     * <p>Setter for the boolean-formed
     * {@code wasOK} variable.</p>
     *
     * @param wasOK given boolean value to
     *              be set to the variable
     * @see boolean
     * @see ServiceReferenceContainer
     */
    public void setWasOK(boolean wasOK) {

        this.wasOK = wasOK;
    }


    /**
     * <p>Setter for the boolean-formed
     * {@code used} variable.</p>
     *
     * @param used given boolean value to
     *             be set to the variable
     * @see boolean
     * @see ServiceReferenceContainer
     */
    public void setUsed(boolean used) {

        this.used = used;
    }
}
