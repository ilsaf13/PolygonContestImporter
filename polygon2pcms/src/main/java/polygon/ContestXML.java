package polygon;

import org.xml.sax.SAXException;
import xmlwrapper.XMLElement;

import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.util.NavigableMap;
import java.util.TreeMap;

public class ContestXML {
    protected String url;
    //language -> name
    protected NavigableMap<String, String> contestNames;
    //problem index maps to polygon links
    protected NavigableMap<String, String> problemLinks;
    // language -> statement link
    protected NavigableMap<String, String> statementLinks;

    protected ContestXML(File xmlFile) throws IOException, SAXException, ParserConfigurationException {
        this(new FileInputStream(xmlFile));
    }

    protected ContestXML(InputStream inputStream) throws ParserConfigurationException, SAXException, IOException {
        XMLElement contestElement = XMLElement.getRoot(inputStream);

        url = contestElement.getAttribute("url");

        //names
        contestNames = new TreeMap<>();
        for (XMLElement nameElement : contestElement.findFirstChild("names").findChildren("name")) {
            contestNames.put(nameElement.getAttribute("language"), nameElement.getAttribute("value"));
        }

        //statements
        statementLinks = new TreeMap<>();
        for (XMLElement statementElement : contestElement.findFirstChild("statements").findChildren("statement")) {
            statementLinks.put(statementElement.getAttribute("language"), statementElement.getAttribute("url"));
        }

        //problems
        problemLinks = new TreeMap<>();
        for (XMLElement problemElement : contestElement.findFirstChild("problems").findChildren("problem")) {
            problemLinks.put(problemElement.getAttribute("index"), problemElement.getAttribute("url"));
        }
    }

    public static ContestXML parse(File xmlFile) throws ParserConfigurationException, IOException, SAXException {
        return ContestXML.parse(new FileInputStream(xmlFile));
    }

    public static ContestXML parse(InputStream inputStream) throws ParserConfigurationException, SAXException, IOException {
        return new ContestXML(inputStream);
    }

    public String getUrl() {
        return url;
    }

    public NavigableMap<String, String> getContestNames() {
        return contestNames;
    }

    public NavigableMap<String, String> getProblemLinks() {
        return problemLinks;
    }

    public NavigableMap<String, String> getStatementLinks() {
        return statementLinks;
    }
}
