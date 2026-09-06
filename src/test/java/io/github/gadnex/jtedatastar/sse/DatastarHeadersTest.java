package io.github.gadnex.jtedatastar.sse;

import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.Test;

public class DatastarHeadersTest implements WithAssertions {

  @Test
  void datastarRequestHeader() {
    assertThat(Datastar.REQUEST_HEADER).isEqualTo("Datastar-Request");
  }
}
