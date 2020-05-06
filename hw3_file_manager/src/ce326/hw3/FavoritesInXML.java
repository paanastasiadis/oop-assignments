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
    private StreamResult streamFile;
    private File configFile;
    private static final String CONFIG_PATH = System.getProperty("user.home") + File.separator + ".java-file-browser";

    public FavoritesInXML() {

        configFile = new File(CONFIG_PATH);

        //if the folder does not exist in the home direcotry of the user, create it
        if (!configFile.exists()) {
            configFile.mkdir();
        }

        //add the properties filename to the path
        configFile = new File(CONFIG_PATH + File.separator + "properties.xml");
        try {
            DocumentBuilderFactory dbFactory =
                    DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();


            if (configFile.exists()) {
                doc = dBuilder.parse(configFile);
                rootFavorites = doc.getDocumentElement();
            } else {
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

    //read all the file entries inside the xml file
    public File[] readAllXMLEntries() {
        File[] entries = new File[0];
        try {

            File fXmlFile = configFile;
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(fXmlFile);

            doc.getDocumentElement().normalize();

            NodeList nList = doc.getElementsByTagName("directory");
            entries = new File[nList.getLength()];

            for (int i = 0; i < nList.getLength(); i++) {

                Node nNode = nList.item(i);

                if (nNode.getNodeType() == Node.ELEMENT_NODE) {

                    Element eElement = (Element) nNode;

                    entries[i] = new File(eElement.getAttribute("path"));

                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return entries;

    }

    //store a new entry to the xml file
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

    //delete a file entry in the xml file
    public void removeEntry(String filePath) {
        NodeList nList = doc.getElementsByTagName("directory");

        for (int i = 0; i < nList.getLength(); i++) {

            Node nNode = nList.item(i);
            if (nNode.getNodeType() == Node.ELEMENT_NODE) {

                Element eElement = (Element) nNode;

                if (eElement.getAttribute("path").equals(filePath)) {
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
}
