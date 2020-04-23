package ce326.hw3;

import org.w3c.dom.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;

public class FavoritesInXML {
    private Element rootFavorites;
    private Document doc;
    private Transformer transformer;
    private DOMSource source;
    private DocumentBuilder dBuilder;
    private StreamResult streamFile;
    private static final String CONFIG_PATH="./properties.xml";

    public FavoritesInXML(String defaultEntry) {
        File configFile = new File(CONFIG_PATH);

        try {
            DocumentBuilderFactory dbFactory =
                    DocumentBuilderFactory.newInstance();
            dBuilder = dbFactory.newDocumentBuilder();

            if (configFile.exists()) {
                doc = dBuilder.parse(configFile);
                rootFavorites = doc.getDocumentElement();
            }
            else {
                doc = dBuilder.newDocument();

                // favorites element
                rootFavorites = doc.createElement("favorites");
                doc.appendChild(rootFavorites);
            }
            // write the content into xml file
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            transformer = transformerFactory.newTransformer();
            source = new DOMSource(doc);
            streamFile = new StreamResult(configFile);
            transformer.transform(source, streamFile);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void removeEntry(String filePath) {
        NodeList nList = doc.getElementsByTagName("directory");

        for (int i = 0; i < nList.getLength(); i++) {

            Node nNode = nList.item(i);
            if (nNode.getNodeType() == Node.ELEMENT_NODE) {

                Element eElement = (Element) nNode;
                System.out.println(filePath);
                System.out.println(eElement.getAttribute("path"));

                if (eElement.getAttribute("path").equals(filePath)) {
                    System.out.println("Done");
                    nNode.getParentNode().removeChild(nNode);
                }
                try {
                    transformer.transform(source, streamFile);
                } catch (TransformerException e) {
                    e.printStackTrace();
                }

            }
        }

    }

    public File[] readAllEntries() {
        File[] entries = new File[0];
        try {

            File fXmlFile = new File(CONFIG_PATH);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(fXmlFile);

            doc.getDocumentElement().normalize();

            System.out.println("Root element :" + doc.getDocumentElement().getNodeName());

            NodeList nList = doc.getElementsByTagName("directory");
            entries = new File[nList.getLength()];

            for (int i = 0; i < nList.getLength(); i++) {

                Node nNode = nList.item(i);

                System.out.println("\nCurrent Element :" + nNode.getNodeName());

                if (nNode.getNodeType() == Node.ELEMENT_NODE) {

                    Element eElement = (Element) nNode;

                    entries[i] = new File(eElement.getAttribute("path"));
                    System.out.println("Name : " + eElement.getElementsByTagName("name").item(0).getTextContent());

                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return entries;

    }

    public void addToXML(String dirName, String dirPath) {
        Element dirElement = doc.createElement("directory");
        rootFavorites.appendChild(dirElement);

        Attr pathAttr = doc.createAttribute("path");
        pathAttr.setValue(dirPath);
        dirElement.setAttributeNode(pathAttr);

        Element name = doc.createElement("name");
        name.appendChild(doc.createTextNode(dirName));
        dirElement.appendChild(name);

        try {
            transformer.transform(source, streamFile);
        } catch (TransformerException e) {
            e.printStackTrace();
        }


    }

}
