package org.hypertrace.core.datamodel.shared;

import java.nio.ByteBuffer;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.hypertrace.core.datamodel.Event;
import org.hypertrace.core.datamodel.StructuredTrace;

/**
 * Builds Event graph, and Entity graph from Structured Trace This is a helper class to make it easy
 * for traversing the tree for Downstream Services
 */
public class StructuredTraceGraph {

  private TraceEventsGraph traceEventsGraph;

  public StructuredTraceGraph(StructuredTrace trace) {
    this.traceEventsGraph = new TraceEventsGraph(trace);
  }

  public void reCreateTraceEventsGraph(StructuredTrace trace) {
    this.traceEventsGraph = new TraceEventsGraph(trace);
  }

  public Set<Event> getRootEvents() {
    return traceEventsGraph.getRootEvents();
  }

  public Event getParentEvent(Event event) {
    return traceEventsGraph.getParentEvent(event);
  }

  public List<Event> getChildrenEvents(Event event) {
    return traceEventsGraph.getChildrenEvents(event);
  }

  public Map<ByteBuffer, Event> getEventMap() {
    return traceEventsGraph.getEventMap();
  }

  public Map<ByteBuffer, ByteBuffer> getChildIdsToParentIds() {
    return traceEventsGraph.getChildIdsToParentIds();
  }

  public Map<ByteBuffer, List<ByteBuffer>> getParentToChildEventIds() {
    return traceEventsGraph.getParentToChildEventIds();
  }

  public TraceEventsGraph getTraceEventsGraph() {
    return traceEventsGraph;
  }
}
