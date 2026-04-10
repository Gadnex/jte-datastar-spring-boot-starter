package io.github.gadnex.jtedatastar;

import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class JteDatastarAutoConfigurationTest implements WithAssertions {

  @Autowired private Datastar datastar;

  @Test
  void datastarBeanCreated() {
    assertThat(datastar).isNotNull();
  }
}
