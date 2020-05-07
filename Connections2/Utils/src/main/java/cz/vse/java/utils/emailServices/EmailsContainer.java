package cz.vse.java.utils.emailServices;


import java.util.ArrayList;

/*********************************************************************
 * <p>The class of {@code EmailsContainer} is used to abstractly define
 * the type of the instances.</p>
 *
 * <p>This class is used as a container of all the emails given to it.
 * The main purpose it's working as one of the main contact list.</p>
 *
 * <i>Written for project "Utils".</i>
 * @author Vojtěch Pavlů
 * @version 14. 02. 2020
 *
 *
 * @see cz.vse.java.utils.emailServices
 */
public class EmailsContainer {


    /* *****************************************************************/
    /* Instance variables **********************************************/

    private ArrayList<EmailAddress> emails = new ArrayList<>();
    private String name;

    /* *****************************************************************/
    /* Static variables ************************************************/

    private static int NUM = 0;

    /* *****************************************************************/
    /* Constructors ****************************************************/

    public EmailsContainer(String name) {

        this.name = name;
    }

    public EmailsContainer(ArrayList<EmailAddress> emails) {

        this.emails = emails;
        this.name = "Email container - " + NUM;
        NUM++;
    }

    public EmailsContainer(ArrayList<EmailAddress> emails, String name) {

        this.emails = emails;
        this.name = name;
    }

    public EmailsContainer() {

        this.name = "Email container - " + NUM;
        NUM++;
    }

    /* *****************************************************************/
    /* Instance methods ************************************************/


    @Override
    public String toString() {
        return "EmailsContainer{" +
                "emails=" + emails +
                ", name='" + name + '\'' +
                '}';
    }

    public void add(EmailAddress emailAddress) {

        if(emailAddress.validate()) {

            emails.add(emailAddress);
        } else {
            throw new IllegalArgumentException("The email address does not correspond " +
                    "with given structure!\nGiven email address: " + emailAddress.getEmailAddress());
        }
    }

    public void add(String emailAddress) {

        try {
            get(emailAddress);
        } catch (Exception e) {
            emails.add(new EmailAddress(emailAddress));
        }
    }

    public EmailAddress get(int index) {

        return emails.get(index);
    }

    public EmailAddress get(String emailAddress) throws Exception {

        EmailAddress result = null;

        for (EmailAddress email : emails) {

            if(email.getEmailAddress().equals(emailAddress)) {

                result = email;
                break;
            }
        }
        if(result != null) {
            return result;
        } else {

            throw new Exception("No such email address present!");
        }
    }

    public void remove(String emailAddress){

        for (EmailAddress email : emails) {

            if(email.getEmailAddress().equals(emailAddress)) {

                emails.remove(email);
                break;
            }
        }
    }

    public void remove(int index){

        emails.remove(index);
    }

    public void clear() {

        emails = new ArrayList<>();
    }

    public boolean contains(EmailAddress emailAddress) {

        try {

            this.get(emailAddress.getEmailAddress());
            return false;
        } catch (Exception e){

            return true;
        }
    }

    public void remove(EmailAddress emailAddress){

        this.remove(emailAddress);
    }


    public int size() {

        return emails.size();
    }


    /* *****************************************************************/
    /* Getters *********************************************************/


    public ArrayList<String> getAllInString(){

        ArrayList<String> emails = new ArrayList<>();

        for (EmailAddress email : this.emails) {

            emails.add(email.getEmailAddress());
        }
        return emails;
    }


    /**
     * Getter for {@link ArrayList} formed {@code emails}
     * of the instance of {@link EmailsContainer}
     *
     * @return the value of {@code emails}
     * @see ArrayList
     * @see EmailsContainer
     */
    public ArrayList<EmailAddress> getEmails() {

        return emails;
    }

    /**
     * Getter for {@link String} formed {@code name}
     * of the instance of {@link EmailsContainer}
     *
     * @return the value of {@code name}
     * @see String
     * @see EmailsContainer
     */
    public String getName() {

        return name;
    }
}
