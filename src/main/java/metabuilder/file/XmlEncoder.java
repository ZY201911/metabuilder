package metabuilder.file;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TransformerHandler;
import javax.xml.transform.stream.StreamResult;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import metabuilder.MetaBuilder;
import metabuilder.diagram.Diagram;
import metabuilder.diagram.Edge;
import metabuilder.diagram.Node;
import metabuilder.diagram.Properties;
import metabuilder.diagram.Property;

/**
 * Converts a graph to Xml notation. The notation includes:
 * * The JetUML version
 * * The graph type
 * * An array of node encodings
 * * An array of edge encodings
 */
public final class XmlEncoder
{
	// ����SAXת������
    private static SAXTransformerFactory factory = (SAXTransformerFactory)SAXTransformerFactory.newInstance();
	
    // ����TransformerHandlerʵ��
    private static TransformerHandler handler = null;
    
    // ��������ʵ��
    private static AttributesImpl attr = new AttributesImpl();
    
	private XmlEncoder() {}
	
	/**
	 * @param pDiagram The diagram to serialize.
	 */
	public static void encode(Diagram pDiagram, File pFile) throws FileNotFoundException
	{
		assert pDiagram != null;
		
		try {
			handler = factory.newTransformerHandler();
		} catch (TransformerConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// ����handlerת����
	    Transformer transformer = handler.getTransformer();
		transformer.setOutputProperty(OutputKeys.INDENT, "yes"); // ����
	    transformer.setOutputProperty(OutputKeys.ENCODING, "utf-8"); // �ַ���
	    
	    // ����Resultʵ�����ӵ�XML�ļ�
        Result result = new StreamResult(new FileOutputStream(pFile));
        handler.setResult(result);

		
        // ��doc����
        try {
			handler.startDocument();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        // addAttribute(String uri, String localName, String qName, String type, String value)
        attr.addAttribute("", "", "type", "", "Diagram");
        attr.addAttribute("", "", "version", "", MetaBuilder.VERSION.toString());
        attr.addAttribute("", "", "name", "", pDiagram.getName());
		
        // ������Ԫ��: handler.startElement(uri, �����ռ�, Ԫ����, �����б�)
        try {
			handler.startElement("", "", pDiagram.getName(), attr);
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        // �������л�������
        SerializationContext context = new SerializationContext(pDiagram);
        encodeNodes(context);
        encodeEdges(context);
        // ������Ԫ��
        try {
			handler.endElement("", "", pDiagram.getName());
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        try {
			handler.endDocument();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private static void encodeNodes(SerializationContext pContext)
	{
		for( Node node : pContext ) 
		{
			encodeNode(node, pContext);
		}
	}
	
	private static void encodeNode(Node pNode, SerializationContext pContext)
	{
		// Ϊ�ڵ㴴������
    	// ÿ�δ����ڵ�ǰ����������ԣ�����һЩ����
        attr.clear();
        // ��������
        properties2Attr(pNode.properties());
        attr.addAttribute("", "", "type", "", pNode.getClass().getSimpleName());
        attr.addAttribute("", "", "id", "", pContext.getId(pNode) + "");
        attr.addAttribute("", "", "x", "", pNode.position().getX() + "");
        attr.addAttribute("", "", "y", "", pNode.position().getY() + "");
    	// �����ڵ�Ԫ��
        try {
			handler.startElement("", "", "packagedElement", attr);
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if( pNode.getChildren().size() > 0 )
		{
			encodeChildren(pNode, pContext);
		}
		// �����ڵ�Ԫ��
        try {
			handler.endElement("", "", "packagedElement");
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private static void encodeChildren(Node pNode, SerializationContext pContext)
	{
		pNode.getChildren().forEach(child -> encodeNode(child, pContext));
	}
	
	private static void encodeEdges(AbstractContext pContext)
	{
		for( Edge edge : pContext.pDiagram().edges() ) 
		{			
			// Ϊ�ߴ�������
	    	// ÿ�δ�����ǰ����������ԣ�����һЩ����
	        attr.clear();
	        // ��������
	        attr.addAttribute("", "", "type", "", edge.getClass().getSimpleName());
	        attr.addAttribute("", "", "start", "", pContext.getId(edge.getStart()) + "");
	        attr.addAttribute("", "", "end", "", pContext.getId(edge.getEnd()) + "");
	        // ������Ԫ��
	        try {
				handler.startElement("", "", "packagedElement", attr);
			} catch (SAXException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        // ������Ԫ��
	        try {
				handler.endElement("", "", "packagedElement");
			} catch (SAXException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	private static void properties2Attr(Properties pProperties)
	{
		for( Property property : pProperties )
		{
			Object value = property.get();
			if( value instanceof String || value instanceof Enum )
			{
				attr.addAttribute("", "", property.name().external(), "", value.toString());
			}
			else if( value instanceof Integer)
			{
				attr.addAttribute("", "", property.name().external(), "", ((int)value) + "");
			}
			else if( value instanceof Boolean)
			{
				attr.addAttribute("", "", property.name().external(), "", ((boolean)value) + "");
			}
		}
	}
}
