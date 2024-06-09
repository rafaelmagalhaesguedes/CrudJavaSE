package org.crud.utils;

import com.google.gson.JsonObject;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class CreateJsonObject {

  public static JsonObject createJson(Element element) {
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
