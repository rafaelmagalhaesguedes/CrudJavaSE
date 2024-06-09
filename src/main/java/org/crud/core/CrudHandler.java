package org.crud.core;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;
import org.crud.utils.Constants;

public class CrudHandler implements HttpHandler {
  private final XmlManager xmlManager = new XmlManager(Constants.FILE_PATH);

  @Override
  public void handle(HttpExchange exchange) throws IOException {
    String method = exchange.getRequestMethod();
    String response = "";
    int responseCode = 200;

    try {
      response = switch (method) {
        case "POST" -> xmlManager.createRecord(exchange);
        case "GET" -> xmlManager.readRecords();
        case "PUT" -> xmlManager.updateRecord(exchange);
        case "DELETE" -> xmlManager.deleteRecord(exchange);
        default -> {
          responseCode = 405; // Method Not Allowed
          yield "Method Not Allowed";
        }
      };

    } catch (Exception e) {
      responseCode = 500; // Internal Server Error
      response = "Internal Server Error: " + e.getMessage();
    }

    exchange.sendResponseHeaders(responseCode, response.getBytes().length);
    OutputStream os = exchange.getResponseBody();
    os.write(response.getBytes());
    os.close();
  }
}
