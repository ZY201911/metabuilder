package metabuilder.file;


import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import metabuilder.diagram.Diagram;
import metabuilder.diagram.DiagramType;
import metabuilder.diagram.Edge;
import metabuilder.diagram.Node;
import metabuilder.diagram.Property;
import metabuilder.geom.Point;

public final class XmlDecoder {
	
	private XmlDecoder() {}
	
	/**
	 * @param pDiagram A JSON object that encodes the diagram.
	 * @return The decoded diagram.
	 * @throws DeserializationException If it's not possible to decode the object into a valid diagram.
	 */
	public static Diagram decode(File pFile)
	{
		assert pFile != null;
		try {
            // ����һ��SAX��������
            SAXParserFactory factory = SAXParserFactory.newInstance();
            // ����һ��SAXת������
            SAXParser saxParser = factory.newSAXParser();
            XMLReader xmlReader = saxParser.getXMLReader();
            XmlHandler xmlHandler = new XmlHandler();
            xmlReader.setContentHandler(xmlHandler);
    		// ����xml
            xmlReader.parse(new InputSource(new FileInputStream(pFile)));
            return xmlHandler.getDiagram();
        } catch (Exception e) {
            e.printStackTrace();
        }
		return null;
	}
}
