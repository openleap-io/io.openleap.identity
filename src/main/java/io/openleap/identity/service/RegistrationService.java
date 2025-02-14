package io.openleap.identity.service;

import io.openleap.identity.model.ClientPrincipal;
import io.openleap.identity.repository.ClientPrincipalRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class RegistrationService {
    Logger logger = LoggerFactory.getLogger(RegistrationService.class);

    private final ClientPrincipalRepository clientPrincipalRepository;


    public RegistrationService(ClientPrincipalRepository clientPrincipalRepository) {
        this.clientPrincipalRepository = clientPrincipalRepository;
    }

    public ClientPrincipal registerClient(ClientPrincipal cp) {
        ClientPrincipal clientPrincipal = clientPrincipalRepository.findByClientId(cp.getClientId());
        if (clientPrincipal == null) {
            logger.debug("Registering new clientId: {}", cp.getClientId());
            return createAndSaveClient(cp);
        }
        logger.debug("Client with existing clientId: {} found. Returning client.", cp.getClientId());
        return clientPrincipal;
    }

    private ClientPrincipal createAndSaveClient(ClientPrincipal cp) {
        ClientPrincipal newClientPrincipal = new ClientPrincipal();
        newClientPrincipal.setRegistrationId(getRegistrationId(cp.getClientSettings()));
        newClientPrincipal.setDisabled(false);
        newClientPrincipal.setClientIdIssuedAt(cp.getClientIdIssuedAt());
        newClientPrincipal.setLocked(false);
        newClientPrincipal.setClientId(cp.getClientId());
        newClientPrincipal.setClientSecret(cp.getClientSecret());
        newClientPrincipal.setClientName(cp.getClientName());
        newClientPrincipal.setClientAuthenticationMethods(cp.getClientAuthenticationMethods());
        newClientPrincipal.setAuthorizationGrantTypes(cp.getAuthorizationGrantTypes());
        newClientPrincipal.setRedirectUris(cp.getRedirectUris());
        newClientPrincipal.setScopes(cp.getScopes());
        newClientPrincipal.setClientSettings(cp.getClientSettings());
        newClientPrincipal.setTokenSettings(cp.getTokenSettings());
        newClientPrincipal.setInstanceId(getInstanceId(cp.getClientSettings()));
        return clientPrincipalRepository.save(newClientPrincipal);
    }

    private String getInstanceId(String clientSettings) {
        if (clientSettings == null) {
            return "static";
        }
        Pattern pattern = Pattern.compile("instance_id=([^,}]+)");
        Matcher matcher = pattern.matcher(clientSettings);

        if (matcher.find()) {
            return matcher.group(1);
        } else {
            return "static";
        }
    }

    private String getRegistrationId(String clientSettings) {
        if (clientSettings == null) {
            return null;
        }
        Pattern pattern = Pattern.compile("registration_id=([^,}]+)");
        Matcher matcher = pattern.matcher(clientSettings);

        if (matcher.find()) {
            return matcher.group(1);
        } else {
            return null;
        }
    }
}
