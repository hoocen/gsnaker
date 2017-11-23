package org.gsnaker.engine.parser.impl;

import org.apache.commons.lang3.math.NumberUtils;
import org.gsnaker.engine.model.NodeModel;
import org.gsnaker.engine.model.SubProcessModel;
import org.gsnaker.engine.parser.AbstractNodeParser;
import org.gsnaker.engine.helper.ConfigHelper;
import org.gsnaker.engine.helper.StringHelper;
import org.w3c.dom.Element;
/**
 * 子流程元素解析器
 * @author hoocen
 * @since 1.0
 */
public class SubProcessParser extends AbstractNodeParser {

	@Override
	protected NodeModel newModel() {
		return new SubProcessModel();
	}
	/**
	 * 解析decisition节点的特有属性expr
	 */
	protected void parseNode(NodeModel node, Element element) {
		SubProcessModel model = (SubProcessModel)node;
		model.setProcessName(element.getAttribute(ATTR_PROCESSNAME));
		String version = element.getAttribute(ATTR_VERSION);
		int ver = 0;
        if(NumberUtils.isNumber(version)) {
        	ver = Integer.parseInt(version);
        }
		model.setVersion(ver);
		String form = element.getAttribute(ATTR_FORM);
		if(StringHelper.isNotEmpty(form)) {
			model.setForm(form);
		} else {
			model.setForm(ConfigHelper.getProperty("subprocessurl"));
		}
	}
}
