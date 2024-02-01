package fr.liksi.keycloak.spi.userstorage;

import fr.liksi.keycloak.spi.external.LegacyConnectorFake;
import fr.liksi.keycloak.spi.external.LegacyConnectorImpl;
import org.keycloak.component.ComponentModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.storage.UserStorageProviderFactory;

import java.util.Objects;

public class LegacyUserStorageProviderFactory implements UserStorageProviderFactory<LegacyUserStorageProvider> {

    private static final String PROVIDER_ID = "legacy-user-storage-provider";
    private static final String ENV_USE_FAKE_LEGACY_CONNECTOR = "USE_FAKE_LEGACY_CONNECTOR";

    @Override
    public LegacyUserStorageProvider create(final KeycloakSession keycloakSession, final ComponentModel componentModel) {
        final var shouldUseFakeConnector = System.getenv(ENV_USE_FAKE_LEGACY_CONNECTOR);
        if (Objects.nonNull(shouldUseFakeConnector) && Boolean.TRUE.equals(Boolean.valueOf(shouldUseFakeConnector))) {
            return new LegacyUserStorageProvider(keycloakSession, componentModel, LegacyConnectorFake.create());
        } else {
            return new LegacyUserStorageProvider(keycloakSession, componentModel, LegacyConnectorImpl.create());
        }
    }

    @Override
    public String getId() {
        return PROVIDER_ID;
    }
}
