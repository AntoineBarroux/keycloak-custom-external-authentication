package fr.liksi.keycloak.spi.userstorage;

import fr.liksi.keycloak.spi.external.LegacyLoginResponse;
import org.keycloak.credential.LegacyUserCredentialManager;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.SubjectCredentialManager;
import org.keycloak.models.UserModel;
import org.keycloak.storage.StorageId;
import org.keycloak.storage.adapter.AbstractUserAdapter;
import org.keycloak.component.ComponentModel;

public class CustomUserModel extends AbstractUserAdapter {
    private String legacyUserId;

    private CustomUserModel(final KeycloakSession session,
            final RealmModel realm,
            final ComponentModel storageProviderModel) {
        super(session, realm, storageProviderModel);
    }

    public static UserModel from(final KeycloakSession keycloakSession, final RealmModel realmModel,
            final ComponentModel componentModel, final LegacyLoginResponse legacyLoginResponse) {
        final var userModel = new CustomUserModel(keycloakSession, realmModel, componentModel);
        userModel.legacyUserId = legacyLoginResponse.userId();
        userModel.storageId = new StorageId(componentModel.getId(), legacyLoginResponse.userId());
        return userModel;
    }

    @Override
    public String getUsername() {
        return this.legacyUserId;
    }

    @Override
    public SubjectCredentialManager credentialManager() {
        return new LegacyUserCredentialManager(session, realm, this);
    }
}