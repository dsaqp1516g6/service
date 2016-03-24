package edu.upc.eetac.dsa.secretsites;

import org.glassfish.jersey.server.ResourceConfig;

/**
 * Created by Marti on 24/03/2016.
 */
public class SecretSitesResourceConfig extends ResourceConfig {

    public SecretSitesResourceConfig() {
        packages("edu.upc.eetac.dsa.secretsites");
    }
}
