package com.vn.scheduling;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

public class SystemXMLParser {

    public Servers loadServerList(String filePath) {
        Document document = readXml(filePath);
        return parseXML(document);

    }

    private Servers parseXML(Document dom) {

        Servers servers = new Servers();
        Element docEle = dom.getDocumentElement();
        NodeList nl = docEle.getElementsByTagName("server");

        // Loop
        for (int i = 0; i < nl.getLength(); i++) {
            Element el = (Element) nl.item(i);
            Server server = new Server();
            server.setType(el.getAttribute("type"));
            server.setLimit(Integer.valueOf(el.getAttribute("limit")));
            server.setBootupTime(Integer.valueOf(el.getAttribute("bootupTime")));
            server.setRate(Double.valueOf(el.getAttribute("rate")));
            server.setCoreCount(Integer.valueOf(el.getAttribute("coreCount")));
            server.setMemorySize(Integer.valueOf(el.getAttribute("memory")));
            server.setDiskSize(Integer.valueOf(el.getAttribute("disk")));
            servers.add(server);
        }

        return servers;

    }

    private Document readXml(String filePath) {

        File file = new File(filePath);
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory
                .newInstance();

        try {
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            return documentBuilder.parse(file);
        }

        catch (SAXException e) {
            e.printStackTrace();
        }

        catch (IOException e) {
            e.printStackTrace();
        }

        catch (ParserConfigurationException ex) {
            ex.printStackTrace();
        }

        return null;
    }

}
