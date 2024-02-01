ARG KEYCLOAK_VERSION=23.0.4

# ----------------------- Build du SPI ----------------------- #
FROM maven:3.8.3-openjdk-17 as spi-builder
WORKDIR /spi-build
COPY /keycloak-custom-external-authentication .
RUN mvn clean install -DskipTests
RUN mkdir /output
RUN find . -name '*.jar' -exec cp {} /output/ \;

# Ajout des themes / SPIs
FROM quay.io/keycloak/keycloak:${KEYCLOAK_VERSION} as keycloak-builder
WORKDIR /opt/keycloak
# Copie des thèmes
COPY /themes/ /opt/keycloak/themes/
# Copie des SPI(jar) dans Keycloak
COPY --from=spi-builder /output/ /opt/keycloak/providers/

RUN /opt/keycloak/bin/kc.sh build


# Build de l'image finale
FROM quay.io/keycloak/keycloak:${KEYCLOAK_VERSION}

COPY --from=keycloak-builder /opt/keycloak/ /opt/keycloak/
ENV KC_DB=postgres
ENV USE_FAKE_LEGACY_CONNECTOR=true
ENTRYPOINT ["/opt/keycloak/bin/kc.sh", "start", "--http-enabled=true", "--hostname-strict-https=false"]
