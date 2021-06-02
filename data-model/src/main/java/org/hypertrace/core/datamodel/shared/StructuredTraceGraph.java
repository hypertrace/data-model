package org.hypertrace.core.datamodel.shared;

import org.hypertrace.core.datamodel.StructuredTrace;

/**
 * Builds Event graph, and Entity graph from Structured Trace This is a helper class to make it easy
 * for traversing the tree for Downstream Services
 */
public class StructuredTraceGraph {

  private TraceEventsGraph traceEventsGraph;
  private TraceEntitiesGraph traceEntitiesGraph;

  public static StructuredTraceGraph createGraph(StructuredTrace trace) {
    StructuredTraceGraph graph = new StructuredTraceGraph();
    graph.traceEventsGraph = TraceEventsGraph.createGraph(trace);
    graph.traceEntitiesGraph = TraceEntitiesGraph.createGraph(trace);

    return graph;
  }

  public static StructuredTraceGraph reCreateTraceEventsGraph(StructuredTrace trace) {
    StructuredTraceGraph graph = new StructuredTraceGraph();
    graph.traceEventsGraph = TraceEventsGraph.createGraph(trace);

    return graph;
  }

  public static StructuredTraceGraph reCreateTraceEntitiesGraph(StructuredTrace trace) {
    StructuredTraceGraph graph = new StructuredTraceGraph();
    graph.traceEntitiesGraph = TraceEntitiesGraph.createGraph(trace);

    return graph;
  }

  public TraceEventsGraph getTraceEventsGraph() {
    return this.traceEventsGraph;
  }

  public TraceEntitiesGraph getTraceEntitiesGraph() {
    return this.traceEntitiesGraph;
  }
}