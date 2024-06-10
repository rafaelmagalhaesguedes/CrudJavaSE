package org.crud.utils;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;

import java.io.File;

/**
 * The type Xml file handler.
 */
public class XmlFileManager {
  private final String filePath;

  /**
   * Constructor
   *
   * @param filePath the file path
   */
  public XmlFileManager(String filePath) {
    this.filePath = filePath;
  }

  /**
   * Public method loadXml that returns an object of type Document.
   *
   * @return Object Document
   */
  public Document loadXml() {
    try {
      DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
      DocumentBuilder dbBuilder = dbFactory.newDocumentBuilder();
      Document doc = dbBuilder.parse(new File(filePath));

      doc.getDocumentElement().normalize();
      return doc;
    } catch (Exception e) {
      System.err.println(Constants.ERROR_LOADING_XML + e.getMessage());
      return null;
    }
  }

  /**
   * Save xml.
   *
   * @param doc the doc
   * @throws Exception the exception
   */
  public void saveXml(Document doc) throws Exception {
    TransformerFactory transformerFactory = TransformerFactory.newInstance();
    Transformer transformer = transformerFactory.newTransformer();

    DOMSource source = new DOMSource(doc);
    StreamResult result = new StreamResult(new File(filePath));

    transformer.transform(source, result);
  }
}
