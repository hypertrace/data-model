package org.hypertrace.core.datamodel.shared;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
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
}
