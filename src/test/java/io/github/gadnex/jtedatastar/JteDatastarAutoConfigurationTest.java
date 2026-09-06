package io.github.gadnex.jtedatastar;

import gg.jte.TemplateEngine;
import io.github.gadnex.jtedatastar.csp.CspHeaderFallbackFilter;
import io.github.gadnex.jtedatastar.csp.CspNonceModelAdvice;
import io.github.gadnex.jtedatastar.csp.CspRequestNonceFilter;
import io.github.gadnex.jtedatastar.sse.Datastar;
import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.autoconfigure.context.MessageSourceAutoConfiguration;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.context.runner.WebApplicationContextRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.security.web.SecurityFilterChain;

class JteDatastarAutoConfigurationTest implements WithAssertions {

  private final WebApplicationContextRunner contextRunner =
      new WebApplicationContextRunner()
          .withConfiguration(
              AutoConfigurations.of(
                  JteDatastarAutoConfiguration.class, MessageSourceAutoConfiguration.class))
          .withUserConfiguration(RequiredDependenciesConfig.class);

  @TestConfiguration(proxyBeanMethods = false)
  static class RequiredDependenciesConfig {

    @Bean
    public TemplateEngine templateEngine() {
      return Mockito.mock(TemplateEngine.class);
    }
  }

  @Nested
  @DisplayName("Test Datastar Bean creation")
  class DatastarBeanCreationTests {

    @Test
    @DisplayName("Should create by default")
    void shouldCreateByDefault() {
      contextRunner.run(context -> assertThat(context).hasSingleBean(Datastar.class));
    }

    @Test
    @DisplayName("Should not create Datastar bean when Datastar bean already exists")
    void shouldNotCreateWhenDatastarBeanExists() {
      Datastar customDatastar = Mockito.mock(Datastar.class);

      contextRunner
          .withBean("customDatastar", Datastar.class, () -> customDatastar)
          .run(
              context -> {
                assertThat(context.getBeanNamesForType(Datastar.class))
                    .containsExactly("customDatastar");
              });
    }

    @Test
    @DisplayName("Should create when datastar.sse.enabled=true")
    void shouldCreateWhenEnabled() {
      contextRunner
          .withPropertyValues("datastar.sse.enabled=true")
          .run(context -> assertThat(context).hasSingleBean(Datastar.class));
    }

    @Test
    @DisplayName("Should not create when datastar.sse.enabled=false")
    void shouldNotCreateWhenDisabled() {
      contextRunner
          .withPropertyValues("datastar.sse.enabled=false")
          .run(context -> assertThat(context).doesNotHaveBean(Datastar.class));
    }
  }

  @Nested
  @DisplayName("Test CspRequestNonceFilter Bean creation")
  class CspRequestNonceFilterBeanCreationTests {

    @Test
    @DisplayName("Should not create by default")
    void shouldNotCreateCspRequestNonceFilterByDefault() {
      contextRunner.run(
          context -> assertThat(context).doesNotHaveBean(CspRequestNonceFilter.class));
    }

    @Test
    @DisplayName("Should create when datastar.csp.enabled=true")
    void shouldCreateCspRequestNonceFilterWhenEnabled() {
      contextRunner
          .withPropertyValues("datastar.csp.enabled=true")
          .run(context -> assertThat(context).hasSingleBean(CspRequestNonceFilter.class));
    }

    @Test
    @DisplayName("Should not create when datastar.csp.enabled=false")
    void shouldNotCreateCspRequestNonceFilterWhenDisabled() {
      contextRunner
          .withPropertyValues("datastar.csp.enabled=false")
          .run(context -> assertThat(context).doesNotHaveBean(CspRequestNonceFilter.class));
    }
  }

  @Nested
  @DisplayName("Test CspHeaderFallbackFilter Bean creation")
  class CspHeaderFallbackFilterTests {

    @Test
    @DisplayName("Should not create by default")
    void shouldNotCreateCspResponseHeaderByDefault() {
      contextRunner.run(
          context -> assertThat(context).doesNotHaveBean(CspHeaderFallbackFilter.class));
    }

    @Test
    @DisplayName("Should not when only datastar.csp.enabled=true")
    void shouldNotCreateCspResponseHeaderWhenOnlyCspEnabled() {
      contextRunner
          .withPropertyValues("datastar.csp.enabled=true")
          .run(context -> assertThat(context).doesNotHaveBean(CspHeaderFallbackFilter.class));
    }

