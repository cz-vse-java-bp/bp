package cz.vse.java.utils.emailServices;


import java.util.Objects;

/**************************************************************
 *
 *
 *
 * Written for project "Utils".
 * @author Vojtěch Pavlů
 * @version 10. 02. 2020
 */
public class EmailAddress {


    /* *******************************************************/
    /* Instance variables ************************************/

    private String emailAddress;

    /* *******************************************************/
    /* Static variables **************************************/

    private static final String ADDRESS_REGEX =
            "([0-9a-zA-Z]([-.\\w]*[0-9a-zA-Z])*@([0-9a-zA-Z][-\\w]*[0-9a-zA-Z]\\.)+[a-zA-Z]{2,9})";

    /* *******************************************************/
    /* Constructors ******************************************/

    public EmailAddress(String emailAddress) {

        if(validate(emailAddress)) {

            this.emailAddress = emailAddress;
        } else {
            throw new IllegalArgumentException("The email address does not correspond " +
                    "with given structure!\nGiven email address: " + emailAddress);
        }
    }

    /* *******************************************************/
    /* Instance methods **************************************/

    public boolean validate(){

        return EmailAddress.validate(this);
    }

    @Override
    public String toString() {
        return "EmailAddress{" +
                "emailAddress='" + emailAddress + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EmailAddress that = (EmailAddress) o;
        return Objects.equals(emailAddress, that.emailAddress);
    }

    @Override
    public int hashCode() {

        return Objects.hash(emailAddress);
    }

    /* *******************************************************/
    /* Static methods ****************************************/

    public static boolean validate(String emailAddress){

        return emailAddress.matches(ADDRESS_REGEX);
    }

    public static boolean validate(EmailAddress emailAddress){

        return validate(emailAddress.getEmailAddress());
    }

    /* *******************************************************/
    /* Getters ***********************************************/

    /**
     * Getter for {@link String} formed {@code emailAddress}
     * of the instance of {@link EmailAddress}
     *
     * @return the value of {@code emailAddress}
     *
     * @see String
     * @see EmailAddress
     */
    public String getEmailAddress() {

        return emailAddress;
    }

    /**
     * Getter for {@link String} formed {@code ADDRESS_REGEX}
     * of the instance of {@link EmailAddress}
     *
     * @return the value of {@code ADDRESS_REGEX}
     *
     * @see String
     * @see EmailAddress
     */
    public static String getAddressRegex() {

        return ADDRESS_REGEX;
    }

    /* *******************************************************/
    /* Setters ***********************************************/

}
