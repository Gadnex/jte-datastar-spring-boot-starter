package io.github.gadnex.jtedatastar;

import java.io.IOException;
import java.util.Set;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public class CapturingSseEmitter extends SseEmitter {
  private final StringBuilder emittedData = new StringBuilder();

  @Override
  public void send(SseEmitter.SseEventBuilder eventBuilder) throws IOException {
    Set<SseEmitter.DataWithMediaType> dataSet = eventBuilder.build();
    for (SseEmitter.DataWithMediaType data : dataSet) {
      emittedData.append(data.getData().toString());
    }
  }

  public String getEmittedData() {
    return emittedData.toString();
  }
}
