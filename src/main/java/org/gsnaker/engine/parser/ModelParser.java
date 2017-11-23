package org.gsnaker.engine.parser;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;

import org.gsnaker.engine.SnakerException;
import org.gsnaker.engine.core.ServiceContext;
import org.gsnaker.engine.helper.XmlHelper;
import org.gsnaker.engine.model.NodeModel;
import org.gsnaker.engine.model.ProcessModel;
import org.gsnaker.engine.model.TransitionModel;
import org.gsnaker.engine.parser.NodeParser;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * 流程定义xml文件的模型解析器
 * @author hoocen
 * @since
 */
public class ModelParser {

	/**
	 * 解析流程定义文件，并将解析后的对象放入模型容器中
	 * @param bytes
	 */
	public static ProcessModel parse(byte[] bytes) {
		DocumentBuilder documentBuilder = XmlHelper.createDocumentBuilder();
		if(documentBuilder == null){
			throw new SnakerException("documentBuilder is null");
		}
		try {
			Document doc = documentBuilder.parse(new ByteArrayInputStream(bytes));
			Element processE = doc.getDocumentElement();
			ProcessModel process = new ProcessModel();
			process.setName(processE.getAttribute(NodeParser.ATTR_NAME));
			process.setDisplayName(processE.getAttribute(NodeParser.ATTR_DISPLAYNAME));
			process.setExpireTime(processE.getAttribute(NodeParser.ATTR_EXPIRETIME));
			process.setInstanceUrl(processE.getAttribute(NodeParser.ATTR_INSTANCEURL));
			process.setInstanceNoClass(processE.getAttribute(NodeParser.ATTR_INSTANCENOCLASS));
			NodeList nodeList = processE.getChildNodes();
			int nodeSize = nodeList.getLength();
			for(int i = 0; i < nodeSize; i++) {
				Node node = nodeList.item(i);
				if (node.getNodeType() == Node.ELEMENT_NODE) {
					NodeModel model = parseModel(node);
					process.getNodes().add(model);
				}
			}
			//循环节点模型，构造变迁输入、输出的source、target
			for(NodeModel node : process.getNodes()) {
				for(TransitionModel transition : node.getOutputs()) {
					String to = transition.getTo();
					for(NodeModel node2 : process.getNodes()) {
						if(to.equalsIgnoreCase(node2.getName())) {
							node2.getInputs().add(transition);
							transition.setTarget(node2);
						}
					}
				}
			}
			return process;
		} catch (SAXException e) {
			throw new SnakerException(e);
		} catch (IOException e) {
			throw new SnakerException(e);
		}
	}
	
	private static NodeModel parseModel(Node node) {
		String nodeName = node.getNodeName();
		Element element = (Element)node;
		NodeParser parser = ServiceContext.findByName(nodeName, NodeParser.class);
		parser.parse(element);
		return parser.getModel();
	}
}
