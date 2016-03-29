package edu.upc.eetac.dsa.secretsites;

import org.glassfish.jersey.linking.DeclarativeLinkingFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;

/**
 * Created by Marti on 24/03/2016.
 */
public class SecretSitesResourceConfig extends ResourceConfig {

    public SecretSitesResourceConfig() {
        packages("edu.upc.eetac.dsa.secretsites");
        packages("edu.upc.eetac.dsa.secretsites.auth");
        packages("edu.upc.eetac.dsa.beeter.cors");
        register(RolesAllowedDynamicFeature.class);
        register(DeclarativeLinkingFeature.class);
    }
}
