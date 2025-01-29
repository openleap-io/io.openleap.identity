package io.openleap.identity.repository;

import io.openleap.identity.model.ClientPrincipal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

public interface ClientPrincipalRepository extends JpaRepository<ClientPrincipal, Long> {
    ClientPrincipal findByClientId(@Param("clientId") String clientId);

    ClientPrincipal findByRegistrationId(@Param("registration_id") String registrationId);
}
