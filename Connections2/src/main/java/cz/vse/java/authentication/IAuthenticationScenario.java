package cz.vse.java.authentication;


/**************************************************************
 * <p>The interface of IAuthenticationScenario is used to contain all methods
 * of the type to be overriden (implemented).</p>
 *
 * <p>Instances implementing this interface are used for providing authentication
 * at many levels for the various {@link cz.vse.java.connections.utils.IConnection}s.
 * Mostly are used in combination with {@link AuthenticationHandlerContainer}.</p>
 *
 * Written for project "Connections2".
 * @author Vojtěch Pavlů
 * @version 03. 04. 2020
 *
 * @see cz.vse.java.authentication
 * @see AuthenticationHandlerContainer
 * @see EAuthenticationScenarioType
 * @see TokenValidator
 * @see UserAuthenticator
 */
public interface IAuthenticationScenario {


    /* *****************************************************************/
    /* Methods to be overriden *****************************************/


    /**
     * <p>Method for resolving the authentication scenario type.</p>
     *
     * @return  the type of authentication scenario
     */
    EAuthenticationScenarioType getAuthScenarioType();


    /* *****************************************************************/
    /* Default methods *************************************************/


}
