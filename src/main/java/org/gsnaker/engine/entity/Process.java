package org.gsnaker.engine.entity;

import java.io.InputStream;
import java.sql.Blob;

import org.gsnaker.engine.SnakerException;
import org.gsnaker.engine.helper.StreamHelper;
import org.gsnaker.engine.model.ProcessModel;

/**
 * 流程定义实体类
 * @author hoocen
 * @since 1.0
 */
public class Process {
	/**
	 * 流程定义ID
	 */
	private String id;
	/**
	 * 流程定义版本
	 */
	private Integer version;
	/**
	 * 流程定义名称
	 */
	private String name;
	/**
	 * 流程定义显示名称
	 */
	private String displayName;
	/**
	 * 流程类型（预留字段）
	 */
	private String type;
	/**
	 * 流程实例url，一般为开始启动url
	 * 可以通过url打开流程申请表单
	 */
	private String instanceUrl;
	/**
	 * 流程状态
	 */
	private Integer state;
	/**
	 * 流程定义时间
	 */
	private String createTime;
	/**
	 * 流程创建者
	 */
	private String creator;
	/**
	 * 流程定义模型,加载process对象时由ModelParser解析流程定义获取
	 */
    private ProcessModel model;
    /**
     * 流程定义xml内容
     */
    private Blob content;
    /**
     * 流程定义字节数组
     */
    private byte[] bytes;
    
    /**
	 * setter name/displayName/instanceUrl
	 * @param processModel
	 */
	public void setModel(ProcessModel processModel) {
		this.model = processModel;
    	this.name = processModel.getName();
    	this.displayName = processModel.getDisplayName();
    	this.instanceUrl = processModel.getInstanceUrl();
	}
	
	public byte[] getDBContent() {
		if(this.content != null) {
			try {
				return this.content.getBytes(1L, Long.valueOf(this.content.length()).intValue());
			} catch (Exception e) {
				try {
					InputStream is = content.getBinaryStream();
					return StreamHelper.readBytes(is);
				} catch (Exception e1) {
					throw new SnakerException("couldn't extract stream out of blob", e1);
				}
			}
		}
		
		return bytes;
	}
    
    public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDisplayName() {
		return displayName;
	}
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Integer getState() {
		return state;
	}
	public void setState(Integer state) {
		this.state = state;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	public ProcessModel getModel() {
		return model;
	}
	public String getInstanceUrl() {
		return instanceUrl;
	}
	public void setInstanceUrl(String instanceUrl) {
		this.instanceUrl = instanceUrl;
	}
	public Blob getContent() {
		return content;
	}
	public void setContent(Blob content) {
		this.content = content;
	}
	public byte[] getBytes() {
		return bytes;
	}
	public void setBytes(byte[] bytes) {
		this.bytes = bytes;
	}
    public Integer getVersion() {
		return version;
	}
	public void setVersion(Integer version) {
		this.version = version;
	}
	
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getCreator() {
		return creator;
	}
	public void setCreator(String creator) {
		this.creator = creator;
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Process(id=").append(this.id);
		sb.append(",name=").append(this.name);
		sb.append(",displayName=").append(this.displayName);
		sb.append(",version=").append(this.version);
		sb.append(",state=").append(this.state).append(")");
		return sb.toString();
	}
}
