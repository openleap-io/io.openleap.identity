package io.openleap.identity.repository;

import io.openleap.identity.model.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfileRepository extends JpaRepository<Profile, Long> {
    Profile findProfileByName(String name);
}
