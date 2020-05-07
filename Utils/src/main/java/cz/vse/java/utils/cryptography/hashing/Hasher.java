package cz.vse.java.utils.cryptography.hashing;


import cz.vse.java.utils.random.RandomNumberGenerator;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.logging.Logger;

/**************************************************************
 * <p>The class of {@link Hasher} is used for hashing any String
 * message. Supports hashing with or without salt.</p>
 *
 * <p>The salt can be generated from random number (with given
 * length) or using input String.</p>
 *
 * <p>The accepted algorithms are defined by {@link HashAlgorithms},
 * which means:</p>
 *
 * <ul>
 *     <li>none</li>
 *     <li>MD5</li>
 *     <li>SHA-1</li>
 *     <li>SHA-256</li>
 *     <li>SHA-384</li>
 *     <li>SHA-512</li>
 * </ul>
 *
 * <b>Do not use 'none' option, because it may be unstable.</b>
 *
 * Written for project "Utils".
 * @author Vojtěch Pavlů
 * @version 07. 02. 2020
 *
 * @see  <a href="https://en.wikipedia.org/wiki/Hash_function">
 *       Hash function</a> from Wikipedia
 * @see HashAlgorithms
 */
public class Hasher {


    /* *******************************************************/
    /* Instance variables ************************************/

    /** Information about algorithm container */
    private final String algorithm;

    /** Information about if is defined the algorithm */
    private final boolean isDefined;

    /** Charset used for generating pepper */
    private final char[] charset;

    /** All generated combinations, used for pepper */
    private ArrayList<String> pepperVariations;

    /* *******************************************************/
    /* Static variables **************************************/

    /** Defined values of bytes in 'char form' */
    private static final char[] HEX_ARR =
            "0123456789ABCDEF".toCharArray();

    /** Default charset used */
    private static final char[] DEFAULT_CHARSET =
            "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();

    /** Logger of the class */
    private static final Logger LOG = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    /* *******************************************************/
    /* Constructors ******************************************/


    /**
     * Parametric constructor for generating instanece of the {@link Hasher}
     * class.
     *
     * @param algorithm                     Chosen algorithm. Should not be
     *                                      changed during lifecycle of the
     *                                      instance.
     *                                      Recommended to use the {@link HashAlgorithms}
     *                                      class by it's static {@code getInstance()}
     *                                      method.
     *
     * @throws NoSuchAlgorithmException     When the algorithm is not supported.
     */
    public Hasher(String algorithm) throws NoSuchAlgorithmException {

        isDefined = checkAlg(algorithm);
        this.algorithm = algorithm;
        charset = DEFAULT_CHARSET;
    }


    /**
     * Parametric constructor for generating instanece of the {@link Hasher}
     * class.
     *
     * @param algorithm                     Chosen algorithm. Should not be
     *                                      changed during lifecycle of the
     *                                      instance.
     *                                      Recommended to use the {@link HashAlgorithms}
     *                                      class by it's static {@code getInstance()}
     *                                      method.
     */
    public Hasher(EHashAlgorithm algorithm) {

        isDefined = !algorithm.getValue().equals("none");
        this.algorithm = algorithm.getValue();
        charset = DEFAULT_CHARSET;
    }


    /**
     * Parametric constructor for generating instanece of the {@link Hasher}
     * class.
     *
     * @param securityLevel The security level, depending on scale of security
     *                      defined by sorted algorithms.
     *                      Minimum value is 0, maximum is 5. Whenever you use
     *                      other value, it ends up in process in {@link HashAlgorithms}
     *                      class.
     */
    public Hasher(int securityLevel){

        isDefined = securityLevel > 0;
        this.algorithm =
                HashAlgorithms.getInstance().getAlgorithm(securityLevel);
        charset = DEFAULT_CHARSET;
    }


    /**
     * Parametric constructor for generating instanece of the {@link Hasher}
     * class.
     *
     * @param algorithm                     Chosen algorithm. Should not be
     *                                      changed during lifecycle of the
     *                                      instance.
     *                                      Recommended to use the {@link HashAlgorithms}
     *                                      class by it's static {@code getInstance()}
     *                                      method.
     *
     * @param charset                       Charset to be used mostly in the
     *                                      'peppering' and 'depeppering' processes.
     *
     * @throws NoSuchAlgorithmException     When the algorithm is not supported.
     */
    public Hasher(String algorithm, char[] charset) throws NoSuchAlgorithmException {

        isDefined = checkAlg(algorithm);
        this.algorithm = algorithm;
        this.charset = charset;
    }


