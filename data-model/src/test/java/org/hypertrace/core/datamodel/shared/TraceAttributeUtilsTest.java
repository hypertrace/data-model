package org.hypertrace.core.datamodel.shared;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.hypertrace.core.datamodel.AttributeValue;
import org.hypertrace.core.datamodel.Attributes;
import org.hypertrace.core.datamodel.StructuredTrace;
import org.junit.jupiter.api.Test;

public class TraceAttributeUtilsTest {

  @Test
  public void getListAttributeTest() {
    StructuredTrace mockTrace = mock(StructuredTrace.class);
    assertNull(TraceAttributeUtils.getListAttribute(mockTrace, "someattribute"));

    when(mockTrace.getAttributes()).thenReturn(Attributes.newBuilder().build());
    assertNull(TraceAttributeUtils.getListAttribute(mockTrace, "someattribute"));

    Map<String, AttributeValue> attributeMap = new HashMap<>();
    when(mockTrace.getAttributes())
        .thenReturn(Attributes.newBuilder().setAttributeMap(attributeMap).build());
    assertNull(TraceAttributeUtils.getListAttribute(mockTrace, "someattribute"));

    attributeMap.put(
        "someattribute",
        AttributeValue.newBuilder().setValueList(List.of("somevalue1", "somevalue2")).build());
    assertEquals(
        List.of("somevalue1", "somevalue2"),
        TraceAttributeUtils.getListAttribute(mockTrace, "someattribute"));
  }

  @Test
  public void getStringAttributeTest() {
    StructuredTrace mockTrace = mock(StructuredTrace.class);
    assertNull(TraceAttributeUtils.getStringAttribute(mockTrace, "someattribute"));

    Map<String, AttributeValue> attributeMap = new HashMap<>();
    when(mockTrace.getAttributes())
        .thenReturn(Attributes.newBuilder().setAttributeMap(attributeMap).build());
    assertNull(TraceAttributeUtils.getStringAttribute(mockTrace, "someattribute"));

    attributeMap.put("someattribute", AttributeValue.newBuilder().setValue("somevalue").build());
    assertEquals("somevalue", TraceAttributeUtils.getStringAttribute(mockTrace, "someattribute"));
  }

  @Test
  public void getBooleanAttributeTest() {
    StructuredTrace mockTrace = mock(StructuredTrace.class);
    assertFalse(TraceAttributeUtils.getBooleanAttribute(mockTrace, "someattribute"));

    Map<String, AttributeValue> attributeMap = new HashMap<>();
    when(mockTrace.getAttributes())
        .thenReturn(Attributes.newBuilder().setAttributeMap(attributeMap).build());
    assertFalse(TraceAttributeUtils.getBooleanAttribute(mockTrace, "someattribute"));

    attributeMap.put("someattribute", AttributeValue.newBuilder().setValue("true").build());
    assertTrue(TraceAttributeUtils.getBooleanAttribute(mockTrace, "someattribute"));

    attributeMap.put(
        "someOtherAttribute", AttributeValue.newBuilder().setValue("xyz").build());
    assertFalse(TraceAttributeUtils.getBooleanAttribute(mockTrace, "someOtherAttribute"));

    attributeMap.put("otherAttribute", AttributeValue.newBuilder().setValue("").build());
    assertFalse(TraceAttributeUtils.getBooleanAttribute(mockTrace, "otherAttribute"));
  }
}
