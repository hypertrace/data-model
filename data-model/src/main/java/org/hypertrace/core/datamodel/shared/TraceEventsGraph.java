package org.hypertrace.core.datamodel.shared;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.hypertrace.core.datamodel.Edge;
import org.hypertrace.core.datamodel.Event;
import org.hypertrace.core.datamodel.StructuredTrace;

public class TraceEventsGraph {

  /* there could be multiple roots for partial trace and incomplete instrumented app spans */
  private final Map<ByteBuffer, List<Event>> parentToChildrenEvents;
  private final Map<ByteBuffer, Event> childToParentEvents;


  /* these containers should be unmodifiable after initialization as we're exposing them via getters */
  private Map<ByteBuffer, Event> eventMap;
  private Set<Event> rootEvents;

  private TraceEventsGraph() {
    this.childToParentEvents = new HashMap<>();
    this.parentToChildrenEvents = new HashMap<>();
  }

  /**
   * Create the instance with or without the full traversal graph includesFullGraph    Includes
   * computing the full traversal graph or not
   */
  static TraceEventsGraph createGraph(StructuredTrace trace) {
    TraceEventsGraph graph = new TraceEventsGraph();
    graph.buildEventMap(trace);
    graph.buildParentChildRelationship(trace);
    graph.buildRootEvents(trace);
    return graph;
  }

  /**
   * @return an immutable set containing the root events
   */
  public Set<Event> getRootEvents() {
    return rootEvents;
  }


  public Event getParentEvent(Event event) {
    return childToParentEvents.get(event.getEventId());
  }


  public List<Event> getChildrenEvents(Event event) {
    return parentToChildrenEvents.get(event.getEventId());
  }

  private void buildEventMap(StructuredTrace trace) {
    eventMap = trace.getEventList().stream()
        .collect(
            Collectors.toUnmodifiableMap(Event::getEventId, Function.identity(), (e1, e2) -> e2));

  }

  private void buildParentChildRelationship(StructuredTrace trace) {
    List<Event> events = trace.getEventList();
    // there's no graph
    if (events == null) {
      return;
    }

    List<Edge> eventEdges = trace.getEventEdgeList();
    if (eventEdges != null) {
      for (Edge edge : trace.getEventEdgeList()) {
        int sourceIndex = edge.getSrcIndex();
        int targetIndex = edge.getTgtIndex();
        Event parentEvent = events.get(sourceIndex);
        Event childEvent = events.get(targetIndex);
        parentToChildrenEvents.computeIfAbsent(parentEvent.getEventId(), k -> new ArrayList<>())
            .add(childEvent);
        childToParentEvents.put(childEvent.getEventId(), parentEvent);
      }
    }
  }

  private void buildRootEvents(StructuredTrace trace) {
    // build the root event ids
    Set<ByteBuffer> rootEventIds = trace.getEventList().stream().map(Event::getEventId)
        .collect(Collectors.toSet());

    // remove all the children, and what's remaining are the events without children
    // we will consider these are roots, including the ones that are standalone
    Set<ByteBuffer> childrenEventIds = parentToChildrenEvents.values().stream()
        .flatMap(l -> l.stream().map(Event::getEventId)).collect(Collectors.toSet());
    rootEventIds.removeAll(childrenEventIds);

    rootEvents = rootEventIds.stream().map(eventMap::get).collect(Collectors.toUnmodifiableSet());
  }

  /**
   * @return an immutable map of event ids to events
   */
  public Map<ByteBuffer, Event> getEventMap() {
    return eventMap;
  }

  public Map<ByteBuffer, ByteBuffer> getChildIdsToParentIds() {
    return childToParentEvents.entrySet().stream()
        .collect(Collectors.toMap(
            Entry::getKey,
            e -> getEventId(e.getValue())
        ));
  }

  public Map<ByteBuffer, List<ByteBuffer>> getParentToChildEventIds() {
    return parentToChildrenEvents.entrySet().stream()
        .collect(Collectors.toMap(
            Entry::getKey,
            e -> e.getValue().stream().map(this::getEventId).collect(Collectors.toList()))
        );
  }

  private ByteBuffer getEventId(Event event) {
    if (event == null) {
      return null;
    }
    return event.getEventId();
  }
}
