package cz.vse.java.utils.xml;


/*********************************************************************
 * <p>The class of {@code User} is used to abstractly define
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
public class User {


    /* *****************************************************************/
    /* Instance variables **********************************************/

    private int id;
    private String name;
    private String lastName;
    private String userName;

    /* *****************************************************************/
    /* Static variables ************************************************/



    /* *****************************************************************/
    /* Constructors ****************************************************/

    public User(int id, String name, String lastName, String userName) {

        this.id = id;
        this.name = name;
        this.lastName = lastName;
        this.userName = userName;
    }

    /* *****************************************************************/
    /* Instance methods ************************************************/

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", lastName='" + lastName + '\'' +
                ", userName='" + userName + '\'' +
                '}';
    }

    /* *****************************************************************/
    /* Static methods **************************************************/



    /* *****************************************************************/
    /* Getters *********************************************************/

    /**
     * Getter for {@link String} formed {@code name}
     * of the instance of {@link User}
     *
     * @return the value of {@code name}
     * @see String
     * @see User
     */
    public String getName() {
        return name;
    }

    /**
     * Getter for {@link String} formed {@code lastName}
     * of the instance of {@link User}
     *
     * @return the value of {@code lastName}
     * @see String
     * @see User
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Getter for {@link String} formed {@code userName}
     * of the instance of {@link User}
     *
     * @return the value of {@code userName}
     * @see String
     * @see User
     */
    public String getUserName() {
        return userName;
    }

    /* *****************************************************************/
    /* Setters *********************************************************/



    /* *****************************************************************/
    /* Main method *****************************************************/


    /**
     * The main method of the class of User.
     *
     */
  /*  public static void main(String[] args){
        
        
    }
    
    */
}