    /**
     * Parametric constructor for generating instance of the {@link Hasher}
     * class.
     *
     * @param securityLevel The security level, depending on scale of security
     *                      defined by sorted algorithms.
     *                      Minimum value is 0, maximum is 5. Whenever you use
     *                      other value, it ends up in process in {@link HashAlgorithms}
     *                      class.
     *
     * @param charset       Charset to be used mostly in the
     *                      'peppering' and 'depeppering' processes.
     */
    public Hasher(int securityLevel, char[] charset){

        if(securityLevel > 0){
            isDefined = true;
        } else {
            isDefined = false;
        }
        this.algorithm =
                HashAlgorithms.getInstance().getAlgorithm(securityLevel);
        this.charset = charset;
    }

    /* *******************************************************/
    /* Instance methods **************************************/


    /**
     * Method for generating hashing from data.
     *
     * <b>Not secure because it doesn't use any salt or pepper!</b>
     *
     * @param data                      String data to be hashed
     *
     * @return                          String-formed hashing of the data
     *
     * @throws NoSuchAlgorithmException When there is no such algorithm
     *                                  in supported ones.
     *
     * @deprecated                      <b>Does not use hashing with salt,
     *                                  can be risky.</b>
     */
    public String generateHash(String data) throws NoSuchAlgorithmException {

        if(isDefined) {
            MessageDigest messageDigest = MessageDigest.getInstance(algorithm);
            messageDigest.reset();

            byte[] hash = messageDigest.digest(data.getBytes());
            return bytesToHex(hash);
        } else {
            return data;
        }
    }


    /**
     * Method for generating hashing with salt using data and 'byte array'-formed
     * salt.
     *
     * @param data                      String data to be hashed
     * @param salt                      byte[] of the salt
     *
     * @return                          String-formed hashing of the data
     *
     * @throws NoSuchAlgorithmException When there is no such algorithm
     *                                  in supported ones.
     */
    public String generateHashWithSalt(String data, byte[] salt) throws NoSuchAlgorithmException {

        if(isDefined) {
            MessageDigest messageDigest = MessageDigest.getInstance(algorithm);
            messageDigest.reset();
            messageDigest.update(salt);

            byte[] hash = messageDigest.digest(data.getBytes());

            return bytesToHex(hash);
        } else {
            return data;
        }
    }


    /**
     * Method for generating hashing with salt using data and autogenerated salt
     * salt.
     *
     * @param data                      String data to be hashed
     * @param saltLength                length of the autogenerated salt
     *
     * @return                          String-formed hashing of the data
     *
     * @throws NoSuchAlgorithmException When there is no such algorithm
     *                                  in supported ones.
     */
    public String[] generateHashWithSalt(String data, int saltLength) throws NoSuchAlgorithmException {

        if(isDefined) {
            MessageDigest messageDigest = MessageDigest.getInstance(algorithm);
            messageDigest.reset();
            byte[] salt = generateSalt(saltLength);
            messageDigest.update(salt);

            byte[] hash = messageDigest.digest(data.getBytes());

            String[] result = new String[2];

            result[0] = bytesToHex(hash);
            result[1] = bytesToHex(salt);

            return result;

        } else {
            return new String[]{data, ""};
        }
    }


    /**
     * Method for generating hashing with salt using data and String-formed salt,
     * which is gonna be transformed to byte[] containing salt
     *
     * @param data                      String data to be hashed
     * @param saltString                String to be generated to the salt
     *
     * @return                          String-formed hashing of the data
     *
     * @throws NoSuchAlgorithmException When there is no such algorithm
     *                                  in supported ones.
     */
    public String generateHashWithSalt(String data, String saltString) throws NoSuchAlgorithmException {

        MessageDigest messageDigest = MessageDigest.getInstance(algorithm);
        messageDigest.reset();
        messageDigest.update(generateSalt(saltString));

        byte[] hash = messageDigest.digest(data.getBytes());

        return bytesToHex(hash);
    }


