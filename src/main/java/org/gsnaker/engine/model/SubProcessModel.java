package org.gsnaker.engine.model;

import org.gsnaker.engine.core.Execution;

/**
 * 子流程定义subprocess元素
 * @author hoocen
 * @since 1.0
 */
public class SubProcessModel extends WorkModel{
	private static final long serialVersionUID = -733056604124167572L;

	/**
	 * 子流程定义名称
	 */
	private String processName;
	/**
	 * 子流程定义版本
	 */
	private Integer version;
	/**
	 * 子流程定义引用
	 */
	private ProcessModel subProcess;
	
	@Override
	protected void exec(Execution execution) {
		runOutTransition(execution);
	}
	
	public String getProcessName() {
		return processName;
	}
	public void setProcessName(String processName) {
		this.processName = processName;
	}
	public Integer getVersion() {
		return version;
	}
	public void setVersion(Integer version) {
		this.version = version;
	}
	public ProcessModel getSubProcess() {
		return subProcess;
	}
	public void setSubProcess(ProcessModel subProcess) {
		this.subProcess = subProcess;
	}
}
