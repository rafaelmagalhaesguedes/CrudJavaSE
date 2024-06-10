package org.crud.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * The type Request data filter.
 */
public class RequestDataFilter {
  private final Set<String> allowedFields;

  /**
   * Instantiates a new Request data filter.
   *
   * @param allowedFields the allowed fields
   */
  public RequestDataFilter(Set<String> allowedFields) {
    this.allowedFields = allowedFields;
  }

  /**
   * Filter map.
   *
   * @param data the data
   * @return the map
   */
  public Map<String, String> filter(Map<String, String> data) {
    Map<String, String> filteredData = new HashMap<>();
    for (Map.Entry<String, String> entry : data.entrySet()) {
      if (allowedFields.contains(entry.getKey())) {
        filteredData.put(entry.getKey(), entry.getValue());
      }
    }
    return filteredData;
  }

  /**
   * Validate boolean.
   *
   * @param data the data
   * @return the boolean
   */
  public boolean validate(Map<String, String> data) {
    // Regex to validate email
    String r = "^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$";

    if (data.isEmpty()) {
      return false;
    }

    if (!data.containsKey("name") || data.get("name").isEmpty()) {
      return false;
    }

    if (!data.containsKey("email") || !data.get("email").matches(r) || data.get("email").isEmpty()) {
      return false;
    }

    if (!data.containsKey("address") || data.get("address").isEmpty()) {
      return data.containsKey("address") && data.get("address").isEmpty();
    }

    // Check if any unexpected fields are present
    for (String key : data.keySet()) {
      if (!key.equals("name") && !key.equals("email") && !key.equals("address")) {
        return false;
      }
    }

    return true;
  }
}
