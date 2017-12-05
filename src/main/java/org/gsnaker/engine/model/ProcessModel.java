package org.gsnaker.engine.model;

import java.util.ArrayList;
import java.util.List;

import org.gsnaker.engine.model.BaseModel;
import org.gsnaker.engine.INoGenerator;
import org.gsnaker.engine.impl.DefaultNoGenerator;
import org.gsnaker.engine.helper.ClassHelper;
import org.gsnaker.engine.helper.StringHelper;
import org.gsnaker.engine.model.NodeModel;
import org.gsnaker.engine.model.TaskModel;
import org.gsnaker.engine.model.StartModel;
/**
 * 流程定义元素
 * @author hoocen
 * @since 1.0
 */
public class ProcessModel  extends BaseModel{

	private static final long serialVersionUID = 8087297316624521590L;
	/**
	 * 节点元素集合
	 */
	private List<NodeModel> nodes = new ArrayList<NodeModel>();
    private List<TaskModel> taskModels = new ArrayList<TaskModel>();
	/**
	 * 流程实例启动url
	 */
	private String instanceUrl;
	/**
	 * 期望完成时间
	 */
	private String expireTime;
	/**
	 * 实例编号生成的class
	 */
	private String instanceNoClass;
	/**
	 * 实例编号生成器对象
	 */
	private INoGenerator generator;
	/**
	 * 流程版本
	 */
	private Integer version;
    /**
     * lock
     */
    private final Object lock = new Object();
    
    /**
	 * 获取process定义的指定节点名称的节点模型
	 * @param nodeName 节点名称
	 * @return
	 */
	public NodeModel getNode(String nodeName) {
		for(NodeModel node : nodes) {
			if(node.getName().equals(nodeName)) {
				return node;
			}
		}
		return null;
	}
	
	/**
	 * 判断当前模型的节点是否包含给定的节点名称参数
	 * @param nodeNames 节点名称数组
	 * @return
	 */
	public <T> boolean containsNodeNames(Class<T> T, String... nodeNames) {
		for(NodeModel node : nodes) {
			if(!T.isInstance(node)) {
				continue;
			}
			for(String nodeName : nodeNames) {
				if(node.getName().equals(nodeName)) {
					return true;
				}
			}
		}
		return false;
	}
	/**
     * 获取所有的有序任务模型集合
     * @return List 任务模型集合
     */
    public List<TaskModel> getTaskModels() {
        if(taskModels.isEmpty()) {
            synchronized (lock) {
                if(taskModels.isEmpty())
                    buildModels(taskModels, getStart().getNextModels(TaskModel.class), TaskModel.class);
            }
        }
        return taskModels;
    }

    /**
     * 根据指定的节点类型返回流程定义中所有模型对象
     * @param clazz 节点类型
     * @param <T> 泛型
     * @return 节点列表
     */
    public <T> List<T> getModels(Class<T> clazz) {
        List<T> models = new ArrayList<T>();
        buildModels(models, getStart().getNextModels(clazz), clazz);
        return models;
    }

    private <T> void buildModels(List<T> models, List<T> nextModels, Class<T> clazz) {
        for(T nextModel : nextModels) {
            if(!models.contains(nextModel)) {
                models.add(nextModel);
                buildModels(models, ((NodeModel)nextModel).getNextModels(clazz), clazz);
            }
        }
    }
	/**
	 * 获取process定义的start节点模型
	 * @return
	 */
	public StartModel getStart() {
		for(NodeModel node : nodes) {
			if(node instanceof StartModel) {
				return (StartModel)node;
			}
		}
		return null;
	}

    public List<NodeModel> getNodes() {
		return nodes;
	}
	public void setNodes(List<NodeModel> nodes) {
		this.nodes = nodes;
	}

	public String getExpireTime() {
		return expireTime;
	}

	public void setExpireTime(String expireTime) {
		this.expireTime = expireTime;
	}

	public String getInstanceUrl() {
		return instanceUrl;
	}

	public void setInstanceUrl(String instanceUrl) {
		this.instanceUrl = instanceUrl;
	}
	public String getInstanceNoClass() {
		return instanceNoClass;
	}

	public void setInstanceNoClass(String instanceNoClass) {
		this.instanceNoClass = instanceNoClass;
		if(StringHelper.isNotEmpty(instanceNoClass)) {
			generator = (INoGenerator)ClassHelper.newInstance(instanceNoClass);
		}
	}
	
	public INoGenerator getGenerator() {
		return generator == null ? new DefaultNoGenerator() : generator;
	}

	public void setGenerator(INoGenerator generator) {
		this.generator = generator;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}
	
}
