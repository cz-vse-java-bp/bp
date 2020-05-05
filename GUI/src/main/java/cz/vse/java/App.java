package cz.vse.java;


import java.util.logging.Logger;


/*********************************************************************
 * <p>The class of {@code App} is used to abstractly define
 * the type of the instances.</p>
 *
 *
 * <i>Written for project "GUI".</i>
 * @author Vojtěch Pavlů
 * @version 01. 04. 2020
 *
 *
 * @see cz.vse.java
 */
public class App {


    /* *****************************************************************/
    /* Instance variables **********************************************/



    /* *****************************************************************/
    /* Static variables ************************************************/

    /**
     * <p>Private static instance of the {@link Logger}
     * - the logger of the {@link App class</p>
     */
    private static final Logger LOG =
            Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    /* *****************************************************************/
    /* Constructors ****************************************************/



    /* *****************************************************************/
    /* Instance methods ************************************************/

    /**
     * <p>Overriden method of <strong>toString</strong>.</p>
     *
     * @return {@link String} interpretation of the App instance.
     */
    @Override
    public String toString() {

        return "App.toString() - NOT DEFINED YET!";
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
     * The main method of the class of App.
     *
     */
   public static void main(final String[] args){
        
        Launcher.main(args);
    }

}
