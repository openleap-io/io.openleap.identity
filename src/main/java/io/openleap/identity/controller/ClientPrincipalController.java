package io.openleap.identity.controller;

import io.openleap.identity.controller.dto.UnregisterRequest;
import io.openleap.identity.model.ClientPrincipal;
import io.openleap.identity.service.RegistrationService;
import io.openleap.identity.service.UnregistrationService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/registration")
public class ClientPrincipalController {

    private final RegistrationService registrationService;
    private final UnregistrationService unregistrationService;

    public ClientPrincipalController(RegistrationService registrationService, UnregistrationService unregistrationService) {
        this.registrationService = registrationService;
        this.unregistrationService = unregistrationService;
    }

    @PostMapping
    public ClientPrincipal registerClient(@RequestBody ClientPrincipal cp) {
        return registrationService.registerClient(cp);
    }

    @PostMapping("/unregister")
    public void unregisterClient(@RequestBody UnregisterRequest unregisterRequest) {
        unregistrationService.unregisterClientByInstanceId(unregisterRequest.instanceId());
    }

}
