package org.gsnaker.engine.model;

import java.util.Collections;
import java.util.List;

import org.gsnaker.engine.core.Execution;
/**
 * 开始节点定义元素
 * @author hoocen
 * @since 1.0
 */
public class StartModel extends NodeModel{

	private static final long serialVersionUID = 5879493291024170790L;

	/**
	 * 开始节点无输入
	 */
	@Override
	public List<TransitionModel> getInputs() {
		return Collections.emptyList();
	}

	@Override
	protected void exec(Execution execution) {
		this.runOutTransition(execution);
	}

}
