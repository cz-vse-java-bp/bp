package cz.vse.java.utils;

import cz.vse.java.util.FingerPrint;
import org.junit.Test;

public class FingerPrintTest {



    @Test
    public void getIp() {

        FingerPrint fp;

        for (int i = 0; i < 3; i++) {

            fp = new FingerPrint();
            String ip = fp.getIp();

            System.out.println("IP " + i + ") " + ip);

        }
    }

    @Test
    public void getMac() {

        FingerPrint fp;

        for (int i = 0; i < 3; i++) {

            fp = new FingerPrint();
            String mac = fp.getMac();

            System.out.println("MAC " + i + ") " +mac);

        }
    }

    @Test
    public void getDeviceName() {

        FingerPrint fp;

        for (int i = 0; i < 3; i++) {

            fp = new FingerPrint();
            String dn = fp.getDeviceName();

            System.out.println("Device Name " + i + ") " +dn);

        }
    }
}