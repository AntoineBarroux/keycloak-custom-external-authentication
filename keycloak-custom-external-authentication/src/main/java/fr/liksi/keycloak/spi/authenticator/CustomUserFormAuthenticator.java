package fr.liksi.keycloak.spi.authenticator;

import jakarta.ws.rs.core.MultivaluedMap;
import org.keycloak.authentication.AuthenticationFlowContext;
import org.keycloak.authentication.AuthenticationFlowError;
import org.keycloak.authentication.authenticators.browser.UsernamePasswordForm;

public class CustomUserFormAuthenticator extends UsernamePasswordForm {

    private static final String ACCOUNT_INPUT = "account";

    @Override
    protected boolean validateForm(AuthenticationFlowContext context, MultivaluedMap<String, String> formData) {
        try {
            final var account = formData.getFirst(ACCOUNT_INPUT);
            context.getAuthenticationSession().setAuthNote(ACCOUNT_INPUT, account);
            return super.validateForm(context, formData);
        } catch (Exception e) {
            context.failure(AuthenticationFlowError.INTERNAL_ERROR);
            return false;
        }
    }
}