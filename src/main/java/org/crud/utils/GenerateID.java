package org.crud.utils;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * The type Generate id.
 */
public class GenerateID {

  /**
   * Extract id from uri string.
   *
   * @param uri the uri
   * @return the string
   */
  public static String extractIdFromURI(String uri) {
    uri = uri.replaceAll("^/+", "").replaceAll("/+$", "");

    String[] parts = uri.split("/");

    if (parts.length > 0) {
      return parts[parts.length - 1];
    } else {
      return null;
    }
  }

  /**
   * Generate new id string.
   *
   * @param doc the doc
   * @return the string
   */
  public static String generateNewId(Document doc) {
    NodeList records = doc.getElementsByTagName(Constants.ELEMENT_TAG_HEADER);
    int maxId = 0;
    for (int i = 0; i < records.getLength(); i++) {
      Element record = (Element) records.item(i);
      String id = getTextContent(record);
      if (id != null) {
        try {
          int intId = Integer.parseInt(id);
          if (intId > maxId) {
            maxId = intId;
          }
        } catch (NumberFormatException e) {
          return Constants.INTERNAL_ERROR;
        }
      }
    }
    return String.valueOf(maxId + 1);
  }

  /**
   * Remove all child nodes except id.
   *
   * @param element the element
   */
  public static void removeAllChildNodesExceptId(Element element) {
    NodeList childNodes = element.getChildNodes();
    for (int i = childNodes.getLength() - 1; i >= 0; i--) {
      Node child = childNodes.item(i);
      if (!Constants.ELEMENT_TAG_ID.equals(child.getNodeName())) {
        element.removeChild(child);
      }
    }
  }

  /**
   * Append id.
   *
   * @param doc           the doc
   * @param parentElement the parent element
   * @param id            the id
   */
  public static void appendId(Document doc, Element parentElement, String id) {
    Element idElement = doc.createElement(Constants.ELEMENT_TAG_ID);
    idElement.appendChild(doc.createTextNode(id));
    parentElement.appendChild(idElement);
  }


  /**
   * Gets text content.
   *
   * @param element the element
   * @return the text content
   */
  public static String getTextContent(Element element) {
    NodeList nodes = element.getElementsByTagName(Constants.ELEMENT_TAG_ID);
    if (nodes.getLength() > 0) {
      Node node = nodes.item(0);
      if (node != null) {
        return node.getTextContent();
      }
    }
    return null;
  }
}
