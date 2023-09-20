package org.hypertrace.core.datamodel.shared;

import static java.util.Collections.emptyList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.hypertrace.core.datamodel.AttributeValue;
import org.hypertrace.core.datamodel.Attributes;
import org.hypertrace.core.datamodel.Event;
import org.hypertrace.core.datamodel.Resource;
import org.hypertrace.core.datamodel.StructuredTrace;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class SpanAttributeUtilsTest {
  @ParameterizedTest
  @ValueSource(strings = {"\"Valid String\"", "[\"Valid String\"]"})
  void testGetStringListAttribute(final String attributeValue) {
    final Event event = mock(Event.class);
    when(event.getAttributes())
        .thenReturn(
            Attributes.newBuilder()
                .setAttributeMap(
                    Map.of(
                        "attributeKey",
                        AttributeValue.newBuilder().setValue(attributeValue).build()))
                .build());
    final List<String> result = SpanAttributeUtils.getStringListAttribute(event, "attributeKey");
    assertEquals(List.of("Valid String"), result);
  }

  @ParameterizedTest
  @ValueSource(strings = {"Invalid JSON", "[]"})
  void testGetStringListAttribute_emptyList(final String attributeValue) {
    final Event event = mock(Event.class);
    when(event.getAttributes())
        .thenReturn(
            Attributes.newBuilder()
                .setAttributeMap(
                    Map.of(
                        "attributeKey",
                        AttributeValue.newBuilder().setValue(attributeValue).build()))
                .build());
    final List<String> result = SpanAttributeUtils.getStringListAttribute(event, "attributeKey");
    assertEquals(emptyList(), result);
  }

  @Test
  void testGetStringListAttribute_multiValuedList() {
    final Event event = mock(Event.class);
    when(event.getAttributes())
        .thenReturn(
            Attributes.newBuilder()
                .setAttributeMap(
                    Map.of(
                        "attributeKey",
                        AttributeValue.newBuilder()
                            .setValue("[\"value1\", \"value2\", 3]")
                            .build()))
                .build());
    final List<String> result = SpanAttributeUtils.getStringListAttribute(event, "attributeKey");
    assertEquals(List.of("value1", "value2", "3"), result);
  }

  @Test
  void testGetResourceStringAttribute() {
    final Event event = mock(Event.class);
    final StructuredTrace trace = mock(StructuredTrace.class);
    Resource resource =
        Resource.newBuilder()
            .setAttributes(
                Attributes.newBuilder()
                    .setAttributeMap(
                        Map.of("key1", AttributeValue.newBuilder().setValue("value1").build()))
                    .build())
            .build();
    when(event.getResourceIndex()).thenReturn(0);
    when(trace.getResourceList()).thenReturn(Collections.singletonList(resource));
    Optional<String> valueAbsent =
        SpanAttributeUtils.getResourceStringAttribute(trace, event, "key2");
    Optional<String> valuePresent =
        SpanAttributeUtils.getResourceStringAttribute(trace, event, "key1");
    assertFalse(valueAbsent.isPresent());
    assertTrue(valuePresent.isPresent());
    assertEquals("value1", valuePresent.get());
  }
}
