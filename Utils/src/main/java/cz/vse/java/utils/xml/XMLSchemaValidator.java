package cz.vse.java.utils.xml;


import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.SchemaFactory;
import java.io.File;

/*********************************************************************
 * <p>The class of {@code XMLSchemaValidator} is used to abstractly define
 * the type of the instances.</p>
 *
 * <p>This class provides methods for validating the XML against
 * XMLSchema.</p>
 *
 * <i>Written for project "Utils".</i>
 * @author Vojtěch Pavlů
 * @version 13. 02. 2020
 *
 *
 * @see cz.vse.java.utils.xml
 * @see IXMLValidator
 * @see IXMLReader
 * @see IXMLWriter
 */
public class XMLSchemaValidator implements IXMLValidator {


    /* *****************************************************************/
    /* Instance variables **********************************************/

    /** Fields of XML document and XMLSchema file paths */
    private String XMLFilePath;
    private String XMLSchemaFilePath;

    /* *****************************************************************/
    /* Constructors ****************************************************/

    /**
     * <p>Parametric constructor used for filling the fields
     * of file paths.</p>
     *
     * @param XMLFilePath       the path to the xml file to be
     *                          validated
     * @param XMLSchemaFilePath the file path to the validation
     *                          schema
     */
    public XMLSchemaValidator(String XMLFilePath, String XMLSchemaFilePath) {

        if(new File(XMLFilePath).exists() && new File(XMLSchemaFilePath).exists()){

            this.XMLFilePath = XMLFilePath;
            this.XMLSchemaFilePath = XMLSchemaFilePath;
        } else {
            throw new IllegalArgumentException("Any of the given file paths does not exist!" +
                    "\nThe document: " + XMLFilePath
                    + "\nor the schema: " + XMLSchemaFilePath);
        }

    }

    /* *****************************************************************/
    /* Instance methods ************************************************/

    /**
     * Overriden method of <strong>toString</strong>.
     *
     * @return String interpretation of the XMLSchemaValidator instance.
     */
    @Override
    public String toString() {
        return "XMLSchemaValidator{" +
                "XMLFilePath='" + XMLFilePath + '\'' +
                ", XMLSchemaFilePath='" + XMLSchemaFilePath + '\'' +
                '}';
    }

    /**
     * <p>Validates the XML file against given schema. When
     * the file does not correspond it, the method returns
     * false, otherwise it returns true.</p>
     *
     * @return boolean value of the result of the
     * validation against the given schema
     */
    @Override
    public boolean validate() throws Exception {

        SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        ((schemaFactory.newSchema(new File(XMLSchemaFilePath))).newValidator())
                    .validate(new StreamSource(new File(XMLFilePath)));

        return true;
    }


    /* *****************************************************************/
    /* Getters *********************************************************/

    /**
     * Getter for {@link String} formed {@code XMLFilePath}
     * of the instance of {@link XMLSchemaValidator}
     *
     * @return the value of {@code XMLFilePath}
     * @see String
     * @see XMLSchemaValidator
     */
    public String getXMLFilePath() {

        return XMLFilePath;
    }

    /**
     * Getter for {@link String} formed {@code XMLSchemaFilePath}
     * of the instance of {@link XMLSchemaValidator}
     *
     * @return the value of {@code XMLSchemaFilePath}
     * @see String
     * @see XMLSchemaValidator
     */
    public String getXMLSchemaFilePath() {

        return XMLSchemaFilePath;
    }

}
