package org.gsnaker.engine.impl;

import java.util.ArrayList;
import java.util.List;

import org.gsnaker.engine.TaskAccessStrategy;
import org.gsnaker.engine.entity.TaskActor;

/**
 * 基于用户或组（角色、部门等）的访问策略类
 * 该策略类适合组作为参与者的情况
 * @author hoocen
 * @since 1.0
 */
public class GeneralAccessStrategy implements TaskAccessStrategy {

	/**
	 * 根据操作人id确定所有的组集合
	 * @param operator 操作人id
	 * @return List 确定的组集合[如操作人属于多个部门、拥有多个角色]
	 */
	protected List<String> ensureGroup(String operator) {
		return null;
	}
	
	/**
	 * 如果操作人id所属的组只要有一项存在于参与者集合中，则表示可访问
	 */
	public boolean isAllowed(String operator, List<TaskActor> actors) {
		List<String> assignees = ensureGroup(operator);
		if(assignees == null) assignees = new ArrayList<String>();
		assignees.add(operator);
		boolean isAllowed = false;
		for (TaskActor actor : actors) {
			for (String assignee : assignees) {
				if (actor.getActorId().equals(assignee)) {
					isAllowed = true;
					break;
				}
			}
		}
		return isAllowed;
	}
}