    /**
     * Method for generating hashing with salt using data only. The
     * salt is gonna be autogenerated with lenght of 20 bytes.
     *
     * @param data                      String data to be hashed
     *
     * @return                          String-formed hashing of the data
     *
     * @throws NoSuchAlgorithmException When there is no such algorithm
     *                                  in supported ones.
     */
    public String[] generateHashWithSalt(String data) throws NoSuchAlgorithmException {

        if(isDefined) {
            MessageDigest messageDigest = MessageDigest.getInstance(algorithm);
            messageDigest.reset();
            byte[] salt = generateSalt(20);
            messageDigest.update(salt);

            byte[] hash = messageDigest.digest(data.getBytes());

            String[] result = new String[2];
            result[0] = bytesToHex(hash);
            result[1] = bytesToHex(salt);


            return result;
        }
        else {
            String[] result = new String[2];
            result[0] = data;
            result[1] = bytesToHex(hexToBytes(""));

            return result;
        }
    }


    /**
     * Method for generating hashing with salt and pepper using data,
     * autogenerated salt and pepper. The length of salt is gonna be
     * 20 bytes, the length of pepper is optional.
     *
     * @param data                      String data to be hashed
     * @param length                    length of the pepper
     *                                  <b>The length should not
     *                                  be much longer than 3, because
     *                                  of high computational demand
     *                                  of the validation of hashing with
     *                                  pepper in higher ranks!</b>
     *
     * @return                          String-formed hashing of the data
     *
     * @throws NoSuchAlgorithmException When there is no such algorithm
     *                                  in supported ones.
     */
    public String[] generateHashWithSaltAndPepper(String data, int length) throws NoSuchAlgorithmException {

        data = data + generatePepper(length);
        return generateHashWithSalt(data);
    }

    public String generateHashWithSaltAndPepper(String data, int length, byte[] salt) throws NoSuchAlgorithmException {

        data = data + generatePepper(length);
        return generateHashWithSalt(data, salt);
    }

    /**
     * Checks two hashes using salt only.
     *
     * @param data      Data to be validated against the hashing. This
     *                  String is about to be hashed and the 'hashed'
     *                  value will be compared to the new hashing.
     * @param hashed    Data to be compared against the new hashing build
     *                  from given data.
     * @param salt      Used salt while hashing the 'hashed' value.
     *
     * @return          the boolean value if is or is not valid.
     *
     * @throws NoSuchAlgorithmException When there is no such algorithm supported
     */
    public boolean checkHashWithSalt(String data, String hashed, String salt) throws NoSuchAlgorithmException {

        return hashed.equals(generateHashWithSalt(data, salt));
    }


    /**
     * Checks basic hashing against String-formed data input.
     *
     * @param data      Data to be checked
     * @param hashed    Hashed original data
     *
     * @return          boolean value if is or is not valid.
     *
     * @throws NoSuchAlgorithmException When there is no such algorithm supported
     *
     * @deprecated <b>The hashing without salt is not secure enough and
     *             for this reason should not be used!</b>
     */
    public boolean checkHash(String data, String hashed) throws NoSuchAlgorithmException {

        return hashed.equals(generateHash(data));
    }


    /**
     * Checks two hashes using salt and pepper. First of all generates all
     * possible combinations of data with peppers. Than these are hashed
     * and compared to default hashed value.
     *
     * @param data          Data to be validated against the hashing. This
     *                      String is about to be hashed and the 'hashed'
     *                      value will be compared to the new hashing.
     * @param hashed        Data to be compared against the new hashing build
     *                      from given data.
     * @param salt          Used salt while hashing the 'hashed' value.
     * @param pepperLength  Length of the pepper used while hashing the original
     *                      value. <b>Should not be much longer than 3</b>
     *
     * @return              the boolean value if is or is not valid.
     *
     * @throws NoSuchAlgorithmException When there is no such algorithm supported
     */
    public boolean checkHashWithSaltAndPepper(String data, String hashed, String salt, int pepperLength) throws NoSuchAlgorithmException {

        boolean indicator = false;
        pepperVariations = new ArrayList<>();
        generatePepperVariations("", pepperLength);

        for (String variation : pepperVariations) {

            String newHash = generateHashWithSalt(data + variation, hexToBytes(salt));

            if(newHash.equals(hashed)){
                indicator = true;
                break;
            }
        }

        return indicator;
    }