    @Test
    @DisplayName("Should not when only datastar.csp.enabled=false")
    void shouldNotCreateCspResponseHeaderWhenOnlyCspDisabled() {
      contextRunner
          .withPropertyValues("datastar.csp.enabled=false")
          .run(context -> assertThat(context).doesNotHaveBean(CspHeaderFallbackFilter.class));
    }

    @Test
    @DisplayName("Should not when only datastar.csp.csp-header=true")
    void shouldNotCreateCspResponseHeaderOnlyCspHeaderEnabled() {
      contextRunner
          .withPropertyValues("datastar.csp.csp-header=true")
          .run(context -> assertThat(context).doesNotHaveBean(CspHeaderFallbackFilter.class));
    }

    @Test
    @DisplayName("Should not when only datastar.csp.csp-header=false")
    void shouldNotCreateCspResponseHeaderOnlyCspHeaderDisabled() {
      contextRunner
          .withPropertyValues("datastar.csp.csp-header=false")
          .run(context -> assertThat(context).doesNotHaveBean(CspHeaderFallbackFilter.class));
    }

    @Test
    @DisplayName("Should create when datastar.csp.enabled=false and datastar.csp.csp-header=false")
    void shouldNotCreateCspResponseHeaderWhenCspDisabledAndCspHeaderDisabled() {
      contextRunner
          .withPropertyValues("datastar.csp.enabled=false", "datastar.csp.csp-header=false")
          .run(context -> assertThat(context).doesNotHaveBean(CspHeaderFallbackFilter.class));
    }

    @Test
    @DisplayName("Should create when datastar.csp.enabled=false and datastar.csp.csp-header=true")
    void shouldNotCreateCspResponseHeaderWhenCspDisabledAndCspHeaderEnabled() {
      contextRunner
          .withPropertyValues("datastar.csp.enabled=false", "datastar.csp.csp-header=true")
          .run(context -> assertThat(context).doesNotHaveBean(CspHeaderFallbackFilter.class));
    }

    @Test
    @DisplayName("Should create when datastar.csp.enabled=true and datastar.csp.csp-header=false")
    void shouldNotCreateCspResponseHeaderWhenCspEnabledAndCspHeaderDisabled() {
      contextRunner
          .withPropertyValues("datastar.csp.enabled=true", "datastar.csp.csp-header=false")
          .run(context -> assertThat(context).doesNotHaveBean(CspHeaderFallbackFilter.class));
    }

    @Test
    @DisplayName("Should create when datastar.csp.enabled=true and datastar.csp.csp-header=true")
    void shouldCreateCspResponseHeaderWhenCspEnabledAndCspHeaderEnabled() {
      contextRunner
          .withPropertyValues("datastar.csp.enabled=true", "datastar.csp.csp-header=true")
          .run(context -> assertThat(context).hasSingleBean(CspHeaderFallbackFilter.class));
    }

    @Test
    @DisplayName(
        "Should not create when datastar.csp.enabled=true and datastar.csp.csp-header=true and Spring Security on classpath")
    void shouldNotCreateCspResponseHeaderWhenSpringSecurityOnClasspath() {
      SecurityFilterChain mockFilterChain = Mockito.mock(SecurityFilterChain.class);

      contextRunner
          .withBean("springSecurityFilterChain", SecurityFilterChain.class, () -> mockFilterChain)
          .withPropertyValues("datastar.csp.enabled=true", "datastar.csp.csp-header=true")
          .run(context -> assertThat(context).doesNotHaveBean(CspHeaderFallbackFilter.class));
    }
  }

  @Nested
  @DisplayName("Test CspNonceModelAdvice Bean creation")
  class CspNonceModelAdviceTests {

    @Test
    @DisplayName("Should not create by default")
    void shouldNotCreateCspNonceModelAdviceByDefault() {
      contextRunner.run(context -> assertThat(context).doesNotHaveBean(CspNonceModelAdvice.class));
    }

    @Test
    @DisplayName("Should create when datastar.csp.enabled=true")
    void shouldCreateCspNonceModelAdviceWhenCspEnabled() {
      contextRunner
          .withPropertyValues("datastar.csp.enabled=true")
          .run(context -> assertThat(context).hasSingleBean(CspNonceModelAdvice.class));
    }

    @Test
    @DisplayName("Should not create when datastar.csp.enabled=false")
    void shouldNotCreateCspNonceModelAdviceWhenCspDisabled() {
      contextRunner
          .withPropertyValues("datastar.csp.enabled=false")
          .run(context -> assertThat(context).doesNotHaveBean(CspNonceModelAdvice.class));
    }
  }
}
