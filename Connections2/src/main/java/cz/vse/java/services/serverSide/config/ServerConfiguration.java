package cz.vse.java.services.serverSide.config;


import cz.vse.java.services.serverSide.EServiceType;
import cz.vse.java.services.serverSide.Router;
import cz.vse.java.services.serverSide.Server;
import cz.vse.java.util.database.DBConnection;
import cz.vse.java.util.database.DatabaseConnectionContainer;
import cz.vse.java.util.database.EDBUse;
import cz.vse.java.utils.xml.ListOfNodes;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.Scanner;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Logger;


/*********************************************************************
 * <p>The class of {@code ServerConfiguration} is used to abstractly define
 * the type of the instances.</p>
 *
 *
 * <i>Written for project "Connections2".</i>
 * @author Vojtěch Pavlů
 * @version 28. 04. 2020
 *
 *
 * @see cz.vse.java.services.serverSide.config
 */
public class ServerConfiguration {


    /* *****************************************************************/
    /* Instance variables **********************************************/

    private CopyOnWriteArrayList<ServiceConfiguration> services;
    private int routerPort;
    private String routerIP;

    private String ksPath;
    private String tsPath;
    private String ksPass;
    private String tsPass;

    /* *****************************************************************/
    /* Static variables ************************************************/

    /**
     * <p>Private static instance of the {@link Logger}
     * - the logger of the {@link ServerConfiguration class</p>
     */
    private static final Logger LOG =
            Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    /* *****************************************************************/
    /* Constructors ****************************************************/

