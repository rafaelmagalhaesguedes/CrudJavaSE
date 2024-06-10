package org.crud.utils;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.net.httpserver.HttpExchange;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * The type Request manager.
 */
public class RequestManager {

  /**
   * Extract request body string.
   *
   * @param exchange the exchange
   * @return the string
   * @throws IOException the io exception
   */
  public static String extractRequestBody(HttpExchange exchange) throws IOException {
    InputStream requestBodyStream = exchange.getRequestBody();
    ByteArrayOutputStream requestBodyBuffer = new ByteArrayOutputStream();

    byte[] buffer = new byte[1024];

    int length;
    while ((length = requestBodyStream.read(buffer)) != -1) {
      requestBodyBuffer.write(buffer, 0, length);
    }

    return requestBodyBuffer.toString(StandardCharsets.UTF_8);
  }

  /**
   * Parse request body map.
   *
   * @param exchange the exchange
   * @return the map
   * @throws IOException the io exception
   */
  public static Map<String, String> parseRequestBody(HttpExchange exchange) throws IOException {
    String requestBody = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
    JsonObject jsonObject = JsonParser.parseString(requestBody).getAsJsonObject();

    Map<String, String> data = new HashMap<>();

    for (Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
      String key = entry.getKey();
      String value = entry.getValue().getAsString();
      data.put(key, value);
    }

    return data;
  }
}
