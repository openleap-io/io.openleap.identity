package io.openleap.identity.repository;

import io.openleap.identity.model.ClientPrincipal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.NativeQuery;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ClientPrincipalRepository extends JpaRepository<ClientPrincipal, Long> {

    ClientPrincipal findByClientId(@Param("clientId") String clientId);

    @Modifying
    void deleteByInstanceId(@Param("instance_id") String instanceId);

    @NativeQuery("SELECT DISTINCT instance_id FROM client_principal where CAST(client_id_issued_at AS TIMESTAMP) < (CURRENT_TIMESTAMP - INTERVAL '1 DAY')")
    List<String> getAllRegisteredInstances();

    @Modifying
    @NativeQuery("DELETE FROM client_principal WHERE instance_id = :instance")
    void deleteUnusedClientPrincipals(String instance);
}