    /**
     * <p>Reads data from the given {@link Document} field ({@code doc}).
     * Meanwhile it print's the data it reads to the console in tables.</p>
     *
     * <p>It also let's the user initializing the {@link Server} choose, if
     * the configuration is correct.</p>
     *
     * @param doc   document it should work with.
     */
    public ServerConfiguration(Document doc) {

        this.services = new CopyOnWriteArrayList<>();

        Node meta = doc.getElementsByTagName("metaInfo").item(0);

        System.out.println("----------------------------------------------" +
                "--------------------------------------------");
        System.out.println(String.format("| %-15s | %s", "Configuration: ", doc.getDocumentURI()));
        System.out.println(String.format("| %-15s | %s", "Created by: ", new ListOfNodes(meta.getChildNodes()).item(0).getTextContent()));
        System.out.println(String.format("| %-15s | %s", "Date: ", new ListOfNodes(meta.getChildNodes()).item(1).getTextContent()));

        System.out.println("----------------------------------------------" +
                "--------------------------------------------");

        ListOfNodes certs = new ListOfNodes(doc.getElementsByTagName("certs")
                .item(0).getChildNodes());

        this.ksPath = certs.item(0).getTextContent();
        this.ksPass = certs.item(1).getTextContent();
        this.tsPath = certs.item(2).getTextContent();
        this.tsPass = certs.item(3).getTextContent();

        ListOfNodes rNodes = new ListOfNodes(doc.getElementsByTagName("router").item(0).getChildNodes());
        Node config = rNodes.item(0);

        System.out.println("\n------------------------------------------");

        if(config.getNodeName().equals("routerConfig")) {

            ListOfNodes confProps = new ListOfNodes(config.getChildNodes());

            int cPort = Integer.parseInt(confProps.item(0).getTextContent());
            this.routerPort = Integer.parseInt(confProps.item(1).getTextContent());
            this.routerIP = "localhost";
            int maxC = Integer.parseInt(new ListOfNodes(confProps.item(2)
                    .getChildNodes()).item(0).getTextContent());

            int maxS;

            String numOfServices = new ListOfNodes(confProps.item(2)
                    .getChildNodes()).item(1).getTextContent();

            if(numOfServices.isEmpty() || numOfServices.isBlank()) {

                maxS = 10;

            } else {

                maxS = Integer.parseInt(numOfServices);
            }


            Router router = new Router(maxC, maxS, cPort, this.routerPort, this.ksPath, this.ksPass);
            Server.getInstance().setRouter(router);

            System.out.println(String.format("| %-20s | %-15s |", "Router access", "localhost"));
            System.out.println(String.format("| %-20s | %-15d |", "Client port", cPort));
            System.out.println(String.format("| %-20s | %-15d |", "Service port", routerPort));

        } else if(config.getNodeName().equals("connTo")) {

            this.routerIP = new ListOfNodes(config.getChildNodes())
                    .item(0).getTextContent();

            this.routerPort = Integer.parseInt(
                    new ListOfNodes(
                        config.getChildNodes()).item(1).getTextContent()
            );

            System.out.println(String.format("| %-20s | %-15s |", "Router access", "Remote access"));
            System.out.println(String.format("| %-20s | %-15s |", "Router IP", routerIP));
            System.out.println(String.format("| %-20s | %-15d |", "Service port", routerPort));
        }
        else {
            System.out.println(config.getNodeName());
        }

        System.out.println("------------------------------------------\n");


        NodeList nl = doc.getElementsByTagName("access");
        System.out.println("\n----------------------------------------------" +
                "--------------------------------------------");
        System.out.println(String.format("| %-30s | %-35s | %-15s |", "USAGE", "DATABASE", "USERNAME"));
        System.out.println("--------------------------------" +
                "----------------------------------------------------------");
        for (int i = 0; i < nl.getLength(); i++) {

            ListOfNodes nl2 = new ListOfNodes(nl.item(i).getChildNodes());

            String use = nl2.item(0).getTextContent();
            String url = nl2.item(1).getTextContent();
            String userName = nl2.item(2).getTextContent();
            String password = nl2.item(3).getTextContent();

            System.out.println(String.format("| %-30s | %-35s | %-15s |",
                    nl2.item(0).getTextContent(),
                    nl2.item(1).getTextContent(),
                    nl2.item(2).getTextContent()
            ));


            setDBUse(use, url, userName, password);
        }

        System.out.println("----------------------------" +
                "--------------------------------------------------------------\n");

        ListOfNodes services = new ListOfNodes(doc.getElementsByTagName("service"));

        System.out.println("------------------------------------------------------------");

        System.out.println(String.format("| %-20s | %-15s | %-15s |",
                "SERVICE NAME", "CLIENTS PORT", "MAX CLIENTS"));

        System.out.println("------------------------------------------------------------");

        for (Node service : services) {

            ListOfNodes se = new ListOfNodes(service.getChildNodes());

            String sName = se.item(0).getTextContent();
            String cPort = se.item(1).getTextContent();
            String maxConns = new ListOfNodes(
                        se.item(2).getChildNodes()
                ).item(0).getTextContent();

            System.out.println(String.format("| %-20s | %-15s | %-15s |", sName, cPort, maxConns));

            this.services.addIfAbsent(this.createServiceConf(service));
        }

        System.out.println("------------------------------------------------------------\n");


        System.out.println("\n----------------------------------------------" +
                "--------------------------------------------");
        System.out.println(String.format("| %-10s | %s", "Keystore", ksPath));
        System.out.println(String.format("| %-10s | %s", "Truststore", tsPath));

        System.out.println("----------------------------------------------" +
                "--------------------------------------------\n");

        startServer();
    }


    /* *****************************************************************/
    /* Instance methods ************************************************/


    /**
     * <p>Creates configuration of service by given service defining {@link Node}.</p>
     *
     * @param node  storing data about the configuration of service.
     *
     * @return      {@link ServiceConfiguration} containing given data.
     */
    private ServiceConfiguration createServiceConf(Node node) {

        ListOfNodes children = new ListOfNodes(node.getChildNodes());

        EServiceType type = EServiceType.getByName(
                children.item(0).getTextContent());

        int clientsPort = Integer.parseInt(children.item(1).getTextContent());
        int maxClients = Integer.parseInt(new ListOfNodes(
                children.item(2).getChildNodes())
                .item(0).getTextContent()
        );


        return new ServiceConfiguration(type, clientsPort, maxClients, this);
    }


