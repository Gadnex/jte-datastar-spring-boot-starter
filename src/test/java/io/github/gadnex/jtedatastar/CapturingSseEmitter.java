package io.github.gadnex.jtedatastar;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public class CapturingSseEmitter extends SseEmitter {
  private final List<String> emittedData = new ArrayList<>();

  @Override
  public void send(SseEmitter.SseEventBuilder eventBuilder) throws IOException {
    Set<SseEmitter.DataWithMediaType> dataSet = eventBuilder.build();
    for (SseEmitter.DataWithMediaType data : dataSet) {
      emittedData.add(data.getData().toString());
    }
  }

  public List<String> getEmittedData() {
    return emittedData;
  }
}
