package cz.vse.java.utils.xml;


/**************************************************************
 * <p>The interface of IXMLReader is used to contain all
 * methods of the type to be overriden (implemented).</p>
 *
 * <p>This interface defines methods of XML reading</p>
 *
 * Written for project "Utils".
 * @author Vojtěch Pavlů
 * @version 13. 02. 2020
 *
 * @see cz.vse.java.utils.xml
 * @see <a href="https://en.wikipedia.org/wiki/XML">
 *     XML from Wikipedia.org</a>
 */
public interface IXMLReader {


    /* *****************************************************************/
    /* Methods to be overriden *****************************************/

    /**
     * <p>Method for reading the XML.</p>
     *
     * <p>It reads all needed data and store
     * it in prepared fields (variables).</p>
     *
     * @throws Exception
     */
    void read() throws Exception;


    /**
     * <p>Method for returning the file path,
     * ie where the file is stored at.</p>
     *
     * @return  the path to the file
     */
    String getFilePath();
}
