package org.gsnaker.engine.parser.impl;

import org.gsnaker.engine.model.DecisionModel;
import org.gsnaker.engine.model.NodeModel;
import org.gsnaker.engine.parser.AbstractNodeParser;
import org.w3c.dom.Element;

/**
 * 决策元素解析器
 * @author hoocen
 * @since 1.0
 */
public class DecisionParser extends AbstractNodeParser {

	@Override
	protected NodeModel newModel() {
		return new DecisionModel();
	}

	/**
	 * 解析decisition节点的特有属性expr
	 */
	protected void parseNode(NodeModel node, Element element) {
		DecisionModel decision = (DecisionModel)node;
		decision.setExpr(element.getAttribute(ATTR_EXPR));
		decision.setHandleClass(element.getAttribute(ATTR_HANDLECLASS));
	}
}
