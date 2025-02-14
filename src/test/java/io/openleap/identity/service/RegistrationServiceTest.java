package io.openleap.identity.service;

import io.openleap.identity.model.ClientPrincipal;
import io.openleap.identity.repository.ClientPrincipalRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

public class RegistrationServiceTest {

    @Mock
    private ClientPrincipalRepository clientPrincipalRepository;

    @InjectMocks
    private RegistrationService registrationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegisterClient_NewClient() {
        ClientPrincipal cp = new ClientPrincipal();
        cp.setClientId("new-client-id");
        when(clientPrincipalRepository.findByClientId("new-client-id")).thenReturn(null);
        when(clientPrincipalRepository.save(any(ClientPrincipal.class))).thenReturn(cp);

        ClientPrincipal result = registrationService.registerClient(cp);

        assertNotNull(result);
        assertEquals("new-client-id", result.getClientId());
        verify(clientPrincipalRepository, times(1)).findByClientId("new-client-id");
        verify(clientPrincipalRepository, times(1)).save(any(ClientPrincipal.class));
    }

    @Test
    void testRegisterClient_ExistingClient() {
        ClientPrincipal cp = new ClientPrincipal();
        cp.setClientId("existing-client-id");
        when(clientPrincipalRepository.findByClientId("existing-client-id")).thenReturn(cp);

        ClientPrincipal result = registrationService.registerClient(cp);

        assertNotNull(result);
        assertEquals("existing-client-id", result.getClientId());
        verify(clientPrincipalRepository, times(1)).findByClientId("existing-client-id");
        verify(clientPrincipalRepository, never()).save(any(ClientPrincipal.class));
    }

    @Test
    void testCreateAndSaveClient() {
        ClientPrincipal cp = new ClientPrincipal();
        cp.setClientId("client-id");
        cp.setRegistrationId("456");
        cp.setInstanceId("123");
        cp.setClientSettings("instance_id=123,registration_id=456");
        when(clientPrincipalRepository.save(any(ClientPrincipal.class))).thenReturn(cp);

        ClientPrincipal result = registrationService.registerClient(cp);

        assertNotNull(result);
        assertEquals("client-id", result.getClientId());
        assertEquals("123", result.getInstanceId());
        assertEquals("456", result.getRegistrationId());
        verify(clientPrincipalRepository, times(1)).save(any(ClientPrincipal.class));
    }
}
