package io.github.gadnex.jtedatastar.csp;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import gg.jte.TemplateEngine;
import io.github.gadnex.jtedatastar.TestApplication;
import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.bind.annotation.GetMapping;

@SpringBootTest(
    classes = {TestApplication.class, CspIntegrationTest.TestConfig.class},
    properties = {"datastar.csp.enabled=true", "datastar.csp.csp-header=true"})
@AutoConfigureMockMvc
class CspIntegrationTest implements WithAssertions {

  @Autowired private MockMvc mockMvc;

  @TestConfiguration(proxyBeanMethods = false)
  static class TestConfig {

    @Bean
    public TemplateEngine templateEngine() {
      return Mockito.mock(TemplateEngine.class);
    }

    @Bean
    public TestController testController() {
      return new TestController();
    }
  }

  @Controller
  static class TestController {
    @GetMapping("/test-csp")
    public String view() {
      return "test-view";
    }
  }

  @Test
  @DisplayName("Should add CSP header to HTTP response and inject matching nonce into model")
  void shouldAddCspHeaderAndModelNonce() throws Exception {
    MvcResult result =
        mockMvc
            .perform(get("/test-csp"))
            .andExpect(status().isOk())
            .andExpect(header().exists("Content-Security-Policy"))
            .andExpect(model().attributeExists("nonce"))
            .andReturn();

    // 1. Extract nonce injected by CspNonceModelAdvice
    String modelNonce = (String) result.getModelAndView().getModel().get("nonce");
    assertThat(modelNonce).isNotBlank();

    // 2. Extract Content-Security-Policy header added by CspHeaderFallbackFilter
    String cspHeader = result.getResponse().getHeader("Content-Security-Policy");
    assertThat(cspHeader)
        .isNotNull()
        .isEqualTo(
            "default-src 'self'; script-src 'self' 'nonce-"
                + modelNonce
                + "'; trusted-types datastar; require-trusted-types-for 'script';");
  }
}
