package cz.vse.java.utils.random;


import cz.vse.java.utils.emailServices.EmailAddress;

/**************************************************************
 * <p>Class for creating random well-formed email addresses.</p>
 *
 * <p>Mostly used for testing or creaing random addresses whenever
 * it's needed for.</p>
 *
 *
 * Written for project "Utils".
 * @author Vojtěch Pavlů
 * @version 10. 02. 2020
 *
 * @see EmailAddress
 */
public class RandomEmailAddressGenerator {


    /* *******************************************************/
    /* Constructors ******************************************/

    /**
     * Non-parametric private constructor preventing creation
     * of any instance of this class.
     */
    private RandomEmailAddressGenerator(){}


    /* *******************************************************/
    /* Static methods ****************************************/


    /**
     * Generates random email address and checks it's validity.
     *
     * @return  String-formed random email address.
     */
    public static String generateRandomEmailAddress(){

        String emailAddress = "";
        boolean done = false;

        RandomStringGenerator rsg = new RandomStringGenerator(Charsets.LOWER_CASES, Charsets.NUMBERS);
        RandomStringGenerator rsg2 = new RandomStringGenerator(Charsets.LOWER_CASES, Charsets.NUMBERS, Charsets.DOT);

        while(!done) {

            emailAddress = rsg2.generateRandomString(1, 10) + "@" + rsg.generateRandomString(1, 20) + "." + rsg.generateRandomString(1, 5);

            if(EmailAddress.validate(emailAddress)){

                done = true;
            }
        }
        return emailAddress;
    }


    /**
     * Generates random email address by given lengths.
     *
     * @param maxLength1                    max length String value before '@'
     * @param maxLength2                    max length String value of domain name
     * @param maxLength3                    max length String value after '.'
     *
     * @return                              random String-formed email address
     *
     * @throws IllegalArgumentException     When any of these parameters is smaller
     *                                      than 1.
     */
    public static String generateRandomEmailAddress(int maxLength1,
                                                    int maxLength2,
                                                    int maxLength3)
                                                    throws IllegalArgumentException {

        if(maxLength1 < 1 || maxLength2 < 1 || maxLength3 < 1){

            throw new IllegalArgumentException("All the parameters should be greater than 1!");
        }

        String emailAddress = "";
        boolean done = false;

        RandomStringGenerator rsg = new RandomStringGenerator(Charsets.LOWER_CASES, Charsets.NUMBERS);
        RandomStringGenerator rsg2 = new RandomStringGenerator(Charsets.LOWER_CASES, Charsets.NUMBERS, Charsets.DOT);

        while(!done) {

            emailAddress = rsg2.generateRandomString(1, maxLength1)
                    + "@" + rsg.generateRandomString(1, maxLength2)
                    + "." + rsg.generateRandomString(1, maxLength3);

            if(EmailAddress.validate(emailAddress)){

                done = true;
            }
        }
        return emailAddress;
    }
}
