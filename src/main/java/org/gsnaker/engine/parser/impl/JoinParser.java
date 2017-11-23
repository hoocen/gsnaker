package org.gsnaker.engine.parser.impl;

import org.gsnaker.engine.model.JoinModel;
import org.gsnaker.engine.model.NodeModel;
import org.gsnaker.engine.parser.AbstractNodeParser;
/**
 * 合并元素解析器
 * @author hoocen
 * @since 1.0
 */
public class JoinParser extends AbstractNodeParser {

	@Override
	protected NodeModel newModel() {
		return new JoinModel();
	}

}
