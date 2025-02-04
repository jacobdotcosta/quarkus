package io.quarkus.hibernate.validator.test.config;

import static org.junit.jupiter.api.Assertions.assertThrows;

import jakarta.inject.Inject;
import jakarta.validation.constraints.Max;

import org.eclipse.microprofile.config.Config;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import io.quarkus.arc.Unremovable;
import io.quarkus.test.QuarkusUnitTest;
import io.smallrye.config.ConfigMapping;
import io.smallrye.config.ConfigValidationException;
import io.smallrye.config.SmallRyeConfig;

public class ConfigMappingInvalidTest {
    @RegisterExtension
    static final QuarkusUnitTest UNIT_TEST = new QuarkusUnitTest().setArchiveProducer(
            () -> ShrinkWrap.create(JavaArchive.class)
                    .addAsResource(new StringAsset("validator.server.host=localhost\n"), "application.properties"));

    @Inject
    Config config;

    @Test
    void invalid() {
        assertThrows(ConfigValidationException.class,
                () -> config.unwrap(SmallRyeConfig.class).getConfigMapping(Server.class),
                "validator.server.host must be less than or equal to 3");
    }

    @Unremovable
    @ConfigMapping(prefix = "validator.server")
    public interface Server {
        @Max(3)
        String host();
    }
}
