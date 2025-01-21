package io.openleap.identity.repository;

import io.openleap.identity.model.UserPrincipal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

public interface UserPrincipalRepository extends JpaRepository<UserPrincipal, Long> {
    UserPrincipal findByUsername(@Param("username") String username);
}
