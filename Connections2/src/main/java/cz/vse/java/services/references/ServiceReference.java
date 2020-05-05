package cz.vse.java.services.references;


import cz.vse.java.services.serverSide.EServiceType;

import java.io.Serializable;


/*********************************************************************
 * <p>The class of {@code ServiceReference} is used to abstractly define
 * the type of the instances.</p>
 *
 *
 * <i>Written for project "Connections2".</i>
 * @author Vojtěch Pavlů
 * @version 02. 04. 2020
 *
 *
 * @see cz.vse.java.services.references
 */
public class ServiceReference implements Serializable {


    /* *****************************************************************/
    /* Instance variables **********************************************/

    private EReferenceFor referenceFor;
    private String ip;
    private int port;
    private EServiceType type;


    /* *****************************************************************/
    /* Constructors ****************************************************/

    public ServiceReference(EReferenceFor referenceFor,
                            String ip,
                            int port,
                            EServiceType type) {

        this.referenceFor = referenceFor;
        this.ip = ip;
        this.port = port;
        this.type = type;
    }

    /* *****************************************************************/
    /* Instance methods ************************************************/

    /**
     * <p>Overriden method of <strong>toString</strong>.</p>
     *
     * @return {@link String} interpretation of the ServiceReference instance.
     */
    @Override
    public String toString() {
        return "ServiceReference{" +
                "referenceFor=" + referenceFor.name() +
                ", ip='" + ip + '\'' +
                ", port=" + port +
                ", type=" + type.name() +
                '}';
    }

    /* *****************************************************************/
    /* Static methods **************************************************/



    /* *****************************************************************/
    /* Getters *********************************************************/

    /**
     * Getter for {@link EReferenceFor} formed {@code referenceFor}
     * of the instance of {@link ServiceReference}
     *
     * @return the value of {@code referenceFor}
     * @see EReferenceFor
     * @see ServiceReference
     */
    public EReferenceFor getReferenceFor() {

        return referenceFor;
    }

    /**
     * Getter for {@link String} formed {@code ip}
     * of the instance of {@link ServiceReference}
     *
     * @return the value of {@code ip}
     * @see String
     * @see ServiceReference
     */
    public String getIp() {

        return ip;
    }

    /**
     * Getter for {@link int} formed {@code port}
     * of the instance of {@link ServiceReference}
     *
     * @return the value of {@code port}
     * @see int
     * @see ServiceReference
     */
    public int getPort() {

        return port;
    }

    /**
     * Getter for {@link EServiceType} formed {@code type}
     * of the instance of {@link ServiceReference}
     *
     * @return the value of {@code type}
     * @see EServiceType
     * @see ServiceReference
     */
    public EServiceType getType() {

        return type;
    }



    /* *****************************************************************/
    /* Setters *********************************************************/

    /**
     * <p>Setter for the {@code EReferenceFor} formed
     * {@code referenceFor} variable.</p>
     *
     * @param referenceFor given EReferenceFor value to
     *                     be set to the variable
     * @see EReferenceFor
     * @see ServiceReference
     */
    public void setReferenceFor(EReferenceFor referenceFor) {

        this.referenceFor = referenceFor;
    }

    /**
     * <p>Setter for the {@code String} formed
     * {@code ip} variable.</p>
     *
     * @param ip given String value to
     *           be set to the variable
     * @see String
     * @see ServiceReference
     */
    public void setIp(String ip) {

        this.ip = ip;
    }

    /**
     * <p>Setter for the {@code $field.typeName} formed
     * {@code port} variable.</p>
     *
     * @param port given $field.typeName value to
     *             be set to the variable
     * @see int
     * @see ServiceReference
     */
    public void setPort(int port) {

        this.port = port;
    }

    /**
     * <p>Setter for the {@code EServiceType} formed
     * {@code type} variable.</p>
     *
     * @param type given EServiceType value to
     *             be set to the variable
     * @see EServiceType
     * @see ServiceReference
     */
    public void setType(EServiceType type) {

        this.type = type;
    }


}
