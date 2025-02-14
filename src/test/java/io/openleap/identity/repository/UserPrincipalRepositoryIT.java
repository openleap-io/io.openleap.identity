package io.openleap.identity.repository;

import io.openleap.identity.model.Language;
import io.openleap.identity.model.Profile;
import io.openleap.identity.model.Salutation;
import io.openleap.identity.model.UserPrincipal;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@Transactional
class UserPrincipalRepositoryIT {
    @Autowired
    private UserPrincipalRepository userPrincipalRepository;

    @Test
    void givenUserPrincipalRepository_whenSaveAndRetreiveEntity_thenOK() {
        //given
        UserPrincipal userPrincipal = new UserPrincipal();
        userPrincipal.setDisabled(false);
        userPrincipal.setLocked(false);
        userPrincipal.setUsername("test-username");
        userPrincipal.setPassword("test-password");
        userPrincipal.setEmail("test-email");
        userPrincipal.setLanguage(Language.ENGLISH);
        userPrincipal.setFirstName("test-first-name");
        userPrincipal.setLastName("test-last-name");
        userPrincipal.setSalutation(Salutation.MR);
        userPrincipal.setProfiles(Set.of(new Profile()));

        //when
        UserPrincipal savedUserPrincipal = userPrincipalRepository.save(userPrincipal);
        UserPrincipal retrievedUserPrincipal = userPrincipalRepository.findById(savedUserPrincipal.getId()).get();

        //then
        assertNotNull(retrievedUserPrincipal);

        //and
        assertEquals(retrievedUserPrincipal.getDisabled(), userPrincipal.getDisabled());
        assertEquals(retrievedUserPrincipal.getLocked(), userPrincipal.getLocked());
        assertEquals(retrievedUserPrincipal.getUsername(), userPrincipal.getUsername());
        assertEquals(retrievedUserPrincipal.getPassword(), userPrincipal.getPassword());
        assertEquals(retrievedUserPrincipal.getEmail(), userPrincipal.getEmail());
        assertEquals(retrievedUserPrincipal.getLanguage(), userPrincipal.getLanguage());
        assertEquals(retrievedUserPrincipal.getFirstName(), userPrincipal.getFirstName());
        assertEquals(retrievedUserPrincipal.getLastName(), userPrincipal.getLastName());
        assertEquals(retrievedUserPrincipal.getSalutation(), userPrincipal.getSalutation());
        assertEquals(retrievedUserPrincipal.getProfiles(), userPrincipal.getProfiles());
    }

    @Test
    void givenUserPrincipalRepository_whenFindByUsername_thenOK() {
        //given
        UserPrincipal userPrincipal = new UserPrincipal();
        userPrincipal.setUsername("test-username");

        //when
        UserPrincipal savedUserPrincipal = userPrincipalRepository.save(userPrincipal);
        UserPrincipal retrievedUserPrincipal = userPrincipalRepository.findByUsername(savedUserPrincipal.getUsername());

        //then
        assertNotNull(retrievedUserPrincipal);

        //and
        assertEquals(retrievedUserPrincipal.getUsername(), savedUserPrincipal.getUsername());
    }

}
