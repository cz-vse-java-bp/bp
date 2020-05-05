package cz.vse.java.utils.cryptography.hashing;


import java.util.ArrayList;
import java.util.List;

/**************************************************************
 * <p>Class for defining the possible algorithms available and
 * usable for the hashing process.</p>
 *
 * <p>Possible algorithms are: </p>
 * <ul>
 *   <li>none</li>
 *   <li>MD5</li>
 *   <li>SHA-1</li>
 *   <li>SHA-256</li>
 *   <li>SHA-384</li>
 *   <li>SHA-512</li>
 * </ul>
 *
 * <p>For the possibility of choosing the right one is recommended
 * to use the scale by integer numbering from <b>0 to 5</b>, where
 * 0 stands for none and 5 stands for the strongest (and of course
 * the slowest one).</p>
 *
 * <b>Do not use 'none' option, because it may be unstable.</b>
 *
 * Written for project "Utils".
 * @author Vojtěch Pavlů
 * @version 07. 02. 2020
 *
 * @see <a href="https://en.wikipedia.org/wiki/Hash_function">
 *     Hash function</a> from Wikipedia
 * @see Hasher
 */
public class HashAlgorithms {


    /* *******************************************************/
    /* Instance variables ************************************/

    private List<String> algorithms;

    /* *******************************************************/
    /* Static variables **************************************/

    private static HashAlgorithms hashAlgorithms;

    /* *******************************************************/
    /* Constructors ******************************************/

    /**
     * Private non-parametric constructor used as a core for
     * <i>Singleton</i> design pattern.
     */
    private HashAlgorithms() {

        algorithms = new ArrayList<>();
        algorithms.add("none");
        algorithms.add("MD5");
        algorithms.add("SHA-1");
        algorithms.add("SHA-256");
        algorithms.add("SHA-384");
        algorithms.add("SHA-512");
    }

    /* *******************************************************/
    /* Instance methods **************************************/


    /* *******************************************************/
    /* Static methods ****************************************/

    /**
     * Method for getting the only instance of the class of
     * {@link HashAlgorithms}.
     *
     * <i>Singleton</i> design pattern
     *
     * @return the only instance of this class
     */
    public static HashAlgorithms getInstance(){

        if(hashAlgorithms == null){

            hashAlgorithms = new HashAlgorithms();
        }

        return hashAlgorithms;
    }

    /* *******************************************************/
    /* Getters ***********************************************/


    /**
     * Returns the name of the algorithm. When the given index
     * is not in defined range (0 - 5), returns 'none' algorithm (in
     * case of negative number) or 'SHA-512' (in case of larger number)
     *
     * @param index of the algorithm by the scale of security.
     *              Acceptable values are from 0 to 5, where 0
     *              stands for no hashing algorithm, 5 stands for
     *              the best security (by SHA-512 alg).
     * @return the String formed name of the algorithm.
     */
    public String getAlgorithm(int index){

        if(index < 0){
            return algorithms.get(0);
        } else if(index > 5){
            return algorithms.get(5);
        } else {
            return algorithms.get(index);
        }
    }
}
