package org.crud.core;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.crud.utils.Constants;
import org.crud.utils.GenerateID;
import org.crud.utils.JsonManager;
import org.crud.utils.XmlFileManager;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.IOException;
import java.util.Map;

/**
 * The type Crud service.
 */
public class CrudService {
  private final XmlFileManager fileHandler;

  /**
   * Instantiates a new Crud service.
   *
   * @param filePath the file path
   */
  public CrudService(String filePath) {
    this.fileHandler = new XmlFileManager(filePath);
  }

  /**
   * Create record string.
   *
   * @return the string
   * @throws IOException the io exception
   */
  public String createRecord(Map<String, String> data) throws IOException {
    Document doc = fileHandler.loadXml();
    if (doc == null) {
      return Constants.ERROR_LOADING_XML;
    }

    try {
      Element root = doc.getDocumentElement();
      Element newRecord = doc.createElement(Constants.ELEMENT_TAG_HEADER);

      String newId = GenerateID.generateNewId(doc);
      Element idElement = doc.createElement("id");
      idElement.appendChild(doc.createTextNode(newId));
      newRecord.appendChild(idElement);

      for (Map.Entry<String, String> entry : data.entrySet()) {
        Element element = doc.createElement(entry.getKey());
        element.appendChild(doc.createTextNode(entry.getValue()));
        newRecord.appendChild(element);
      }

      root.appendChild(newRecord);
      fileHandler.saveXml(doc);
      return Constants.RECORD_CREATED_SUCCESS;
    } catch (Exception e) {
      return Constants.ERROR_CREATING_RECORD + e.getMessage();
    }
  }

  /**
   * Read records string.
   *
   * @return the string
   */
  public String readRecords() {
    Document doc = fileHandler.loadXml();

    if (doc == null) {
      return Constants.ERROR_LOADING_XML;
    }

    NodeList records = doc.getElementsByTagName(Constants.ELEMENT_TAG_HEADER);
    JsonArray jsonArray = new JsonArray();

    for (int i = 0; i < records.getLength(); i++) {
      Element record = (Element) records.item(i);
      JsonObject jsonObject = JsonManager.createJsonObject(record);
      jsonArray.add(jsonObject);
    }

    return jsonArray.toString();
  }

  /**
   * Update record string.
   *
   * @param idToUpdate the id to update
   * @return the string
   * @throws IOException the io exception
   */
  public String updateRecord(Map<String, String> data, String idToUpdate) throws IOException {
    Document doc = fileHandler.loadXml();

    if (doc == null) {
      return Constants.ERROR_LOADING_XML;
    }

    try {
      NodeList records = doc.getElementsByTagName(Constants.ELEMENT_TAG_HEADER);
      for (int i = 0; i < records.getLength(); i++) {
        Element record = (Element) records.item(i);
        String id = GenerateID.getTextContent(record);
        assert id != null;
        if (id.equals(idToUpdate)) {
          GenerateID.removeAllChildNodesExceptId(record);
          for (Map.Entry<String, String> entry : data.entrySet()) {
            Element element = doc.createElement(entry.getKey());
            element.appendChild(doc.createTextNode(entry.getValue()));
            record.appendChild(element);
          }
          fileHandler.saveXml(doc);
          return Constants.RECORD_UPDATED_SUCCESS;
        }
      }
      return Constants.RECORD_NOT_FOUND;
    } catch (Exception e) {
      return Constants.ERROR_UPDATING_RECORD + e.getMessage();
    }
  }

  /**
   * Delete record string.
   *
   * @param idToDelete the id to delete
   * @return the string
   * @throws IOException the io exception
   */
  public String deleteRecord(String idToDelete) throws IOException {
    Document doc = fileHandler.loadXml();

    if (doc == null) {
      return Constants.ERROR_LOADING_XML;
    }

    try {
      NodeList records = doc.getElementsByTagName(Constants.ELEMENT_TAG_HEADER);
      Element root = doc.getDocumentElement();

      for (int i = 0; i < records.getLength(); i++) {
        Element record = (Element) records.item(i);
        String id = GenerateID.getTextContent(record);

        assert id != null;

        if (id.equals(idToDelete)) {
          root.removeChild(record);
          fileHandler.saveXml(doc);
          return Constants.RECORD_DELETED_SUCCESS;
        }
      }

      return Constants.RECORD_NOT_FOUND;
    } catch (Exception e) {
      return Constants.ERROR_DELETING_RECORD + e.getMessage();
    }
  }
}
