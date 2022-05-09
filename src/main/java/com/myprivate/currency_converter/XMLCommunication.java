package com.myprivate.currency_converter;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.net.*;

public class XMLCommunication {
    private static DocumentBuilderFactory dbf=null;
    String urlString;
    URL url;


    public XMLCommunication() throws MalformedURLException {
        dbf = DocumentBuilderFactory.newInstance();
        urlString = "http://api.nbp.pl/api/exchangerates/tables/a/?format=xml";
        url=new URL(urlString);
    }

    public static NodeList webComm() throws IOException, ParserConfigurationException, SAXException {

        String url = "http://api.nbp.pl/api/exchangerates/tables/a/?format=xml";
        URLConnection connection = new URL(url).openConnection();
        try (InputStream is = connection.getInputStream()) {
            dbf.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
            DocumentBuilder dBuilder = dbf.newDocumentBuilder();
            Document doc = dBuilder.parse(is);
            return doc.getElementsByTagName("Rate");
        }
    }

    public static NodeList localComm() throws ParserConfigurationException, IOException, SAXException {

        File xmlFile = new File("src/main/resources/com/myprivate/currency_converter/XMLCurrrency.xml");
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = factory.newDocumentBuilder();
        Document doc = dBuilder.parse(xmlFile);
        doc.getDocumentElement().normalize();
        return doc.getElementsByTagName("Rate");
    }

    public boolean connectionListener() throws IOException{

        try {
            URLConnection connection = url.openConnection();
            connection.connect();
            System.out.println("connection succeeded");
            return true;
        }catch (UnknownHostException | SocketException e){
            System.out.println("connection failed");
            return false;
        }

    }
}