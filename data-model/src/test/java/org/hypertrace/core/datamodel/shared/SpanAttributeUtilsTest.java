package org.hypertrace.core.datamodel.shared;

import static java.util.Collections.emptyList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Map;
import org.hypertrace.core.datamodel.AttributeValue;
import org.hypertrace.core.datamodel.Attributes;
import org.hypertrace.core.datamodel.Event;
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
}