    /**
     * Generates all possible variations of peppers by given length.
     * <b>Should not be longer than 3, because of algorithm complexity
     * and multidimensional computing.</b>
     *
     * @param length    Length of the pepper
     *
     * @return          The pepper to be used in hashing
     *                  with pepper
     */
    public String generatePepper(int length){

        String pepper = "";

        for(int i = 0; i<length; i++){

            int random = RandomNumberGenerator.getRandomNumberInRange(0, charset.length-1);

            pepper = pepper + (charset[random]);
        }

        return pepper;
    }


    /* *******************************************************/
    /* Static methods ****************************************/


    /**
     * Generates salt with length of given value.
     *
     * @param length    length of final byte[] value
     *
     * @return          byte[] of rendomly generated bytes
     */
    public static byte[] generateSalt(int length){

        byte[] saltBytes = new byte[length];

        SecureRandom secureRandom = new SecureRandom();
        secureRandom.nextBytes(saltBytes);

        return saltBytes;
    }


    /**
     * Generates salt from given String value.
     *
     * @param input     String input
     *
     * @return          byte[] value generated
     *                  from given input.
     */
    public static byte[] generateSalt(String input){

        return hexToBytes(input);
    }


    /**
     * Method for transforming hexadecimal values to
     * byte[].
     *
     * @param bytes     String-formed byte-array, which
     *                  means a bunch of hexadecimal values.
     *
     * @return          byte[] of the given values.
     */
    public static byte[] hexToBytes(String bytes){

        int length = bytes.length();
        byte[] data = new byte[length / 2];

        for (int i = 0; i < length; i += 2) {
            data[i / 2] = (byte) ((Character.digit(bytes.charAt(i), 16) << 4)
                    + Character.digit(bytes.charAt(i+1), 16));
        }

        return data;
    }


    /**
     * Generates String from given byte[].
     *
     * @param bytes byte[] of the hexadecimal values.
     *
     * @return      String-formed value of byte[].
     */
    public static String bytesToHex(byte[] bytes) {

        char[] hexChars = new char[bytes.length * 2];

        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = HEX_ARR[v >>> 4];
            hexChars[j * 2 + 1] = HEX_ARR[v & 0x0F];
        }

        return new String(hexChars);
    }


    /**
     * Checks algorithm to be sure it does what it should.
     *
     * @param algorithmName             Name of the algorithm.
     *                                  It's recommended to use
     *                                  {@link HashAlgorithms} class
     *                                  by {@code getInstance()} method.
     *
     * @return                          boolean by if is chosen hashing alg
     *                                  or not.
     *
     * @throws NoSuchAlgorithmException when there is no such algorithm in
     *                                  the set of supported ones.
     */
    private boolean checkAlg(String algorithmName) throws NoSuchAlgorithmException {

        boolean result;

        switch (algorithmName){

            case ("none"): {
                result = false;
                break;
            }
            case ("MD5"):
            case ("SHA-1"):
            case ("SHA-256"):
            case ("SHA-384"):
            case ("SHA-512"): {
                result = true;
                break;
            }

            default: {
                throw new NoSuchAlgorithmException("No such algorithm as " + algorithmName + " possible.\n" +
                        "The only possible algorithms are 'none', 'MD5', 'SHA-1', 'SHA-256', " +
                        "'SHA-384' or 'SHA-512'.\nOthers are not supported.");
            }
        }
        return result;
    }


    /**
     * Recursively generates all the possible pepper variations
     * from given parameter of length.
     *
     * @param input     String input
     * @param length    the max length of the searched pepper
     */
    private void generatePepperVariations(String input, int length){

        if (input.length() < length) {

            for (char c : charset) {

                String newString = input + c;
                pepperVariations.add(newString);
                generatePepperVariations(newString, length);
            }
        }
    }

    /* *******************************************************/
    /* Getters ***********************************************/


    public String getAlgorithm() {

        return algorithm;
    }

    public boolean isDefined() {

        return isDefined;
    }

    public char[] getCharset() {

        return charset;
    }

    public static char[] getDefaultCharset() {

        return DEFAULT_CHARSET;
    }

    /* *******************************************************/
    /* Setters ***********************************************/

}
