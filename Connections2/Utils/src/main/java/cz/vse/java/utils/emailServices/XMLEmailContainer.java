package cz.vse.java.utils.emailServices;


import cz.vse.java.utils.xml.*;
import cz.vse.java.utils.xml.IXMLReader;
import cz.vse.java.utils.xml.IXMLWriter;
import cz.vse.java.utils.xml.ListOfNodes;
import cz.vse.java.utils.xml.XMLSchemaValidator;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.util.ArrayList;

/*********************************************************************
 * <p>The class of {@code XMLEmailContainer} is used to abstractly define
 * the type of the instances.</p>
 *
 * <p>This class implements {@link IXMLWriter} and {@link IXMLReader}
 * to be able to fully deal with XML-based contact list.</p>
 *
 * <p>The actual container of email addresses is stored in the
 * {@code emailsContainer} field and is made of {@link EmailsContainer}
 * instance. The reason to do so is to be able to <i>pseudo</i>-persist
 * the list of emails and work with it dynamically at the same time.</p>
 *
 * <i>Written for project "Utils".</i>
 * @author Vojtěch Pavlů
 * @version 14. 02. 2020
 *
 *
 * @see cz.vse.java.utils.emailServices
 * @see EmailAddress
 * @see EmailsContainer
 * @see IXMLReader
 * @see IXMLWriter
 * @see IXMLValidator
 * @see XMLSchemaValidator
 */
public class XMLEmailContainer implements IXMLReader, IXMLWriter {


    /* *****************************************************************/
    /* Instance variables **********************************************/

    /** The file path to the document */
    private String documentFilePath;

    /** The validator of the XML document */
    private XMLSchemaValidator validator;

    /** The container of the elements */
    private EmailsContainer emailsContainer;

    /* *****************************************************************/
    /* Static variables ************************************************/

    /** The private, static and final field to be able to find out
     * where the schema is located. It's located in the <i>Resource</i>
     * directory, ie <i>src/main/Resources</i> under name of
     * '<b>EmailListSchema.xsd</b>'.*/
    private static final File SCHEMA_FILEPATH = new File(
            XMLEmailContainer.class.getClassLoader().getResource(
                    "EmailListSchema.xsd").getFile()
    );

    /* *****************************************************************/
    /* Constructors ****************************************************/

    /**
     * <p>The parametric constructor to of this class.</p>
     *
     * <p>It's responsible for initializing the field of
     * {@code documentFilePath}, {@code validator}
     * and for validating the given document in the beginning,
     * which means validating the file and validating against
     * the given schema.</p>
     *
     * @param documentFilePath  The absolute path to the XML document.
     *
     * @throws Exception        When the document is not valid,
     *                          when there is no possibility to
     *                          validate the document, when the
     *                          document does not exist or is not
     *                          readable file.
     */
    public XMLEmailContainer(String documentFilePath) throws Exception {

        this.documentFilePath = documentFilePath;

        File file = new File(documentFilePath);
        if((!file.exists()) || (file.isDirectory())){

            throw new Exception("The file is a directory or does not exist!");
        }

        this.validator = new XMLSchemaValidator(
                documentFilePath, SCHEMA_FILEPATH.getAbsolutePath()
        );

        validator.validate();
    }

    /* *****************************************************************/
    /* Instance methods ************************************************/

    /**
     * <p>Method for reading the XML.</p>
     *
     * <p>It reads all needed data and store
     * it in prepared fields (variables).</p>
     *
     * @throws Exception
     */
    @Override
    public void read() throws Exception {

        if(validator.validate()){

            String backslash = "\\\\";
            emailsContainer = new EmailsContainer(
                    documentFilePath.split(backslash)[documentFilePath.split(backslash).length-1]
            );

            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(documentFilePath);
            doc.getDocumentElement().normalize();

            ListOfNodes nList = new ListOfNodes(doc.getElementsByTagName("emailAddress"));

            for (Node email : nList) {

                emailsContainer.add(email.getTextContent());
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

        return documentFilePath;
    }

    /**
     * Method for writing to the xml file.
     */
    @Override
    public void write() {

        try {
            DocumentBuilderFactory dbFactory =
                    DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.newDocument();

            Element root = doc.createElement("emailList");
            doc.appendChild(root);

            for (EmailAddress email : emailsContainer.getEmails()) {

                Element emailElement = doc.createElement("emailAddress");
                emailElement.setTextContent(email.getEmailAddress());

                root.appendChild(emailElement);
            }

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();

            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");

            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File(documentFilePath));
            transformer.transform(source, result);

        } catch (ParserConfigurationException | TransformerException e) {
            e.printStackTrace();
        }
    }

    public void add(EmailAddress emailAddress){

        if(emailAddress.validate()){

            emailsContainer.add(emailAddress);
            write();
        }
    }

    public void addAll(ArrayList<EmailAddress> emails) {

        for (EmailAddress email : emails) {

            this.add(email);
        }
    }

    public void addAll(EmailAddress... emails) {

        for (EmailAddress email : emails) {

            this.add(email);
        }
    }

    public void remove(EmailAddress emailAddress) {

        if(emailAddress.validate() && this.contains(emailAddress)){

            emailsContainer.remove(emailAddress);
            write();
        }
    }

    public void remove(int index){

        if(emailsContainer.getEmails().size()-1 >= index){

            emailsContainer.remove(index);
            write();
        }
    }

    public boolean contains(EmailAddress emailAddress){

        return emailsContainer.contains(emailAddress);
    }

    public int size() {

        return emailsContainer.size();
    }

    /* *****************************************************************/
    /* Static methods **************************************************/



    /* *****************************************************************/
    /* Getters *********************************************************/



    /* *****************************************************************/
    /* Setters *********************************************************/

    
}
