package fr.liksi.keycloak.spi.external;

import org.keycloak.broker.provider.util.SimpleHttp;
import org.keycloak.models.KeycloakSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LegacyConnectorImpl implements LegacyConnector {
    private static final Logger LOGGER = LoggerFactory.getLogger(LegacyConnectorImpl.class);

    private LegacyConnectorImpl() {
    }

    public static LegacyConnectorImpl create() {
        return new LegacyConnectorImpl();
    }

    public LegacyLoginResponse getUserByUsername(KeycloakSession keycloakSession, String account, String login) throws Exception {
        LOGGER.info("Get user by username on legacy API {}, {}", account, login);
        final var uri = "http://localhost:9000/api/users";
        SimpleHttp request = SimpleHttp.doPost(uri, keycloakSession).param("account", account).param("login", login);

        try (SimpleHttp.Response response = request.asResponse()) {
            if (response.getStatus() == 200) {
                return response.asJson(LegacyLoginResponse.class);
            } else {
                LOGGER.warn("An error occured while trying to get user by username. Code HTTP : {} / {}, {}", response.getStatus(), account, login);
                throw new Exception("Unable to authenticate user");
            }
        } catch (Exception e) {
            LOGGER.warn("A technical error occured while trying to get user by username");
            throw new Exception("Technical error while calling legacy API", e);
        }
    }

    public LegacyLoginResponse validateUserPassword(KeycloakSession keycloakSession, String userId, String password) throws Exception {
        LOGGER.info("Validate user password on legacy API {}", userId);
        final var uri = "http://localhost:9000/api/auth";
        SimpleHttp request = SimpleHttp.doPost(uri, keycloakSession).param("userId", userId).param("password", password);

        try (SimpleHttp.Response response = request.asResponse()) {
            if (response.getStatus() == 200) {
                return response.asJson(LegacyLoginResponse.class);

            } else {
                LOGGER.warn("Unable to validate credentials for user {}", userId);
                throw new Exception("Unable to authenticate user");
            }
        } catch (Exception e) {
            LOGGER.warn("Unable to validate credentials for user {}", userId);
            throw new Exception("Technical error while calling legacy API", e);
        }
    }

    public LegacyLoginResponse getUserById(KeycloakSession keycloakSession, String userId) throws Exception {
        LOGGER.info("Get user by id on legacy API {}", userId);
        final var uri = "http://localhost:9000/api/users";
        SimpleHttp request = SimpleHttp.doPost(uri, keycloakSession).param("userId", userId);

        try (SimpleHttp.Response response = request.asResponse()) {
            if (response.getStatus() == 200) {
                return response.asJson(LegacyLoginResponse.class);
            } else {
                LOGGER.warn("An error occured while trying to get user by id. Code HTTP : {} / {}", response.getStatus(), userId);
                throw new Exception("Unable to retrieve user");
            }
        } catch (Exception e) {
            LOGGER.warn("A technical error occured while trying to get user by id");
            throw new Exception("Technical error while calling legacy API", e);
        }
    }
}
