package org.crud.utils;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.util.Map;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * The type Json manager.
 */
public class JsonManager {

  /**
   * Append new data from json object.
   *
   * @param doc           the doc
   * @param parentElement the parent element
   * @param jsonObject    the json object
   */
  public static void appendNewDataFromJsonObject(Document doc, Element parentElement, JsonObject jsonObject) {
    for (Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
      String key = entry.getKey();
      String value = entry.getValue().getAsString();
      Element element = doc.createElement(key);
      element.appendChild(doc.createTextNode(value));
      parentElement.appendChild(element);
    }
  }

  /**
   * Create json object json object.
   *
   * @param element the element
   * @return the json object
   */
  public static JsonObject createJsonObject(Element element) {
    JsonObject jsonObject = new JsonObject();

    NodeList childNodes = element.getChildNodes();
    for (int j = 0; j < childNodes.getLength(); j++) {
      Node childNode = childNodes.item(j);
      if (childNode.getNodeType() == Node.ELEMENT_NODE) {
        Element childElement = (Element) childNode;
        String tagName = childElement.getTagName();
        String textContent = childElement.getTextContent();
        jsonObject.addProperty(tagName, textContent);
      }
    }

    return jsonObject;
  }
}
