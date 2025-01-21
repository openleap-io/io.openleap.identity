package io.openleap.identity.config;

import io.openleap.identity.model.ClientPrincipal;
import io.openleap.identity.model.Profile;
import io.openleap.identity.model.UserPrincipal;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

@Component
public class Config implements RepositoryRestConfigurer {

    @Override
    public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config, CorsRegistry cors) {
        config.exposeIdsFor(ClientPrincipal.class);
        config.exposeIdsFor(UserPrincipal.class);
        config.exposeIdsFor(Profile.class);
    }


}