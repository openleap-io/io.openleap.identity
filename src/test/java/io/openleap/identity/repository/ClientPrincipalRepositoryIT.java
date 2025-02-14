package io.openleap.identity.repository;

import io.openleap.identity.model.ClientPrincipal;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class ClientPrincipalRepositoryIT {
    @Autowired
    private ClientPrincipalRepository clientPrincipalRepository;

    @Test
    void test_givenClientPrincipalRepository_whenSaveAndRetreiveEntity_thenOK() {
        //given
        ClientPrincipal clientPrincipal = new ClientPrincipal();
        clientPrincipal.setRegistrationId("test-registration-id");
        clientPrincipal.setDisabled(false);
        clientPrincipal.setLocked(false);
        clientPrincipal.setClientId("test-client-id");
        clientPrincipal.setClientIdIssuedAt(Instant.from(Instant.now()));
        clientPrincipal.setClientSecret("test-client-secret");
        clientPrincipal.setClientSecretExpiresAt(Instant.from(Instant.now()));
        clientPrincipal.setClientName("test-client-name");
        clientPrincipal.setClientAuthenticationMethods("test-client-authentication-methods");
        clientPrincipal.setAuthorizationGrantTypes("test-authorization-grant-types");
        clientPrincipal.setRedirectUris("test-redirect-uris");
        clientPrincipal.setPostLogoutRedirectUris("test-post-logout-redirect-uris");
        clientPrincipal.setScopes("test-scopes");
        clientPrincipal.setClientSettings("test-client-settings");
        clientPrincipal.setTokenSettings("test-token-settings");
        clientPrincipal.setInstanceId("test-instance-id");

        //when
        ClientPrincipal savedClientPrincipal = clientPrincipalRepository.save(clientPrincipal);
        ClientPrincipal retrievedClientPrincipal = clientPrincipalRepository.findById(savedClientPrincipal.getId()).get();

        //then
        assertNotNull(retrievedClientPrincipal);

        //and
        assertEquals(retrievedClientPrincipal.getRegistrationId(),clientPrincipal.getRegistrationId());
        assertEquals(retrievedClientPrincipal.getDisabled(),clientPrincipal.getDisabled());
        assertEquals(retrievedClientPrincipal.getLocked(),clientPrincipal.getLocked());
        assertEquals(retrievedClientPrincipal.getClientId(),clientPrincipal.getClientId());
        assertEquals(retrievedClientPrincipal.getClientIdIssuedAt(),clientPrincipal.getClientIdIssuedAt());
        assertEquals(retrievedClientPrincipal.getClientSecret(),clientPrincipal.getClientSecret());
        assertEquals(retrievedClientPrincipal.getClientSecretExpiresAt(),clientPrincipal.getClientSecretExpiresAt());
        assertEquals(retrievedClientPrincipal.getClientName(),clientPrincipal.getClientName());
        assertEquals(retrievedClientPrincipal.getClientAuthenticationMethods(),clientPrincipal.getClientAuthenticationMethods());
        assertEquals(retrievedClientPrincipal.getAuthorizationGrantTypes(),clientPrincipal.getAuthorizationGrantTypes());
        assertEquals(retrievedClientPrincipal.getRedirectUris(),clientPrincipal.getRedirectUris());
        assertEquals(retrievedClientPrincipal.getPostLogoutRedirectUris(),clientPrincipal.getPostLogoutRedirectUris());
        assertEquals(retrievedClientPrincipal.getScopes(),clientPrincipal.getScopes());
        assertEquals(retrievedClientPrincipal.getClientSettings(),clientPrincipal.getClientSettings());
        assertEquals(retrievedClientPrincipal.getTokenSettings(),clientPrincipal.getTokenSettings());
        assertEquals(retrievedClientPrincipal.getInstanceId(),clientPrincipal.getInstanceId());
    }

    @Test
    void givenClientPrincipalRepository_whenFindByClientId_thenOK() {
        //given
        ClientPrincipal clientPrincipal = new ClientPrincipal();
        clientPrincipal.setClientId("test-client-id");

        //when
        ClientPrincipal savedClientPrincipal = clientPrincipalRepository.save(clientPrincipal);
        ClientPrincipal retrievedClientPrincipal = clientPrincipalRepository.findByClientId(savedClientPrincipal.getClientId());

        //then
        assertNotNull(retrievedClientPrincipal);

        //and
        assertEquals(retrievedClientPrincipal.getClientId(),clientPrincipal.getClientId());
    }

    @Test
    void givenClientPrincipalRepository_whenDeleteByInstanceId_thenOK() {
        //given
        ClientPrincipal clientPrincipal = new ClientPrincipal();
        clientPrincipal.setClientId("test-client-id");
        clientPrincipal.setInstanceId("test-instance-id");

        //when
        ClientPrincipal savedClientPrincipal = clientPrincipalRepository.save(clientPrincipal);
        clientPrincipalRepository.deleteByInstanceId(savedClientPrincipal.getInstanceId());
        ClientPrincipal retrievedClientPrincipal = clientPrincipalRepository.findByClientId(savedClientPrincipal.getClientId());

        //then
        assertNull(retrievedClientPrincipal);
    }

    @Test
    void givenClientPrincipalRepository_whenGetAllRegisteredInstances_thenOK() {
        //given
        ClientPrincipal clientPrincipal = new ClientPrincipal();
        clientPrincipal.setClientId("test-client-id");
        clientPrincipal.setInstanceId("test-instance-id");

        ClientPrincipal clientPrincipal2 = new ClientPrincipal();
        clientPrincipal2.setClientId("test-client-id2");
        clientPrincipal2.setInstanceId("test-instance-id2");

        ClientPrincipal clientPrincipal3 = new ClientPrincipal();
        clientPrincipal3.setClientId("test-client-id3");
        clientPrincipal3.setInstanceId("test-instance-id");

        ClientPrincipal savedClientPrincipal1 = clientPrincipalRepository.save(clientPrincipal);
        ClientPrincipal savedClientPrincipal2 = clientPrincipalRepository.save(clientPrincipal2);
        ClientPrincipal savedClientPrincipal3 = clientPrincipalRepository.save(clientPrincipal3);

        //when
        List<String> instances = clientPrincipalRepository.getAllRegisteredInstances();

        //then
        assertEquals(3, instances.size()); // 2 + the static default one

        //and
        assertTrue(instances.contains(savedClientPrincipal1.getInstanceId()));
        assertTrue(instances.contains(savedClientPrincipal2.getInstanceId()));
        assertTrue(instances.contains(savedClientPrincipal3.getInstanceId()));
    }

    @Test
    void givenClientPrincipalRepository_whenDeleteUnusedClientPrincipals_thenOK() {
        //given
        ClientPrincipal clientPrincipal = new ClientPrincipal();
        clientPrincipal.setClientId("test-client-id");
        clientPrincipal.setInstanceId("test-instance-id");

        ClientPrincipal clientPrincipal2 = new ClientPrincipal();
        clientPrincipal2.setClientId("test-client-id2");
        clientPrincipal2.setInstanceId("test-instance-id2");

        ClientPrincipal clientPrincipal3 = new ClientPrincipal();
        clientPrincipal3.setClientId("test-client-id3");
        clientPrincipal3.setInstanceId("test-instance-id");

        ClientPrincipal savedClientPrincipal1 = clientPrincipalRepository.save(clientPrincipal);
        ClientPrincipal savedClientPrincipal2 = clientPrincipalRepository.save(clientPrincipal2);
        ClientPrincipal savedClientPrincipal3 = clientPrincipalRepository.save(clientPrincipal3);

        //when
        clientPrincipalRepository.deleteUnusedClientPrincipals("test-instance-id");

        //then
        assertFalse(clientPrincipalRepository.existsById(savedClientPrincipal1.getId()));
        assertTrue(clientPrincipalRepository.existsById(savedClientPrincipal2.getId()));
        assertFalse(clientPrincipalRepository.existsById(savedClientPrincipal3.getId()));
    }
}
