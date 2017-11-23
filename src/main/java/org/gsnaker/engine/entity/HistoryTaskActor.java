package org.gsnaker.engine.entity;

import java.io.Serializable;
/**
 * 参与者历史实体类
 * @author hoocen
 * @since
 */
public class HistoryTaskActor implements Serializable {

	private static final long serialVersionUID = -2356433550924826926L;

	/**
	 * 关联的任务ID
	 */
    private String taskId;
    /**
     * 关联的参与者ID（参与者可以为用户、部门、角色）
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
