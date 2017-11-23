package org.gsnaker.engine.parser.impl;

import org.gsnaker.engine.model.EndModel;
import org.gsnaker.engine.model.NodeModel;
import org.gsnaker.engine.parser.AbstractNodeParser;
/**
 * 结束元素解析器
 * @author hoocen
 * @since 1.0
 */
public class EndParser extends AbstractNodeParser {

	@Override
	protected NodeModel newModel() {
		return new EndModel();
	}

}
