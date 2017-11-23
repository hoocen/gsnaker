package org.gsnaker.engine.model;

import org.gsnaker.engine.core.Execution;
import org.gsnaker.engine.handlers.impl.MergeBranchHandler;

public class JoinModel extends NodeModel{

	private static final long serialVersionUID = -1226686972239202324L;

	@Override
	protected void exec(Execution execution) {
		fire(new MergeBranchHandler(this), execution);
		if(execution.isMerged()) runOutTransition(execution);
	}

}
