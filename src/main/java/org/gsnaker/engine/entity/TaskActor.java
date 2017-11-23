package org.gsnaker.engine.entity;

import java.io.Serializable;
/**
 * 任务、参与者关联实体
 * @author hoocen
 * @since 1.0
 */
public class TaskActor implements Serializable{

	private static final long serialVersionUID = -754059045812973773L;

	/**
	 * 关联任务ID
	 */
	private String taskId;
	/**
	 * 关联参与者ID
	 */
	private String actorId;
	
	public String getTaskId() {
		return taskId;
	}
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	public String getActorId() {
		return actorId;
	}
	public void setActorId(String actorId) {
		this.actorId = actorId;
	}
}
