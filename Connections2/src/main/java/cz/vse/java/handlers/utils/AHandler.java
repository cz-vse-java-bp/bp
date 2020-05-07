package cz.vse.java.handlers.utils;



/*********************************************************************
 * <p>The class of {@code AHandler} is used to abstractly define
 * the type of the instances.</p>
 *
 * <p>This abstract class contains a basic container only for easier
 * implementation of it's descendants.</p>
 *
 * <i>Written for project "Connections2".</i>
 * @author Vojtěch Pavlů
 * @version 02. 04. 2020
 *
 *
 * .vse.java.handlers
 */
public abstract class AHandler implements IHandler {


    /* *****************************************************************/
    /* Instance variables **********************************************/

    private HandlerContainer container;


    /* *****************************************************************/
    /* Constructors ****************************************************/

    public AHandler(HandlerContainer container) {

        this.container = container;
    }

    /* *****************************************************************/
    /* Instance methods ************************************************/



    /* *****************************************************************/
    /* Static methods **************************************************/



    /* *****************************************************************/
    /* Getters *********************************************************/

    /**
     * Getter for {@link HandlerContainer} formed {@code container}
     * of the instance of {@link AHandler}
     *
     * @return the value of {@code container}
     * @see HandlerContainer
     * @see AHandler
     */
    public HandlerContainer getContainer() {

        return container;
    }

    /* *****************************************************************/
    /* Setters *********************************************************/

    /**
     * <p>Setter for the {@code HandlerContainer} formed
     * {@code container} variable.</p>
     *
     * @param container given HandlerContainer value to
     *                  be set to the variable
     * @see HandlerContainer
     * @see AHandler
     */
    public void setContainer(HandlerContainer container) {

        this.container = container;
    }

    /* *****************************************************************/
    /* Main method *****************************************************/


}
