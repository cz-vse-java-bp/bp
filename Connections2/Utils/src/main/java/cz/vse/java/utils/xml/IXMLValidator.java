package cz.vse.java.utils.xml;


import org.xml.sax.SAXException;

import java.io.IOException;

/**************************************************************
 * <p>The interface of IXMLValidator is used to contain all methods
 * of the type to be overriden (implemented).</p>
 *
 * <p>The interface is defining methods to validate the XML
 * documents against given schema.</p>
 *
 * Written for project "Utils".
 * @author Vojtěch Pavlů
 * @version 13. 02. 2020
 *
 * @see cz.vse.java.utils.xml
 * @see <a href="https://en.wikipedia.org/wiki/XML">
 *     XML from Wikipedia.org</a>
 */
public interface IXMLValidator {


    /* *****************************************************************/
    /* Methods to be overriden *****************************************/

    /**
     * <p>Validates the XML file against given schema. When
     * the file does not correspond it, the method returns
     * false, otherwise it returns true.</p>
     *
     * @return          boolean value of the result of the
     *                  validation against the given schema
     *
     * @throws Exception When the document is invalid or when
     *                   any of the files is unreachable
     */
    boolean validate() throws Exception;
}
