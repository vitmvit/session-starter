package by.vitikova.session.config;

import by.vitikova.session.processor.SessionManagementBeanPostProcessor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

public class AutoConfigurationTest {

    private final ApplicationContextRunner contextRunner = new ApplicationContextRunner();

    @Test
    public void shouldCreateBPPBean() {
        contextRunner.withPropertyValues("session.enable=true")
                .withUserConfiguration(SessionAutoConfiguration.class)
                .run(context ->
                        assertThat(context).hasSingleBean(SessionManagementBeanPostProcessor.class)
                );
    }

    @Test
    public void shouldNotCreateBPPBean() {
        contextRunner.withPropertyValues("session.enable=false")
                .withUserConfiguration(SessionAutoConfiguration.class)
                .run(context ->
                        assertThat(context).doesNotHaveBean(SessionManagementBeanPostProcessor.class)
                );
    }
}