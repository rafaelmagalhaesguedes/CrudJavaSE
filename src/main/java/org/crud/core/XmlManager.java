package org.crud.core;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.net.httpserver.HttpExchange;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import org.crud.utils.Constants;
import org.crud.utils.CreateJsonObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class XmlManager {
  private final XmlFileHandler fileHandler;

  public XmlManager(String filePath) {
    this.fileHandler = new XmlFileHandler(filePath);
  }

  private Map<String, String> parseRequestBody(HttpExchange exchange) throws IOException {
    // Read the request body
    String requestBody = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);

    // Converts the request body to JSON
    JsonObject jsonObject = JsonParser.parseString(requestBody).getAsJsonObject();

    // Create a map to store the data
    Map<String, String> data = new HashMap<>();

    // Iterates over JSON inputs and dynamically adds to map
    for (Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
      String key = entry.getKey();
      String value = entry.getValue().getAsString();
      data.put(key, value);
    }

    return data;
  }

  public String createRecord(HttpExchange exchange) throws IOException {
    Document doc = fileHandler.loadXml();

    if (doc == null) {
      return Constants.ERROR_LOADING_XML;
    }

    try {
      // Create map to get data
      Map<String, String> data = parseRequestBody(exchange);

      // Create a new record in XML
      Element root = doc.getDocumentElement();
      Element newRecord = doc.createElement(Constants.ELEMENT_HEADER_NAME);

      // Iterates over data and creates XML elements dynamically
      for (Map.Entry<String, String> entry : data.entrySet()) {
        String key = entry.getKey();
        String value = entry.getValue();

        Element element = doc.createElement(key);
        element.appendChild(doc.createTextNode(value));
        newRecord.appendChild(element);
      }

      root.appendChild(newRecord);

      fileHandler.saveXml(doc);
      return Constants.RECORD_CREATED_SUCCESS;
    } catch (Exception e) {
      return Constants.ERROR_CREATING_RECORD + e.getMessage();
    }
  }

  public String readRecords() {
    Document doc = fileHandler.loadXml();

    if (doc == null) {
      return Constants.ERROR_LOADING_XML;
    }

    NodeList records = doc.getElementsByTagName(Constants.ELEMENT_HEADER_NAME);
    JsonArray jsonArray = new JsonArray();

    for (int i = 0; i < records.getLength(); i++) {
      Element record = (Element) records.item(i);
      JsonObject jsonObject = CreateJsonObject.createJson(record);
      jsonArray.add(jsonObject);
    }

    return jsonArray.toString();
  }

  public String updateRecord(HttpExchange exchange) throws IOException {
    Document doc = fileHandler.loadXml();
    if (doc == null) {
      return Constants.ERROR_LOADING_XML;
    }

    try {
      Map<String, String> data = parseRequestBody(exchange);
      String idToUpdate = data.get("id");

      NodeList records = doc.getElementsByTagName(Constants.ELEMENT_HEADER_NAME);

      for (int i = 0; i < records.getLength(); i++) {
        Element record = (Element) records.item(i);
        String id = record.getElementsByTagName("id").item(0).getTextContent();

        if (id.equals(idToUpdate)) {
          // Remove all existing child nodes
          while (record.hasChildNodes()) {
            record.removeChild(record.getFirstChild());
          }

          // Add all new data from the JSON object
          InputStreamReader isr = new InputStreamReader(exchange.getRequestBody());
          BufferedReader br = new BufferedReader(isr);
          StringBuilder jsonBuilder = new StringBuilder();
          String line;
          while ((line = br.readLine()) != null) {
            jsonBuilder.append(line);
          }
          String jsonString = jsonBuilder.toString();
          JsonObject jsonObject = JsonParser.parseString(jsonString).getAsJsonObject();
          for (Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue().getAsString();
            Element element = doc.createElement(key);
            element.appendChild(doc.createTextNode(value));
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

  public String deleteRecord(HttpExchange exchange) throws IOException {
    Document doc = fileHandler.loadXml();
    if (doc == null) {
      return Constants.ERROR_LOADING_XML;
    }

    try {
      Map<String, String> data = parseRequestBody(exchange);
      String idToDelete = data.get("id");

      NodeList records = doc.getElementsByTagName("record");
      Element root = doc.getDocumentElement();

      for (int i = 0; i < records.getLength(); i++) {
        Element record = (Element) records.item(i);
        String id = record.getElementsByTagName("id").item(0).getTextContent();

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
