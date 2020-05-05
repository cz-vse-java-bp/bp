package cz.vse.java.util;


import cz.vse.java.utils.random.Charsets;
import cz.vse.java.utils.random.RandomNumberGenerator;

import java.io.Serializable;

/*********************************************************************
 * <p>The class of {@code Token} is used to abstractly define
 * the type of the instances.</p>
 *
 * <p>Instances of this class are used for authentication of the connection
 * between devices mostly.</p>
 *
 * <p>The token is set of randomly generated characters sealed in this instance.</p>
 *
 * <i>Written for project "Connections".</i>
 * @author Vojtěch Pavlů
 * @version 04. 03. 2020
 *
 *
 */
public class Token implements Serializable {


    /* *****************************************************************/
    /* Instance variables **********************************************/

    /** {@link String} formed token */
    private String token;

    /* *****************************************************************/
    /* Static variables ************************************************/

    /**
     * Final field of the default charset
     * used to generate random tokens.
     */
    private static final Charsets DEFAULT_CHARSET = Charsets.CAP_NUM;

    /* *****************************************************************/
    /* Constructors ****************************************************/


    /**
     * <p>Generates the {@link Token} instance with given
     * {@link String} length.</p>
     *
     * @param length    of the String-formed token.
     */
    public Token(int length) {

        this.token = generateToken(length);
    }


    /**
     * <p>Generates token using {@link Token#generateToken()}.</p>
     */
    public Token() {

        this.token = generateToken();
    }

    /**
     * <p>Constructor for the {@link Token} instance just sealing the
     * given {@link String}.</p>
     *
     * @param token         String-formed token to be sealed by instance
     *                      of this class
     */
    public Token(String token) {

        this.token = token;
    }


    /* *****************************************************************/
    /* Instance methods ************************************************/


    /**
     * <p>Validates the given {@link Token} using
     * {@link Token#validate(String)} method.</p>
     *
     * @param token     to be validated.
     *
     * @return          result, if they are equal.
     */
    public boolean validate(Token token) {

        return this.validate(token.getToken());
    }


    /**
     * <p>Validates {@link String}s if they are equal.</p>
     *
     * @param token     String to be validated.
     *
     * @return          result of equality
     */
    public boolean validate(String token) {

        return this.token.equals(token);
    }

    /* *****************************************************************/
    /* Static methods **************************************************/


    /**
     * <p>Generates random token from the {@code DEFAULT_CHARSET}.</p>
     *
     * @param length    The length the token should have.
     * @return          The result {@link String}-formed token.
     */
    private static String generateToken(int length) {

        StringBuilder token = new StringBuilder();

        for (int i = 0; i < length; i++) {

            token.append(DEFAULT_CHARSET.getCharset()[
                    RandomNumberGenerator.getRandomNumberInRange(
                            0, DEFAULT_CHARSET.getCharset().length-1)
                    ]
            );
        }

        return token.toString();
    }


    /**
     * <p>Generates random token from the {@code DEFAULT_CHARSET}.</p>
     *
     * @return          The result {@link String}-formed token.
     */
    private static String generateToken() {

        StringBuilder token = new StringBuilder();

        boolean done = false;

        while(!done) {

            for (int i = 0; i < RandomNumberGenerator.getRandomNumberInRange(30, 50); i++) {

                token.append(DEFAULT_CHARSET.getCharset()[
                        RandomNumberGenerator.getRandomNumberInRange(
                                0, DEFAULT_CHARSET.getCharset().length - 1)
                        ]
                );
            }

            if(token.length() > 0) {

                done = true;
            }
        }

        return token.toString();
    }

    /* *****************************************************************/
    /* Getters *********************************************************/

    /**
     * Getter for {@link Charsets}-formed {@code DEFAULT_CHARSET}
     * of the class of {@link Token}
     *
     * @return the value of {@code DEFAULT_CHARSET}
     *
     * @see Charsets
     * @see Token
     */
    public static Charsets getDefaultCharset() {

        return DEFAULT_CHARSET;
    }


    /**
     * Getter for {@link String}-formed {@code token}
     * of the instance of {@link Token}
     *
     * @return the value of {@code token}
     * @see String
     * @see Token
     */
    public String getToken() {

        return token;
    }


    /**
     * Getter for {@link int}-formed {@code length}
     * of the instance of {@link Token}
     *
     * @return the length of {@code token} interpretation
     * @see Token
     */
    public int length() {

        return token.length();
    }
}
