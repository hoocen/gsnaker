package org.gsnaker.engine.model;

import org.gsnaker.engine.core.Execution;

/**
 * 分支定义元素
 * @author hoocen
 * @since
 */
public class ForkModel extends NodeModel{

	private static final long serialVersionUID = 3724508126104338425L;

	@Override
	protected void exec(Execution execution) {
		runOutTransition(execution);
	}

}
