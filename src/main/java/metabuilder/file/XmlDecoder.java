package metabuilder.file;


import java.io.File;
import java.lang.reflect.InvocationTargetException;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
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

public final class XmlDecoder extends DefaultHandler {
	private static final String PREFIX_NODES = "metabuilder.diagram.nodes.";
	private static final String PREFIX_EDGES = "metabuilder.diagram.edges.";
	private static Diagram diagram = new Diagram(DiagramType.fromName("MetaModelDiagram"));
	private static DeserializationContext context = new DeserializationContext(diagram);
	
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
            // 创建一个SAX解析工厂
            SAXParserFactory factory = SAXParserFactory.newInstance();
            // 创建一个SAX转换工具
            SAXParser saxParser = factory.newSAXParser();
            // 解析XML
            saxParser.parse(pFile, new XmlDecoder());
        } catch (Exception e) {
            e.printStackTrace();
        }
		System.out.println(diagram);
		return diagram;
	}

 
    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if(qName == "MetaModelDiagram") {}
        else if(qName == "packagedElement") {
        	String type = attributes.getValue("type");
        	if(type.substring(type.length() - 4).equals("Node")) {
        		System.out.println("getNode");
        		Class<?> nodeClass = null;
				try {
					nodeClass = Class.forName(PREFIX_NODES + type);
				} catch (ClassNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				Node node = null;
				try {
					node = (Node) nodeClass.getDeclaredConstructor().newInstance();
				} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
						| InvocationTargetException | NoSuchMethodException | SecurityException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				int x = Integer.parseInt(attributes.getValue("x"));
				int y = Integer.parseInt(attributes.getValue("y"));
				node.moveTo(new Point(x, y));
				for( Property property : node.properties() )
				{
					property.set(attributes.getValue(property.name().external()));
					
				}
				int id = Integer.parseInt(attributes.getValue("id"));
				System.out.println(node);
				context.addNode(node, id);
        	}
        	else if(type.substring(type.length() - 4).equals("Edge")) {
        		System.out.println("getEdge");
        		Class<?> edgeClass = null;
				try {
					edgeClass = Class.forName(PREFIX_EDGES + type);
				} catch (ClassNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				Edge edge = null;
				try {
					edge = (Edge) edgeClass.getDeclaredConstructor().newInstance();
				} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
						| InvocationTargetException | NoSuchMethodException | SecurityException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				int start = Integer.parseInt(attributes.getValue("start"));
				int end = Integer.parseInt(attributes.getValue("end"));
//				for( Property property : edge.properties() )
//				{
//					property.set(attributes.getValue(property.name().external()));
//					
//				}
				edge.connect(context.getNode(start), context.getNode(end), context.pDiagram());
				System.out.println(edge);
				context.pDiagram().addEdge(edge);
        	}
        }
    }
 
    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        System.out.println("</" + qName + ">");
    }
 
    @Override
    public void characters(char ch[], int start, int length) throws SAXException {
//        String str = new String(ch, start, length);
//        if (!"\n".equals(str)) {
//            System.out.print(str);
//        }
    }
}
