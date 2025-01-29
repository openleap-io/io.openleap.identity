package io.openleap.identity.service;

import io.openleap.identity.model.ClientPrincipal;
import io.openleap.identity.repository.ClientPrincipalRepository;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class RegistrationService {
    private final ClientPrincipalRepository clientPrincipalRepository;

    public RegistrationService(ClientPrincipalRepository clientPrincipalRepository) {
        this.clientPrincipalRepository = clientPrincipalRepository;
    }

    public ClientPrincipal registerClient(ClientPrincipal cp) {
        ClientPrincipal clientPrincipal = clientPrincipalRepository.findByClientName(cp.getClientName());
        if (clientPrincipal == null) {
            return createAndSaveClient(cp);
        }
        String instanceIds = getInstanceId(clientPrincipal.getClientSettings());
        String instanceId = getInstanceId(cp.getClientSettings());
        if (instanceId == null ||
                (instanceIds != null && Arrays.stream(instanceIds.split(",")).toList().contains(instanceId))) {
            return clientPrincipal;
        }
        if (instanceIds == null) {
            clientPrincipal.setInstanceId(instanceId);
        } else {
            clientPrincipal.setInstanceId(instanceIds.concat(",").concat(instanceId));
        }

        return clientPrincipalRepository.save(clientPrincipal);
    }

    private ClientPrincipal createAndSaveClient(ClientPrincipal cp) {
        ClientPrincipal newClientPrincipal = new ClientPrincipal();
        newClientPrincipal.setDisabled(false);
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
        Pattern pattern = Pattern.compile("instance_id=([^,}]+)");
        Matcher matcher = pattern.matcher(clientSettings);

        if (matcher.find()) {
            return matcher.group(1);
        } else {
            return null;
        }
    }

    public void removeClientByClientName(String clientName, String instanceId) {
        ClientPrincipal clientPrincipal = clientPrincipalRepository.findByClientName(clientName);
        if (clientPrincipal != null && instanceId != null && clientPrincipal.getInstanceId() != null) {
            List<String> newInstanceIds = Arrays.stream(clientPrincipal.getInstanceId().split(",")).filter(id -> !id.equals(instanceId)).toList();
            if (newInstanceIds.isEmpty()) {
                clientPrincipalRepository.delete(clientPrincipal);
            } else {
                clientPrincipal.setInstanceId(String.join(",", newInstanceIds));
                clientPrincipalRepository.save(clientPrincipal);
            }
        }
    }
}
