package io.openleap.identity.service;

import io.openleap.identity.repository.ClientPrincipalRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;

import java.util.List;

import static org.mockito.Mockito.*;

public class UnregistrationServiceTest {


    @Mock
    private ClientPrincipalRepository clientPrincipalRepository;

    @Mock
    private DiscoveryClient discoveryClient;

    @InjectMocks
    private UnregistrationService unregistrationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testUnregisterClients() {
        List<String> registeredInstancesDatabase = List.of("instance1", "instance2", "static");
        List<String> registeredServices = List.of("service1", "service2");
        ServiceInstance instance1 = mock(ServiceInstance.class);
        ServiceInstance instance2 = mock(ServiceInstance.class);

        when(clientPrincipalRepository.getAllRegisteredInstances()).thenReturn(registeredInstancesDatabase);
        when(discoveryClient.getServices()).thenReturn(registeredServices);
        when(discoveryClient.getInstances("service1")).thenReturn(List.of(instance1));
        when(discoveryClient.getInstances("service2")).thenReturn(List.of(instance2));
        when(instance1.getInstanceId()).thenReturn("instance1");
        when(instance2.getInstanceId()).thenReturn("instance3");

        unregistrationService.unregisterClients();

        verify(clientPrincipalRepository, times(1)).deleteUnusedClientPrincipals("instance2");
    }

    @Test
    void testUnregisterClientByInstanceId() {
        String instanceId = "instance1";

        unregistrationService.unregisterClientByInstanceId(instanceId);

        verify(clientPrincipalRepository, times(1)).deleteByInstanceId(instanceId);
    }
}
