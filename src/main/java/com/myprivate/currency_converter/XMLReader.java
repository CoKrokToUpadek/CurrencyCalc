package com.myprivate.currency_converter;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;


import org.xml.sax.SAXException;

public class XMLReader {
    private List<Currency> xmlList;


    public XMLReader() throws IOException, ParserConfigurationException, SAXException {
        this.xmlList = new ArrayList<>();
        populateCurrencyList(false);
    }

    public void populateCurrencyList(boolean loadFromWeb) throws SAXException,
            IOException, ParserConfigurationException {

        this.xmlList.add(new Currency("złoty polski", "PLN", 1));

        NodeList outPutNodeList;
        if (loadFromWeb){
            outPutNodeList=XMLCommunication.webComm();
        }
        else {
            outPutNodeList=XMLCommunication.localComm();
        }

        NodeList nList = outPutNodeList;

        for (int i = 0; i < nList.getLength(); i++) {

            Node nNode = nList.item(i);

            if (nNode.getNodeType() == Node.ELEMENT_NODE) {

                Element elem = (Element) nNode;

                Node node1 = elem.getElementsByTagName("Currency").item(0);
                String currencyLong = node1.getTextContent();

                Node node2 = elem.getElementsByTagName("Code").item(0);
                String currencyShort = node2.getTextContent();

                Node node3 = elem.getElementsByTagName("Mid").item(0);
                String currencyValue = node3.getTextContent();

                Currency c = new Currency(currencyLong, currencyShort, Double.parseDouble(currencyValue));
                xmlList.add(c);
                //debug
                //System.out.printf(""+c+"\n");

            }
        }
    }

    public List<Currency> getXmlList() {
        return xmlList;
    }

    public double getElement(int i) {
        return xmlList.get(i).getCurrencyRate();
    }

    public static void updateLocalXML() throws ParserConfigurationException, IOException, SAXException, TransformerException {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = dbf.newDocumentBuilder();

        URL url = new URL("http://api.nbp.pl/api/exchangerates/tables/a/?format=xml");
        InputStream stream = url.openStream();
        Document doc = docBuilder.parse(stream);
        stream.close();
        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        Result output = new StreamResult(new File("src/main/resources/com/myprivate/currency_converter/XMLCurrrency.xml"));
        Source input = new DOMSource(doc);
        transformer.transform(input, output);
    }

    public void xmlListUpdate() throws IOException, ParserConfigurationException, SAXException, TransformerException {
            xmlList.clear();
            populateCurrencyList(true);
            updateLocalXML();
            System.out.println(xmlList.get(1).getCurrencyRate());
    }
}
