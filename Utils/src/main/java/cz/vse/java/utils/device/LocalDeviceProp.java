package cz.vse.java.utils.device;


import java.net.*;

/**************************************************************
 * <p>Class for generating data about the local device.</p>
 *
 * <p>Methods of this class are used to get data about the
 * device like an network element, ie IP, MAC or host name.</p>
 *
 * Written for project "Utils".
 * @author Vojtěch Pavlů
 * @version 11. 02. 2020
 *
 * @see <a href="https://en.wikipedia.org/wiki/IP_address">IP address</a>
 * @see <a href="https://en.wikipedia.org/wiki/MAC_address">MAC address</a>
 * @see <a href="https://en.wikipedia.org/wiki/Hostname">Hostname</a>
 */
public class LocalDeviceProp {


    /* *******************************************************/
    /* Constructors ******************************************/

    private LocalDeviceProp(){}

    /* *******************************************************/
    /* Static methods ****************************************/

    /**
     * Returns MAC address in specified format - values
     * are divided by given character.
     *
     * @param divider   the character dividing the result
     *                  values.
     *
     * @return         String interpretation of the MAC address
     *
     * @throws SocketException          When IO exception occurs
     * @throws UnknownHostException     When the host is not known.
     */
    public static String getMac(char divider) throws SocketException, UnknownHostException {


        InetAddress ip = InetAddress.getLocalHost();
        NetworkInterface networkInterface = NetworkInterface.getByInetAddress(ip);
        byte[] mac = networkInterface.getHardwareAddress();
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < mac.length; i++) {

            sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? divider : ""));
        }

        return sb.toString();
    }


    /**
     * Returns IP address in specified format - values
     * are divided by given character.
     *
     * @param divider   the character dividing the result
     *                  values.
     *
     * @return         String interpretation of the IP address
     *
     * @throws UnknownHostException     When the host is not known.
     */
    public static String getIp(char divider) throws UnknownHostException {

        String ip = InetAddress.getLocalHost().getHostAddress();
        ip = ip.replace('.', divider);

        return ip;
    }


    /**
     * Returns hostname of the device in the network.
     *
     * @return  String interpretation of the device's name
     *
     * @throws UnknownHostException When the host is not known.
     */
    public static String getHostName() throws UnknownHostException {

        return InetAddress.getLocalHost().getHostName();
    }
}
