package org.gsnaker.engine.parser.impl;

import org.gsnaker.engine.model.CustomModel;
import org.gsnaker.engine.model.NodeModel;
import org.gsnaker.engine.parser.AbstractNodeParser;
import org.w3c.dom.Element;
/**
 * 自定义元素解析器
 * @author hoocen
 * @since 1.0
 */
public class CustomParser extends AbstractNodeParser {

	@Override
	protected NodeModel newModel() {
		return new CustomModel();
	}
	protected void parseNode(NodeModel node, Element element) {
		CustomModel custom = (CustomModel)node;
		custom.setClazz(element.getAttribute(ATTR_CLAZZ));
		custom.setMethodName(element.getAttribute(ATTR_METHODNAME));
		custom.setArgs(element.getAttribute(ATTR_ARGS));
		custom.setVar(element.getAttribute(ATTR_VAR));
	}
}