    /**
     * <p>Starts the server by asking the user for acceptance
     * of the generated data. When he accepts, the {@link Server}
     * is about to run, otherwise is asked again or the server is closed.</p>
     */
    private void startServer() {

        boolean done = false;

        while(!done) {

            System.out.println("Do you agree? (y/n)");
            Scanner sc = new Scanner(System.in);
            String answer = sc.nextLine();

            answer = answer.toLowerCase().trim();

            if(answer.equals("yes") || answer.equals("y")) {

                Server.getInstance().startRouter();

                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                for (ServiceConfiguration serviceConf : this.services) {

                    serviceConf.create();
                }

                new Thread(Server.getInstance()).start();

                done = true;

            } else if(answer.equals("no") || answer.equals("n")) {

                Server.reset();
                System.out.println("Edit the config file or select another XML configuration file then.");
                done = true;

            } else {

                System.out.println("Wrong answer! Try to type just 'y' or 'n'.");
            }
        }
    }

    /* *****************************************************************/
    /* Static methods **************************************************/


    /**
     * <p>Sets the DB connection to {@link DatabaseConnectionContainer} using
     * {@link EDBUse} enum instance and {@link DBConnection},
     * which is generated in here</p>
     *
     * @param name          name of the usage
     * @param url           where the DB is at
     * @param userName      user with privileges to run queries defined by this use
     * @param password      password used for authentication of the user
     */
    private static void setDBUse(String name, String url, String userName, String password) {

        EDBUse use = EDBUse.getByName(name);

        if(use != null) {

            DatabaseConnectionContainer.getInstance().add(use,
                    new DBConnection(url, userName, password));

        } else {

            throw new IllegalArgumentException("Not possible EDBUse name: " + name);
        }
    }


    /* *****************************************************************/
    /* Getters *********************************************************/

    /**
     * Getter for {@link CopyOnWriteArrayList<>} formed {@code services}
     * of the instance of {@link ServerConfiguration}
     *
     * @return the value of {@code services}
     * @see CopyOnWriteArrayList<>
     * @see ServerConfiguration
     */
    public CopyOnWriteArrayList<ServiceConfiguration> getServices() {

        return services;
    }

    /**
     * Getter for {@link int} formed {@code routerPort}
     * of the instance of {@link ServerConfiguration}
     *
     * @return the value of {@code routerPort}
     * @see int
     * @see ServerConfiguration
     */
    public int getRouterPort() {

        return routerPort;
    }

    /**
     * Getter for {@link String} formed {@code routerIP}
     * of the instance of {@link ServerConfiguration}
     *
     * @return the value of {@code routerIP}
     * @see String
     * @see ServerConfiguration
     */
    public String getRouterIP() {

        return routerIP;
    }

    /**
     * Getter for {@link String} formed {@code ksPath}
     * of the instance of {@link ServerConfiguration}
     *
     * @return the value of {@code ksPath}
     * @see String
     * @see ServerConfiguration
     */
    public String getKsPath() {

        return ksPath;
    }

    /**
     * Getter for {@link String} formed {@code tsPath}
     * of the instance of {@link ServerConfiguration}
     *
     * @return the value of {@code tsPath}
     * @see String
     * @see ServerConfiguration
     */
    public String getTsPath() {

        return tsPath;
    }

    /**
     * Getter for {@link String} formed {@code ksPass}
     * of the instance of {@link ServerConfiguration}
     *
     * @return the value of {@code ksPass}
     * @see String
     * @see ServerConfiguration
     */
    public String getKsPass() {

        return ksPass;
    }

    /**
     * Getter for {@link String} formed {@code tsPass}
     * of the instance of {@link ServerConfiguration}
     *
     * @return the value of {@code tsPass}
     * @see String
     * @see ServerConfiguration
     */
    public String getTsPass() {

        return tsPass;
    }

    /* *****************************************************************/
    /* Setters *********************************************************/



}
