package org.hypertrace.core.datamodel.shared;

import java.util.List;
import java.util.Optional;
import org.hypertrace.core.datamodel.AttributeValue;
import org.hypertrace.core.datamodel.Event;
import org.hypertrace.core.datamodel.StructuredTrace;

/** Convenience methods to deal with StructuredTrace attributes. */
public class TraceAttributeUtils {

  public static boolean containsAttribute(StructuredTrace trace, String attribute) {
    return trace.getAttributes() != null
        && trace.getAttributes().getAttributeMap() != null
        && trace.getAttributes().getAttributeMap().containsKey(attribute);
  }

  public static boolean getBooleanAttribute(StructuredTrace trace, String attribute) {
    return Boolean.parseBoolean(getStringAttribute(trace, attribute));
  }

  public static String getStringAttribute(StructuredTrace trace, String attribute) {
    if (trace.getAttributes() == null) {
      return null;
    }

    if (trace.getAttributes().getAttributeMap() == null) {
      return null;
    }

    AttributeValue value = trace.getAttributes().getAttributeMap().get(attribute);
    if (value == null) {
      return null;
    }

    return value.getValue();
  }

  public static List<String> getListAttribute(StructuredTrace trace, String attribute) {
    if (trace.getAttributes() == null) {
      return null;
    }

    if (trace.getAttributes().getAttributeMap() == null) {
      return null;
    }

    AttributeValue value = trace.getAttributes().getAttributeMap().get(attribute);
    if (value == null) {
      return null;
    }

    return value.getValueList();
  }

  public static Optional<String> getAttributeValueIncludeSearchInSpans(
      StructuredTrace trace, String key) {
    if (trace == null) {
      return Optional.empty();
    }

    Optional<String> value =
        AttributeSearch.searchForAttributeIgnoreKeyCase(trace.getAttributes(), key);
    if (value.isPresent()) {
      return value;
    }

    for (Event event : trace.getEventList()) {
      // SpanAttributeUtils.getStringAttributeIgnoreKeyCase searches in both attributes and enriched
      // attributes
      value = SpanAttributeUtils.getStringAttributeIgnoreKeyCase(event, key);
      if (value.isPresent()) {
        return value;
      }
    }
    return value;
  }
}
