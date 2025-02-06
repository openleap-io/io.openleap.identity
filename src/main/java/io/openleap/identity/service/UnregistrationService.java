package io.openleap.identity.service;

import io.openleap.identity.repository.ClientPrincipalRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

@Service
@EnableScheduling
public class UnregistrationService {
    Logger logger = LoggerFactory.getLogger(UnregistrationService.class);
    private final ClientPrincipalRepository clientPrincipalRepository;
    private final DiscoveryClient discoveryClient;

    public UnregistrationService(ClientPrincipalRepository clientPrincipalRepository, DiscoveryClient discoveryClient) {
        this.clientPrincipalRepository = clientPrincipalRepository;
        this.discoveryClient = discoveryClient;
    }

    @Scheduled(cron = "0 */5 * * * *")
    @Transactional
    public void unregisterClients() {
        List<String> registeredInstancesDatabase = clientPrincipalRepository.getAllRegisteredInstances()
                .stream()
                .toList();


        // get all registered applications
        List<String> registeredServices = discoveryClient.getServices();
        logger.debug("Running cleanup job for unused client principals for applications: {}", registeredServices);
        // get all application instances of each application
        var serviceInstances = registeredServices.stream()
                .map(discoveryClient::getInstances).toList();

        var instanceFromService = serviceInstances.stream().flatMap(Collection::stream).toList();

        List<String> registeredInstancesEureka = instanceFromService.stream()
                .map(ServiceInstance::getInstanceId)
                .filter(Objects::nonNull)
                .toList();

        List<String> instancesForRemoval = registeredInstancesDatabase.stream()
                .filter(Predicate.not("static"::equals).and(Predicate.not(""::equals)).and(Predicate.not(registeredInstancesEureka::contains)))
                .distinct()
                .toList();

        logger.debug("Removing unused client principals for instances: {}", instancesForRemoval);
        instancesForRemoval.forEach(clientPrincipalRepository::deleteUnusedClientPrincipals);

    }

    @Transactional
    public void unregisterClientByInstanceId(String instanceId) {
        clientPrincipalRepository.deleteByInstanceId(instanceId);
    }
}
