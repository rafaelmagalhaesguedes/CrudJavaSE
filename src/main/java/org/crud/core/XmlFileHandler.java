package org.crud.core;

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
public class XmlFileHandler {
  private final String filePath;

  /**
   * Constructor
   *
   * @param filePath the file path
   */
  public XmlFileHandler(String filePath) {
    this.filePath = filePath;
  }

  /**
   * Public method loadXml that returns an object of type Document.
   *
   * @return Object Document
   */
  public Document loadXml() {
    try {
      // Here we create an instance of DocumentBuilderFactory using the method
      // static newInstance. DocumentBuilderFactory is a factory that allows
      // the application obtains instances of DocumentBuilder, which are used to
      // build Document objects.
      DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();

      // Using the factory (dbFactory) we just created, we call the method
      // newDocumentBuilder to get a new DocumentBuilder instance.
      // DocumentBuilder is the class responsible for parsing the XML and creating the
      // Document object.
      DocumentBuilder dbBuilder = dbFactory.newDocumentBuilder();

      // In this step, we use DocumentBuilder (dbBuilder) to analyze the
      // XML file located at the specified path (filePath). The method
      // parse reads the file and returns a Document object that represents the
      // XML document tree in memory. This document (doc) contains all
      // the structure and data of the XML file.
      Document doc = dbBuilder.parse(new File(filePath));

      // We call the normalize method on the document root element (doc).
      // This method normalizes the Document, eliminating empty text nodes
      // and joining adjacent text nodes, ensuring a structure
      // document consistent.
      doc.getDocumentElement().normalize();

      // We return the Document object we just created and normalized.
      // This object contains the complete representation of the XML file.
      return doc;
    } catch (Exception e) {
      System.err.println("Error loading XML file: " + e.getMessage());
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
    // Here we create an instance of TransformerFactory using static method
    // newInstance. TransformerFactory is a factory that allows the application to obtain
    // Transformer instances, which are used to transform the tree
    // XML document into an output representation, such as a file or stream.
    TransformerFactory transformerFactory = TransformerFactory.newInstance();

    // Using the factory (transformerFactory) we just created, we call the
    // newTransformer method to get a new Transformer instance.
    // Transformer is the class responsible for transforming the data source
    // (the Document object in our case) into an output result (an XML file).
    Transformer transformer = transformerFactory.newTransformer();

    // We create a DOMSource instance with the Document object (doc). DOMSource is
    // a class that acts as a container for the XML document tree that
    // we want to transform. In this case, source encapsulates the XML document that
    // we want to save.
    DOMSource source = new DOMSource(doc);

    // We create an instance of StreamResult with a new File that points to the
    // file path (filePath). StreamResult is a class that specifies
    // where the transformation output should be sent. In this case, result
    // encapsulates the destination file where we want to save the XML document.
    StreamResult result = new StreamResult(new File(filePath));

    // We use the Transformer (transformer) to transform the source (source, which
    // contains the XML document) in the result (result, which specifies the
    // destiny). The transform method performs the actual transformation, writing the
    // content of the XML document in the specified file.
    transformer.transform(source, result);
  }
}
