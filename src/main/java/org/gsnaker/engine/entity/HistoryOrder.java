package org.gsnaker.engine.entity;

import java.io.Serializable;
import java.util.Collections;
import java.util.Map;

import org.gsnaker.engine.SnakerEngine;
import org.gsnaker.engine.core.ServiceContext;
import org.gsnaker.engine.entity.Order;
import org.gsnaker.engine.entity.Process;
import org.gsnaker.engine.helper.JsonHelper;

/**
 * 流程实例历史实体类
 * @author hoocen
 * @since 1.0
 */
public class HistoryOrder implements Serializable {

	private static final long serialVersionUID = -8693208348917952096L;

	/**
	 * 主键ID
	 */
	private String id;
	/**
	 * 流程定义ID
	 */
    private String processId;
    /**
     * 流程实例状态（0：结束；1：活动）
     */
    private Integer orderState;
    /**
     * 流程实例创建者ID
     */
    private String creator;
    /**
     * 流程实例创建时间
     */
    private String createTime;
    /**
     * 流程实例结束时间
     */
    private String endTime;
    /**
     * 流程实例为子流程时，该字段标识父流程实例ID
     */
    private String parentId;
    /**
     * 流程实例期望完成时间
     */
    private String expireTime;
    /**
     * 流程实例优先级
     */
    private Integer priority;
    /**
     * 流程实例编号
     */
    private String orderNo;
    /**
     * 流程实例版本
     */
    private Integer orderVersion;
	/**
     * 流程实例附属变量
     */
    private String variable;

	public HistoryOrder() {
    	
    }
    
    public HistoryOrder(Order order) {
    	this.id = order.getId();
    	this.processId = order.getProcessId();
    	this.createTime = order.getCreateTime();
    	this.expireTime = order.getExpireTime();
    	this.creator = order.getCreator();
    	this.parentId = order.getParentId();
    	this.priority = order.getPriority();
    	this.orderNo = order.getOrderNo();
    	this.orderVersion = order.getVersion();
    	this.variable = order.getVariable();
    }

    /**
     * 根据历史实例撤回活动实例
     * @return 活动实例对象
     */
    public Order undo() {
        Order order = new Order();
        order.setId(this.id);
        order.setProcessId(this.processId);
        order.setParentId(this.parentId);
        order.setCreator(this.creator);
        order.setCreateTime(this.createTime);
        order.setLastUpdator(this.creator);
        order.setLastUpdateTime(this.endTime);
        order.setExpireTime(this.expireTime);
        order.setOrderNo(this.orderNo);
        order.setPriority(this.priority);
        order.setVariable(this.variable);
        order.setVersion(this.orderVersion);
        return order;
    }

	public String getProcessId() {
		return processId;
	}

	public void setProcessId(String processId) {
		this.processId = processId;
	}

	public Integer getOrderState() {
		return orderState;
	}

	public void setOrderState(Integer orderState) {
		this.orderState = orderState;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getExpireTime() {
		return expireTime;
	}

	public void setExpireTime(String expireTime) {
		this.expireTime = expireTime;
	}

	public Integer getPriority() {
		return priority;
	}

	public void setPriority(Integer priority) {
		this.priority = priority;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
    
    public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getVariable() {
		return variable;
	}

	public void setVariable(String variable) {
		this.variable = variable;
	}

    @SuppressWarnings("unchecked")
    public Map<String, Object> getVariableMap() {
        Map<String, Object> map = JsonHelper.fromJson(this.variable, Map.class);
        if(map == null) return Collections.emptyMap();
        return map;
    }
	
	public Integer getOrderVersion() {
		return orderVersion;
	}

	public void setOrderVersion(Integer orderVersion) {
		this.orderVersion = orderVersion;
	}

	public String getProcessName() {
		SnakerEngine engine = ServiceContext.getEngine();
		Process process = engine.process().getProcessById(this.processId);
		if(process == null) return this.processId;
		return process.getDisplayName();
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("HistoryOrder(id=").append(this.id);
		sb.append(",processId=").append(this.processId);
		sb.append(",creator=").append(this.creator);
		sb.append(",createTime").append(this.createTime);
		sb.append(",orderNo=").append(this.orderNo).append(")");
		return sb.toString();
	}
}
