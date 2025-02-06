package io.openleap.identity.repository;

import io.openleap.identity.model.ClientPrincipal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.NativeQuery;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ClientPrincipalRepository extends JpaRepository<ClientPrincipal, Long> {
    ClientPrincipal findByClientId(@Param("clientId") String clientId);

    ClientPrincipal findByRegistrationId(@Param("registration_id") String registrationId);


    @Modifying
    void deleteByInstanceId(@Param("instance_id") String instanceId);

    @NativeQuery("SELECT instance_id FROM client_principal")
    List<String> getAllRegisteredInstances();

    @Modifying
    @NativeQuery("DELETE FROM client_principal WHERE instance_id = :instance")
    void deleteUnusedClientPrincipals(String instance);
}
