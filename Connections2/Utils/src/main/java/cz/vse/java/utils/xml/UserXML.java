package cz.vse.java.utils.xml;


import org.w3c.dom.Document;
import org.w3c.dom.Node;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.util.ArrayList;

/*********************************************************************
 * <p>The class of {@code UserXML} is used to abstractly define
 * the type of the instances.</p>
 *
 *
 * <i>Written for project "Utils".</i>
 * @author Vojtěch Pavlů
 * @version 13. 02. 2020
 *
 *
 * @see cz.vse.java.utils.xml
 */
public class UserXML implements IXMLWriter, IXMLReader {


    /* *****************************************************************/
    /* Instance variables **********************************************/

    private ArrayList<User> users;

    private String filePath;

    private XMLSchemaValidator validator;



    /* *****************************************************************/
    /* Static variables ************************************************/



    /* *****************************************************************/
    /* Constructors ****************************************************/

    public UserXML(String filePath, String schemaFilePath){

        this.filePath = filePath;
        users = new ArrayList<>();
        validator = new XMLSchemaValidator(filePath, schemaFilePath);
    }

    /* *****************************************************************/
    /* Instance methods ************************************************/

    /**
     * <p>Method for reading the XML.</p>
     *
     * <p>It reads all needed data and store
     * it in prepared fields (variables).</p>
     */
    @Override
    public void read() throws Exception {

        if(validator.validate()) {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(filePath);
            doc.getDocumentElement().normalize();

            ListOfNodes nList = new ListOfNodes(doc.getElementsByTagName("user"));

            for (Node u : nList) {

                int id = Integer.parseInt(u.getAttributes().item(0).getTextContent());

                ListOfNodes ln = new ListOfNodes(u.getChildNodes());

                String name = ln.item(0).getTextContent();
                String lastname = ln.item(1).getTextContent();
                String username = ln.item(2).getTextContent();

                User user = new User(id, name, lastname, username);
                users.add(user);
            }
        }
    }

    /**
     * <p>Method for returning the file path,
     * ie where the file is stored at.</p>
     *
     * @return the path to the file
     */
    @Override
    public String getFilePath() {

        return filePath;
    }

    /**
     * Method for writing to the xml file.
     */
    @Override
    public void write() {


    }



    /* *****************************************************************/
    /* Static methods **************************************************/



    /* *****************************************************************/
    /* Getters *********************************************************/



    /* *****************************************************************/
    /* Setters *********************************************************/



    /* *****************************************************************/
    /* Main method *****************************************************/


    /**
     * The main method of the class of UserXML.
     *
     */
    public static void main(String[] args) throws Exception {
        
        UserXML ux = new UserXML("C:\\Users\\user\\Desktop\\users.xml",
                "C:\\Users\\user\\Desktop\\usersSchema.xsd");
        ux.read();

        for (User user : ux.users) {

            System.out.println(user.toString());
        }
    }
    

}
