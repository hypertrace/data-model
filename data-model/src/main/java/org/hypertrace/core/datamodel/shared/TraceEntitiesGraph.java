package org.hypertrace.core.datamodel.shared;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.hypertrace.core.datamodel.Edge;
import org.hypertrace.core.datamodel.Entity;
import org.hypertrace.core.datamodel.StructuredTrace;

public class TraceEntitiesGraph {

  /* entity have many-to-many relationship */
  private final Map<String, List<Entity>> parentToChildrenEntities;
  private final Map<String, List<Entity>> childToParentEntities;

  /* these containers should be unmodifiable after initialization as we're exposing them via getters */
  private Map<String, Entity> entityMap;
  private Set<Entity> rootEntities;

  private TraceEntitiesGraph() {
    this.childToParentEntities = new HashMap<>();
    this.parentToChildrenEntities = new HashMap<>();
  }

  /**
   * Create the instance with or without the full traversal graph includesFullGraph    Includes
   * computing the full traversal graph or not
   */
  public static TraceEntitiesGraph createGraph(StructuredTrace trace) {
    TraceEntitiesGraph graph = new TraceEntitiesGraph();
    graph.buildEntityMap(trace);
    graph.buildParentChildRelationship(trace);
    graph.buildRootEntities(trace);
    return graph;
  }

  /**
   * @return an immutable set containing the root entities
   */
  public Set<Entity> getRootEntities() {
    return rootEntities;
  }


  public List<Entity> getParentEntities(Entity entity) {
    return childToParentEntities.get(entity.getEntityId());
  }


  public List<Entity> getChildrenEntities(Entity entity) {
    return parentToChildrenEntities.get(entity.getEntityId());
  }

  /**
   * @return an immutable map of entity ids to entities
   */
  public Map<String, Entity> getEntityMap() {
    return entityMap;
  }

  private void buildEntityMap(StructuredTrace trace) {
    entityMap = trace.getEntityList().stream()
        .collect(
            Collectors.toUnmodifiableMap(Entity::getEntityId, Function.identity(), (e1, e2) -> e2));
  }

  private void buildParentChildRelationship(StructuredTrace trace) {
    List<Entity> entities = trace.getEntityList();

    // return for now, we don't forsee this is to be null.
    if (entities == null) {
      return;
    }

    List<Edge> entityEdges = trace.getEntityEdgeList();
    if (entityEdges != null) {
      for (Edge entityEdge : trace.getEntityEdgeList()) {
        Integer sourceIndex = entityEdge.getSrcIndex();
        Integer targetIndex = entityEdge.getTgtIndex();
        Entity parentEntity = entities.get(sourceIndex);
        Entity childEntity = entities.get(targetIndex);
        parentToChildrenEntities.computeIfAbsent(parentEntity.getEntityId(), k -> new ArrayList<>())
            .add(childEntity);
        childToParentEntities.computeIfAbsent(childEntity.getEntityId(), k -> new ArrayList<>())
            .add(parentEntity);
      }
    }
  }

  private void buildRootEntities(StructuredTrace trace) {
    // build the root entity ids
    Set<String> rootEntityIds = trace.getEntityList().stream().map(Entity::getEntityId)
        .collect(Collectors.toSet());

    // remove all the children, and what's remaining are the entities without children
    // we will consider these are roots, including the ones that are standalone
    Set<String> childrenEntityIds = parentToChildrenEntities.values().stream()
        .flatMap(l -> l.stream().map(Entity::getEntityId)).collect(Collectors.toSet());
    rootEntityIds.removeAll(childrenEntityIds);

    rootEntities = rootEntityIds.stream().map(entityMap::get)
        .collect(Collectors.toUnmodifiableSet());
  }
}
