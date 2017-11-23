package org.gsnaker.engine.parser.impl;

import org.gsnaker.engine.model.ForkModel;
import org.gsnaker.engine.model.NodeModel;
import org.gsnaker.engine.parser.AbstractNodeParser;
/**
 * 分支元素解析器
 * @author hoocen
 * @since 1.0
 */
public class ForkParser extends AbstractNodeParser{

	@Override
	protected NodeModel newModel() {
		return new ForkModel();
	}

}
