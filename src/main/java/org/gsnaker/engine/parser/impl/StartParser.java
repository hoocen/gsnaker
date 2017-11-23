package org.gsnaker.engine.parser.impl;

import org.gsnaker.engine.model.NodeModel;
import org.gsnaker.engine.model.StartModel;
import org.gsnaker.engine.parser.AbstractNodeParser;
/**
 * 开始元素解析器
 * @author hoocen
 * @since 1.0
 */
public class StartParser extends AbstractNodeParser{

	@Override
	protected NodeModel newModel() {
		return new StartModel();
	}

}
