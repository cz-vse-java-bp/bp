package cz.vse.java.util.persistance.entities;



import cz.vse.java.util.userData.ERole;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;


/*********************************************************************
 * <p>The class of {@code User} is used to abstractly define
 * the type of the instances.</p>
 *
 *
 * <i>Written for project "Connections2".</i>
 * @author Vojtěch Pavlů
 * @version 07. 04. 2020
 *
 *
 * @see cz.vse.java.utils.persistance.entities
 */
public class User implements IEntity {


    /* *****************************************************************/
    /* Instance variables **********************************************/

    private Long id;
    private String firstName;
    private String lastName;
    private String userName;
    private String password;
    private String passwordSalt;
    private LocalDate dateOfCreation;
    private List<ERole> roles;

    /* *****************************************************************/
    /* Static variables ************************************************/

    /**
     * <p>Private static instance of the {@link Logger}
     * - the logger of the {@link User class</p>
     */
    private static final Logger LOG =
            Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    /* *****************************************************************/
    /* Constructors ****************************************************/

    public User() {

        this.roles = new ArrayList<>();
    }

    /* *****************************************************************/
    /* Instance methods ************************************************/


    public void addRole(ERole role) {

        this.roles.add(role);
    }

    /**
     * <p>Checks the validity of the given entity.</p>
     * <p>Mostly checks null values.</p>
     *
     * @return result, if the entity is valid against
     * schema.
     */
    @Override
    public boolean check() {

        return true;
    }

    /**
     * Returns a string representation of the object. In general, the
     * {@code toString} method returns a string that
     * "textually represents" this object. The result should
     * be a concise but informative representation that is easy for a
     * person to read.
     * It is recommended that all subclasses override this method.
     * <p>
     * The {@code toString} method for class {@code Object}
     * returns a string consisting of the name of the class of which the
     * object is an instance, the at-sign character `{@code @}', and
     * the unsigned hexadecimal representation of the hash code of the
     * object. In other words, this method returns a string equal to the
     * value of:
     * <blockquote>
     * <pre>
     * getClass().getName() + '@' + Integer.toHexString(hashCode())
     * </pre></blockquote>
     *
     * @return a string representation of the object.
     */
    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                ", passwordSalt='" + passwordSalt + '\'' +
                ", dateOfCreation=" + dateOfCreation +
                ", roles=" + roles +
                '}';
    }

    /* *****************************************************************/
    /* Static methods **************************************************/



    /* *****************************************************************/
    /* Getters *********************************************************/

    /**
     * Getter for {@link Long} formed {@code id}
     * of the instance of {@link User}
     *
     * @return the value of {@code id}
     * @see Long
     * @see User
     */
    public Long getId() {

        return id;
    }

    /**
     * Getter for {@link String} formed {@code firstName}
     * of the instance of {@link User}
     *
     * @return the value of {@code firstName}
     * @see String
     * @see User
     */
    public String getFirstName() {

        return firstName;
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

    /**
     * Getter for {@link String} formed {@code password}
     * of the instance of {@link User}
     *
     * @return the value of {@code passwordHash}
     * @see String
     * @see User
     */
    public String getPassword() {

        return password;
    }

    /**
     * Getter for {@link String} formed {@code passwordSalt}
     * of the instance of {@link User}
     *
     * @return the value of {@code passwordSalt}
     * @see String
     * @see User
     */
    public String getPasswordSalt() {

        return passwordSalt;
    }

    /**
     * Getter for {@link LocalDate} formed {@code dateOfCreation}
     * of the instance of {@link User}
     *
     * @return the value of {@code dateOfCreation}
     * @see LocalDate
     * @see User
     */
    public LocalDate getDateOfCreation() {

        return dateOfCreation;
    }

    /**
     * Getter for {@link List<ERole>} formed {@code roles}
     * of the instance of {@link User}
     *
     * @return the value of {@code roles}
     * @see List<ERole>
     * @see User
     */
    public List<ERole> getRoles() {

        return roles;
    }

    /* *****************************************************************/
    /* Setters *********************************************************/

    /**
     * <p>Setter for the {@code Long} formed
     * {@code id} variable.</p>
     *
     * @param id given Long value to
     *           be set to the variable
     * @see Long
     * @see User
     */
    public void setId(Long id) {

        this.id = id;
    }

    /**
     * <p>Setter for the {@code String} formed
     * {@code firstName} variable.</p>
     *
     * @param firstName given String value to
     *                  be set to the variable
     * @see String
     * @see User
     */
    public void setFirstName(String firstName) {

        this.firstName = firstName;
    }

    /**
     * <p>Setter for the {@code String} formed
     * {@code lastName} variable.</p>
     *
     * @param lastName given String value to
     *                 be set to the variable
     * @see String
     * @see User
     */
    public void setLastName(String lastName) {

        this.lastName = lastName;
    }

    /**
     * <p>Setter for the {@code String} formed
     * {@code userName} variable.</p>
     *
     * @param userName given String value to
     *                 be set to the variable
     * @see String
     * @see User
     */
    public void setUserName(String userName) {

        this.userName = userName;
    }

    /**
     * <p>Setter for the {@code String} formed
     * {@code passwordHash} variable.</p>
     *
     * @param password given String value to
     *                     be set to the variable
     * @see String
     * @see User
     */
    public void setPassword(String password) {

        this.password = password;
    }

    /**
     * <p>Setter for the {@code String} formed
     * {@code passwordSalt} variable.</p>
     *
     * @param passwordSalt given String value to
     *                     be set to the variable
     * @see String
     * @see User
     */
    public void setPasswordSalt(String passwordSalt) {

        this.passwordSalt = passwordSalt;
    }

    /**
     * <p>Setter for the {@code LocalDate} formed
     * {@code dateOfCreation} variable.</p>
     *
     * @param dateOfCreation given LocalDate value to
     *                       be set to the variable
     * @see LocalDate
     * @see User
     */
    public void setDateOfCreation(LocalDate dateOfCreation) {

        this.dateOfCreation = dateOfCreation;
    }


    /**
     * <p>Setter for the {@code ERole>} formed
     * {@code roles} variable.</p>
     *
     * @param roles given ERole value to
     *              be set to the variable
     * @see List<ERole>
     * @see User
     */
    public void setRoles(List<ERole> roles) {

        this.roles = roles;
    }
}
