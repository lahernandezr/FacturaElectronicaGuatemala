package com.printer;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


/**
    Libreria la manupulacion de archivo XML
    
    @version 1
    @author Luis Angel Hernandez Rodriguez
    
    Correo: lahernandezr@outlook.com
*/
public class PrinterXML {

    /**
    * Función que parsea un documento XML para su manipulación
    * @param filePath ruta del archivo XML
    */    
    public Document paserXMLtoPrinter(String filePath){
        if(filePath.equals(""))
        {
            System.out.println("Especifique la ruta del archivo XML.");
            return null;
        }
        try {
            File inputFile = new File(filePath);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(inputFile);
            doc.getDocumentElement().normalize();
            return doc;
         } catch (Exception e) {
            e.printStackTrace();
         }
         return null;
    }

    /**
    Función busca una etiqueta en un documento parse XML
    @param doc Documento parse XML
    @param name Nombre del tagName a buscar
    */ 
    public String searchData(Document doc, String name){
        if(doc == null)
            return "";     
        NodeList nList = doc.getElementsByTagName(name);
        String value ="";
        for (int temp = 0; temp < nList.getLength(); temp++) {
           Node nNode = nList.item(temp);
           value = nNode.getTextContent();
        }
        return value;
    }
    /**
    Función busca los nodos en base la etiqueta en un documento parse XML
    @param doc Documento parse XML
    @param name Nombre del tagName a buscar
    */ 
    public NodeList searchList(Document doc, String name){
        if(doc == null)
            return null;           
        NodeList nList = doc.getElementsByTagName(name);
        if(nList == null)
            return null;           
        NodeList listDetalles = nList.item(0).getChildNodes();
        return listDetalles;
    }
}
