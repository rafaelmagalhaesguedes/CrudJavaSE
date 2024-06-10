package org.crud;

import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.net.InetSocketAddress;
import org.crud.core.CrudController;

/**
 * The type Server.
 */
public class Server {

  /**
   * Instantiates a new Server.
   *
   * @throws IOException the io exception
   */
  public Server() throws IOException {
    HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
    server.createContext("/api/crud", new CrudController());
    server.setExecutor(null);
    server.start();
    System.out.println("\nServer started on port 8080");
  }
}
