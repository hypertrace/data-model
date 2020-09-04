package org.hypertrace.core.datamodel.shared;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.apache.avro.generic.GenericRecord;

public class ApiNode<T extends GenericRecord> {

  private final T entryApiBoundaryEvent;
  private final List<T> exitApiBoundaryEvents;
  private final T headSpan;
  private final List<T> apiNodeEvents;

  public ApiNode(T headSpan, List<T> apiNodeEvents, T apiEntryEvent, List<T> exitEvents) {
    this.entryApiBoundaryEvent = apiEntryEvent;
    this.exitApiBoundaryEvents = exitEvents;
    this.headSpan = headSpan;
    this.apiNodeEvents = apiNodeEvents;
  }

  /**
   * Returns the first span in the Api trace. This span could be an API entry or an intermediate
   * service or exit span.
   */
  public T getHeadSpan() {
    return headSpan;
  }

  /**
   * Returns an optional API ENTRY span from this Api node. It's optional because, based on the
   * instrumentation, there could be only Exit call from a service and no Entry.
   */
  public Optional<T> getEntryApiBoundaryEvent() {
    return Optional.ofNullable(entryApiBoundaryEvent);
  }

  public List<T> getExitApiBoundaryEvents() {
    return exitApiBoundaryEvents;
  }

  public List<T> getApiNodeEvents() {
    return apiNodeEvents;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ApiNode apiNode = (ApiNode) o;
    return Objects.equals(entryApiBoundaryEvent, apiNode.entryApiBoundaryEvent) &&
        Objects.equals(exitApiBoundaryEvents, apiNode.exitApiBoundaryEvents) &&
        Objects.equals(headSpan, apiNode.headSpan) &&
        Objects.equals(apiNodeEvents, apiNode.apiNodeEvents);
  }

  @Override
  public int hashCode() {
    return Objects.hash(entryApiBoundaryEvent, exitApiBoundaryEvents, headSpan, apiNodeEvents);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("ApiNode{")
        .append("entryApiBoundaryEvent=")
        .append(entryApiBoundaryEvent)
        .append(", exitApiBoundaryEvents=")
        .append(exitApiBoundaryEvents)
        .append(", headSpan=")
        .append(headSpan)
        .append(", apiNodeEvents")
        .append(apiNodeEvents)
        .append('}');
    return sb.toString();
  }
}
