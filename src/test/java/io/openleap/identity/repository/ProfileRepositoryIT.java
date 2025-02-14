package io.openleap.identity.repository;

import io.openleap.identity.model.Profile;
import io.openleap.identity.model.Role;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@Transactional
class ProfileRepositoryIT {
    @Autowired
    private ProfileRepository profileRepository;

    @Test
    void givenProfileRepository_whenSaveAndRetreiveEntity_thenOK() {
        //given
        Profile profile = new Profile();
        profile.setName("test-name");
        profile.setEnabled(true);
        profile.setDefaultProfile(true);
        profile.setRoles(Set.of(new Role("test-role")));

        //when
        Profile savedProfile = profileRepository.save(profile);
        Profile retrievedProfile = profileRepository.findById(savedProfile.getId()).get();

        //then
        assertNotNull(retrievedProfile);

        //and
        assertEquals(retrievedProfile.getName(), profile.getName());
        assertEquals(retrievedProfile.getEnabled(), profile.getEnabled());
        assertEquals(retrievedProfile.getDefaultProfile(), profile.getDefaultProfile());
        assertEquals(retrievedProfile.getRoles(), profile.getRoles());
    }

    @Test
    void givenProfileRepository_whenFindProfileByName_thenOK() {
        //given
        Profile profile = new Profile();
        profile.setName("test-name");

        //when
        Profile savedProfile = profileRepository.save(profile);
        Profile retrievedProfile = profileRepository.findProfileByName(savedProfile.getName());

        //then
        assertNotNull(retrievedProfile);

        //and
        assertEquals(retrievedProfile.getName(), savedProfile.getName());
    }

}
