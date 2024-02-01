package fr.liksi.keycloak.spi.userstorage;

import fr.liksi.keycloak.spi.external.LegacyConnector;
import org.keycloak.component.ComponentModel;
import org.keycloak.credential.CredentialInput;
import org.keycloak.credential.CredentialInputValidator;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.UserModel;
import org.keycloak.models.credential.PasswordCredentialModel;
import org.keycloak.storage.StorageId;
import org.keycloak.storage.UserStorageProvider;
import org.keycloak.storage.user.UserLookupProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

public class LegacyUserStorageProvider implements UserStorageProvider, UserLookupProvider, CredentialInputValidator {

    private static final Logger LOG = LoggerFactory.getLogger(LegacyUserStorageProvider.class);

    private final KeycloakSession keycloakSession;
    private final ComponentModel componentModel;
    private final LegacyConnector legacyConnector;

    public LegacyUserStorageProvider(KeycloakSession keycloakSession,
            ComponentModel componentModel,
            LegacyConnector legacyConnector) {
        this.keycloakSession = keycloakSession;
        this.componentModel = componentModel;
        this.legacyConnector = legacyConnector;
    }

    @Override
    public UserModel getUserByUsername(final RealmModel realmModel, final String username) {
        try {
            LOG.info("Get user by username {}", username);
            final var account = keycloakSession.getContext().getAuthenticationSession().getAuthNote("account");
            return CustomUserModel.from(keycloakSession, realmModel, componentModel, legacyConnector.getUserByUsername(keycloakSession, account, username));
        } catch(Exception e) {
            LOG.error("Error while getting user by username {}", username, e);
            return null;
        }
    }

    @Override
    public boolean isValid(final RealmModel realmModel, final UserModel userModel, final CredentialInput credentialInput) {
        try {
            LOG.info("validate credentials {}", userModel.getId());
            // userModel.getId() => storageId : f:componentId:userId
            final var userId = StorageId.externalId(userModel.getId());
            return Objects.nonNull(legacyConnector.validateUserPassword(keycloakSession, userId, credentialInput.getChallengeResponse()));
        } catch(Exception e) {
            LOG.error("Error vaidating credentials {}", userModel.getId(), e);
            return false;
        }
    }
    @Override
    public UserModel getUserById(final RealmModel realmModel, final String id) {
        try {
            LOG.info("Get user by id {}", id);
            // Id => storageId : f:componentId:userId
            final var userId = StorageId.externalId(id);
            return CustomUserModel.from(keycloakSession, realmModel, componentModel, legacyConnector.getUserById(keycloakSession, userId));
        } catch(Exception e) {
            LOG.error("Error while getting user by id {}", id, e);
            return null;
        }
    }

    @Override
    public UserModel getUserByEmail(final RealmModel realmModel, final String s) {
        return null;
    }

    @Override
    public boolean supportsCredentialType(final String s) {
        return PasswordCredentialModel.TYPE.equals(s);
    }

    @Override
    public boolean isConfiguredFor(final RealmModel realmModel, final UserModel userModel, final String s) {
        return true;
    }

    @Override
    public void close() {

    }
}