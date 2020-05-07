package cz.vse.java.utils.xml;


import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.util.ArrayList;

/*********************************************************************
 * <p>The class of {@code CarsExample} is used to abstractly define
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
public class CarsExample implements IXMLWriter {


    /* *****************************************************************/
    /* Instance variables **********************************************/

    private String filePath;

    private ArrayList<String> cars;

    /* *****************************************************************/
    /* Static variables ************************************************/



    /* *****************************************************************/
    /* Constructors ****************************************************/

    public CarsExample(String filePath) {

        this.filePath = filePath;
        cars = new ArrayList<>();
        cars.add("ferrari");
        cars.add("lamborghini");
        cars.add("porsche");
    }

    /* *****************************************************************/
    /* Instance methods ************************************************/

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

            // root element
            Element rootElement = doc.createElement("cars");
            doc.appendChild(rootElement);

            // supercars element
            Element supercars = doc.createElement("supercars");
            rootElement.appendChild(supercars);

            for (String car : cars) {

                Element supercar = doc.createElement("supercar");
                Attr attr = doc.createAttribute("company");
                attr.setValue(car);
                supercar.setAttributeNode(attr);

                Element carname = doc.createElement("carname");
                Attr attrType = doc.createAttribute("type");
                attrType.setValue("formula one");
                carname.setAttributeNode(attrType);
                carname.appendChild(doc.createTextNode("Ferrari 101"));
                supercar.appendChild(carname);

                Element carname1 = doc.createElement("carname");
                Attr attrType1 = doc.createAttribute("type");
                attrType1.setValue("sports");
                carname1.setAttributeNode(attrType1);
                carname1.appendChild(doc.createTextNode("Ferrari 202"));
                supercar.appendChild(carname1);
                supercars.appendChild(supercar);
            }

            // carname element


            // write the content into xml file
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File(this.filePath));
            transformer.transform(source, result);

            /*// Output to console for testing
            StreamResult consoleResult = new StreamResult(System.out);
            transformer.transform(source, consoleResult);*/
        } catch (Exception e) {
            e.printStackTrace();
        }
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
     * The main method of the class of CarsExample.
     *
     */
    public static void main(String[] args){
        
        CarsExample e = new CarsExample("C:\\Users\\user\\Desktop\\test.xml");
        e.write();
    }
    

}
