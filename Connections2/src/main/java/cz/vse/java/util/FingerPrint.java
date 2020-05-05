package cz.vse.java.util;


import cz.vse.java.utils.device.LocalDeviceProp;

import java.io.Serializable;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

/*********************************************************************
 * <p>The class of {@code FingerPrint} is used to abstractly define
 * the type of the instances.</p>
 *
 *
 * <i>Written for project "Connections".</i>
 * @author Vojtěch Pavlů
 * @version 21. 02. 2020
 *
 *
 * @see cz.vse.java
 */
public class FingerPrint implements Serializable {


    /* *****************************************************************/
    /* Instance variables **********************************************/

    private String ip;
    private String mac;
    private String deviceName;

    /* *****************************************************************/
    /* Static variables ************************************************/

    private static final Logger LOG = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    private static final Long serilVersionUID = 3451345L;

    /* *****************************************************************/
    /* Constructors ****************************************************/

    /**
     * <p>Generates FingerPrint from given values.</p>
     *
     * @param ip            IP address
     * @param mac           MAC address
     * @param deviceName    Name of the device.
     */
    public FingerPrint(String ip, String mac, String deviceName) {

        this.ip = ip;
        this.mac = mac;
        this.deviceName = deviceName;
    }

    /**
     * <p>Generates FingerPrint using {@link LocalDeviceProp} static methods.</p>
     *
     * <p>May be problems with {@code deviceName} or {@code MAC}, depends
     * on settings of the device and it's network interface.</p>
     */
    public FingerPrint() {

        try {
            this.ip = LocalDeviceProp.getIp('.');
            this.mac = LocalDeviceProp.getMac(':');
            this.deviceName = LocalDeviceProp.getHostName();
        } catch (UnknownHostException e) {
            this.deviceName = "unknown";
            LOG.log(Level.SEVERE, "The host is not known: " + e.getMessage());
        } catch (SocketException | NullPointerException e) {
            this.mac = "unknown";
            LOG.log(Level.SEVERE, "The problem occurs in the socket: " + e.getMessage());
        }
    }

    /* *****************************************************************/
    /* Instance methods ************************************************/

    /**
     * Overriden method of <strong>toString</strong>.
     *
     * @return String interpretation of the FingerPrint instance.
     */
    @Override
    public String toString() {
        return "FingerPrint{" +
                "ip='" + ip + '\'' +
                ", mac='" + mac + '\'' +
                ", deviceName='" + deviceName + '\'' +
                '}';
    }

    /* *****************************************************************/
    /* Static methods **************************************************/



    /* *****************************************************************/
    /* Getters *********************************************************/

    /**
     * Getter for {@link String} formed {@code ip}
     * of the instance of {@link FingerPrint}
     *
     * @return the value of {@code ip}
     * @see String
     * @see FingerPrint
     */
    public String getIp() {

        return ip;
    }

    /**
     * Getter for {@link String} formed {@code mac}
     * of the instance of {@link FingerPrint}
     *
     * @return the value of {@code mac}
     * @see String
     * @see FingerPrint
     */
    public String getMac() {

        return mac;
    }

    /**
     * Getter for {@link String} formed {@code deviceName}
     * of the instance of {@link FingerPrint}
     *
     * @return the value of {@code deviceName}
     * @see String
     * @see FingerPrint
     */
    public String getDeviceName() {

        return deviceName;
    }
}
