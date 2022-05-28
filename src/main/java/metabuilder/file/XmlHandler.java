package metabuilder.file;

import java.lang.reflect.InvocationTargetException;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import metabuilder.diagram.Diagram;
import metabuilder.diagram.DiagramType;
import metabuilder.diagram.Edge;
import metabuilder.diagram.Node;
import metabuilder.diagram.Property;
import metabuilder.geom.Point;

public class XmlHandler extends DefaultHandler {
	private final String PREFIX_NODES = "metabuilder.diagram.nodes.";
	private final String PREFIX_EDGES = "metabuilder.diagram.edges.";
	private Diagram diagram = new Diagram(DiagramType.fromName("MetaModelDiagram"));
	private DeserializationContext context = new DeserializationContext(diagram);
	
	@Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if(qName == "packagedElement") {
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
				context.pDiagram().addRootNode(node);
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
    
    public Diagram getDiagram() {
    	return context.pDiagram();
    }
 
}
