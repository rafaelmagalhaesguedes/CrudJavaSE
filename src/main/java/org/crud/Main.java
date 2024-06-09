package org.crud;

import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.net.InetSocketAddress;
import org.crud.core.CrudHandler;

public class Main {

  public static void main(String[] args) throws IOException {
    HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
    server.createContext("/api/crud", new CrudHandler());
    server.setExecutor(null);
    server.start();
    System.out.println("\nServer started on port 8080");
  }
}