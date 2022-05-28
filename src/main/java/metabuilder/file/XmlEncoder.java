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
	// 创建SAX转换工厂
    private static SAXTransformerFactory factory = (SAXTransformerFactory)SAXTransformerFactory.newInstance();
	
    // 创建TransformerHandler实例
    private static TransformerHandler handler = null;
    
    // 创建属性实例
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
		
		// 创建handler转换器
	    Transformer transformer = handler.getTransformer();
		transformer.setOutputProperty(OutputKeys.INDENT, "yes"); // 换行
	    transformer.setOutputProperty(OutputKeys.ENCODING, "utf-8"); // 字符集
	    
	    // 创建Result实例连接到XML文件
        Result result = new StreamResult(new FileOutputStream(pFile));
        handler.setResult(result);

		
        // 打开doc对象
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
		
        // 创建根元素: handler.startElement(uri, 命名空间, 元素名, 属性列表)
        try {
			handler.startElement("", "", pDiagram.getName(), attr);
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        // 创建序列化上下文
        SerializationContext context = new SerializationContext(pDiagram);
        encodeNodes(context);
        encodeEdges(context);
        // 结束根元素
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
		// 为节点创建属性
    	// 每次创建节点前，先清空属性，放置一些属性
        attr.clear();
        // 设置属性
        properties2Attr(pNode.properties());
        attr.addAttribute("", "", "type", "", pNode.getClass().getSimpleName());
        attr.addAttribute("", "", "id", "", pContext.getId(pNode) + "");
        attr.addAttribute("", "", "x", "", pNode.position().getX() + "");
        attr.addAttribute("", "", "y", "", pNode.position().getY() + "");
    	// 创建节点元素
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
		// 结束节点元素
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
			// 为边创建属性
	    	// 每次创建边前，先清空属性，放置一些属性
	        attr.clear();
	        // 设置属性
	        attr.addAttribute("", "", "type", "", edge.getClass().getSimpleName());
	        attr.addAttribute("", "", "start", "", pContext.getId(edge.getStart()) + "");
	        attr.addAttribute("", "", "end", "", pContext.getId(edge.getEnd()) + "");
	        // 创建边元素
	        try {
				handler.startElement("", "", "packagedElement", attr);
			} catch (SAXException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        // 结束边元素
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
