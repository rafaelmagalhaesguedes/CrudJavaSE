package org.crud.core;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.crud.utils.Constants;
import org.crud.utils.GenerateID;
import org.crud.utils.RequestDataFilter;
import org.crud.utils.RequestManager;

/**
 * The type Crud controller.
 */
public class CrudController implements HttpHandler {
  private final CrudService xmlManager = new CrudService(Constants.FILE_PATH);
  private final RequestDataFilter dataFilter;

  public CrudController() {
    Set<String> allowedFields = new HashSet<>();
    allowedFields.add("name");
    allowedFields.add("email");
    allowedFields.add("address");
    this.dataFilter = new RequestDataFilter(allowedFields);
  }

  @Override
  public void handle(HttpExchange exchange) throws IOException {
    String method = exchange.getRequestMethod();
    String response = "";
    int responseCode = 200;

    try {
      switch (method) {
        case Constants.POST -> {
          Map<String, String> requestDataMap = RequestManager.parseRequestBody(exchange);
          Map<String, String> filteredData = dataFilter.filter(requestDataMap);
          if (dataFilter.validate(filteredData)) {
            response = xmlManager.createRecord(filteredData);
          } else {
            responseCode = 400; // Bad Request
            response = Constants.INVALID_DATA;
          }
        }
        case Constants.GET -> response = xmlManager.readRecords();
        case Constants.PUT -> {
          String putId = GenerateID.extractIdFromURI(exchange.getRequestURI().toString());
          Map<String, String> requestDataMap = RequestManager.parseRequestBody(exchange);
          Map<String, String> filteredData = dataFilter.filter(requestDataMap);
          if (dataFilter.validate(filteredData)) {
            response = xmlManager.updateRecord(filteredData, putId);
          } else {
            responseCode = 400; // Bad Request
            response = Constants.INVALID_DATA;
          }
        }
        case Constants.DELETE -> {
          String deleteId = GenerateID.extractIdFromURI(exchange.getRequestURI().toString());
          response = xmlManager.deleteRecord(deleteId);
        }
        default -> {
          responseCode = 405; // Method Not Allowed
          response = Constants.METHOD_NOT_ALLOWED;
        }
      }
    } catch (Exception e) {
      responseCode = 500; // Internal Server Error
      response = Constants.INTERNAL_ERROR + e.getMessage();
    }

    exchange.sendResponseHeaders(responseCode, response.getBytes().length);
    OutputStream os = exchange.getResponseBody();
    os.write(response.getBytes());
    os.close();
  }
}
