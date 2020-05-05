package cz.vse.java.utils.xml;


/**************************************************************
 * <p>The interface of IXMLWriter is used to contain all methods
 * of the type to be overriden (implemented).</p>
 *
 *
 * Written for project "Utils".
 * @author Vojtěch Pavlů
 * @version 13. 02. 2020
 *
 * @see cz.vse.java.utils.xml
 * @see <a href="https://en.wikipedia.org/wiki/XML">
 *     XML from Wikipedia.org</a>
 */
public interface IXMLWriter {


    /* *****************************************************************/
    /* Methods to be overriden *****************************************/

    /**
     * Method for writing to the xml file.
     */
    void write();
}
