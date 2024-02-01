package fr.liksi.keycloak.spi.external;

import org.keycloak.models.KeycloakSession;

public class LegacyConnectorFake implements LegacyConnector {

    private LegacyConnectorFake() {
    }

    public static LegacyConnectorFake create() {
        return new LegacyConnectorFake();
    }

    @Override
    public LegacyLoginResponse validateUserPassword(final KeycloakSession keycloakSession, final String userId, final String password)
            throws Exception {
        return "password".equals(password) ? new LegacyLoginResponse("1") : null;
    }

    @Override
    public LegacyLoginResponse getUserByUsername(final KeycloakSession keycloakSession, final String account, final String login)
            throws Exception {
        return new LegacyLoginResponse("1");
    }

    @Override
    public LegacyLoginResponse getUserById(final KeycloakSession keycloakSession, final String userId) throws Exception {
        return new LegacyLoginResponse(userId);
    }
}
