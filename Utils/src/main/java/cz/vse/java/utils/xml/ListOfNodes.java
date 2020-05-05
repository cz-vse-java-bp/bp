package cz.vse.java.utils.xml;


import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/*********************************************************************
 * <p>The class of {@code ListOfNodes} is used to abstractly define
 * the type of the instances.</p>
 *
 * <p>This class contains methods for dealing with a List of Nodes
 * (in W3C XML context). Substitutes basic {@link NodeList}, where there is
 * no possibility to iterate through and eliminates empty text nodes
 * (made out of whitespaces)</p>
 *
 * <i>Written for project "Utils".</i>
 * @author Vojtěch Pavlů
 * @version 13. 02. 2020
 *
 *
 * @see cz.vse.java.utils.xml
 * @see NodeList
 */
public class ListOfNodes implements NodeList, Iterable<Node> {


    /* *****************************************************************/
    /* Instance variables **********************************************/

    /** The actual list of children nodes */
    private List<Node> children;

    /* *****************************************************************/
    /* Constructors ****************************************************/


    /**
     * The parametric constructor, which is generated by given
     * {@link NodeList} instance.
     *
     * @param list  the {@link NodeList} instance - parent
     *              of the {@link Node} children
     */
    public ListOfNodes(NodeList list) {

        children = new ArrayList<Node>();
        for (int i = 0; i < list.getLength(); i++) {

            if (!isWhitespaceNode(list.item(i))) {

                children.add(list.item(i));
            }
        }
    }

    /* *****************************************************************/
    /* Instance methods ************************************************/


    /**
     * The iterator of the {@link ListOfNodes} instance
     *
     * @return the iterator
     */
    @Override
    public Iterator<Node> iterator() {

        return children.iterator();
    }


    /**
     * Method for getting the item at the actual position
     *
     * @param index index of the {@link Node}
     *
     * @return      the {@link Node} at the position
     *              of the index
     */
    @Override
    public Node item(int index) {
        return children.get(index);
    }


    /**
     * Returns the length of the list
     *
     * @return integer length of the list
     */
    @Override
    public int getLength() {
        return children.size();
    }


    /* *****************************************************************/
    /* Static methods **************************************************/


    /**
     * Returns boolean value about if the node is made
     * out of the whitespaces only. If it is, returns
     * true, else returns false.
     *
     * @param node  the inspected node
     *
     * @return      boolean value about if there is or is
     *              not any non-whitespace character
     */
    private static boolean isWhitespaceNode(Node node) {

        if (node.getNodeType() == Node.TEXT_NODE) {

            return node.getNodeValue().trim().length() == 0;
        } else {

            return false;
        }
    }
}