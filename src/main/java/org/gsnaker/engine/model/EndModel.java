package org.gsnaker.engine.model;

import java.util.Collections;
import java.util.List;

import org.gsnaker.engine.core.Execution;
import org.gsnaker.engine.handlers.impl.EndProcessHandler;
import org.gsnaker.engine.model.TransitionModel;
/**
 * 结束节点end元素
 * @author hoocen
 * @since 1.0
 */
public class EndModel extends NodeModel{

	private static final long serialVersionUID = -494653247654768488L;

	@Override
	public void exec(Execution execution) {
		fire(new EndProcessHandler(), execution);
	}
	
	/**
	 * 结束节点无输出变迁
	 */
	public List<TransitionModel> getOutputs() {
		return Collections.emptyList();
	}


}
