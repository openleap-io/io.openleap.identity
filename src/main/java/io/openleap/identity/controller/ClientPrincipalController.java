package io.openleap.identity.controller;

import io.openleap.identity.model.ClientPrincipal;
import io.openleap.identity.service.RegistrationService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/registration")
public class ClientPrincipalController {

    private final RegistrationService registrationService;

    public ClientPrincipalController(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @PostMapping()
    public ClientPrincipal registerClient(@RequestBody ClientPrincipal cp) {
        return registrationService.registerClient(cp);
    }


    @DeleteMapping("/{registrationId}/{instanceId}")
    public void removeClientByRegistrationId(@PathVariable String registrationId, @PathVariable String instanceId) {
        registrationService.removeClientByClientName(registrationId, instanceId);
    }
}
