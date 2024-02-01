package fr.liksi.keycloak.spi.external;

import org.keycloak.models.KeycloakSession;

public interface LegacyConnector {
    LegacyLoginResponse getUserByUsername(KeycloakSession keycloakSession, String account, String login) throws Exception;
    LegacyLoginResponse validateUserPassword(KeycloakSession keycloakSession, String userId, String password) throws Exception;
    LegacyLoginResponse getUserById(KeycloakSession keycloakSession, String userId) throws Exception;
}
