package org.gsnaker.engine.entity;

import java.io.Serializable;
/**
 * 抄送实例实体类
 * @author hoocen
 * @since 1.0
 */
public class CCOrder implements Serializable{

	private static final long serialVersionUID = 1264805206184827477L;
	private String orderId;
	private String actorId;
    private String creator;
    private String createTime;
    private String finishTime;
	private Integer status;
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }
	public String getActorId() {
		return actorId;
	}
	public void setActorId(String actorId) {
		this.actorId = actorId;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(String finishTime) {
        this.finishTime = finishTime;
    }
}
