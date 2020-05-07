package cz.vse.java.utils.xml;


/*********************************************************************
 * <p>The class of {@code xmltest} is used to abstractly define
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
public class xmltest implements IXMLReader {


    /* *****************************************************************/
    /* Instance variables **********************************************/

    private String filePath;



    /* *****************************************************************/
    /* Static variables ************************************************/



    /* *****************************************************************/
    /* Constructors ****************************************************/

    public xmltest(String filePath) {

        this.filePath = filePath;
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
    public void read() {


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

    /* *****************************************************************/
    /* Static methods **************************************************/



    /* *****************************************************************/
    /* Getters *********************************************************/



    /* *****************************************************************/
    /* Setters *********************************************************/



    /* *****************************************************************/
    /* Main method *****************************************************/

    public static Long counter = 0L;


    /**
     * The main method of the class of xmltest.
     *
     */
    public static void main(String[] args){
        
        Runnable t1 = new Runnable() {
            @Override
            public void run() {

                synchronized (counter) {

                    while (counter < 100) {

                        System.out.println("Thread t1:" + counter);
                        counter = counter + 1;
                        try {
                            Thread.sleep(10);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        };

        Runnable t2 = new Runnable() {
            @Override
            public void run() {

                synchronized (counter) {

                    while (counter < 200) {

                        System.out.println("Thread t2:" + counter);
                        counter = counter + 1;
                        try {
                            Thread.sleep(10);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        };

        t1.run();
        t2.run();
    }
    

}
