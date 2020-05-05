package cz.vse.java.admin.utils;


import cz.vse.java.util.persistance.entities.User;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.logging.Logger;


/*********************************************************************
 * <p>The class of {@code UserSealer} is used to abstractly define
 * the type of the instances.</p>
 *
 *
 * <i>Written for project "GUI".</i>
 * @author Vojtěch Pavlů
 * @version 17. 04. 2020
 *
 *
 * @see cz.vse.java.admin.utils
 */
public class UserSealer {


    /* *****************************************************************/
    /* Instance variables **********************************************/

    private Long id;
    private String firstname;
    private String lastname;
    private String username;
    private LocalDate dateOfCreation;

    private User user;

    /* *****************************************************************/
    /* Static variables ************************************************/

    /**
     * <p>Private static instance of the {@link Logger}
     * - the logger of the {@link UserSealer class</p>
     */
    private static final Logger LOG =
            Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    /* *****************************************************************/
    /* Constructors ****************************************************/

    public UserSealer(User user) {

        this.user = user;

        this.id = user.getId();
        this.firstname = user.getFirstName();
        this.lastname = user.getLastName();
        this.username = user.getUserName();
        this.dateOfCreation = user.getDateOfCreation();

    }

    /* *****************************************************************/
    /* Instance methods ************************************************/



    /* *****************************************************************/
    /* Static methods **************************************************/



    /* *****************************************************************/
    /* Getters *********************************************************/

    /**
     * Getter for {@link Long} formed {@code id}
     * of the instance of {@link UserSealer}
     *
     * @return the value of {@code id}
     * @see Long
     * @see UserSealer
     */
    public Long getId() {

        return id;
    }

    /**
     * Getter for {@link String} formed {@code firstname}
     * of the instance of {@link UserSealer}
     *
     * @return the value of {@code firstname}
     * @see String
     * @see UserSealer
     */
    public String getFirstname() {

        return firstname;
    }

    /**
     * Getter for {@link String} formed {@code lastname}
     * of the instance of {@link UserSealer}
     *
     * @return the value of {@code lastname}
     * @see String
     * @see UserSealer
     */
    public String getLastname() {

        return lastname;
    }

    /**
     * Getter for {@link String} formed {@code username}
     * of the instance of {@link UserSealer}
     *
     * @return the value of {@code username}
     * @see String
     * @see UserSealer
     */
    public String getUsername() {

        return username;
    }

    /**
     * Getter for {@link LocalDate} formed {@code dateOfCreation}
     * of the instance of {@link UserSealer}
     *
     * @return the value of {@code dateOfCreation}
     * @see LocalDate
     * @see UserSealer
     */
    public LocalDate getDateOfCreation() {

        return dateOfCreation;
    }

    /**
     * Getter for {@link User} formed {@code user}
     * of the instance of {@link UserSealer}
     *
     * @return the value of {@code user}
     * @see User
     * @see UserSealer
     */
    public User getUser() {

        return user;
    }


    /* *****************************************************************/
    /* Setters *********************************************************/



}
