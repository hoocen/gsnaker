package org.gsnaker.engine;

import java.util.List;
import java.util.Map;

import org.gsnaker.engine.cfg.Configuration;
import org.gsnaker.engine.core.Execution;
import org.gsnaker.engine.entity.Order;
import org.gsnaker.engine.entity.Task;
import org.gsnaker.engine.model.TaskModel;
import org.gsnaker.engine.IQueryService;

/**
 * 流程引擎接口
 * @author hoocen
 * @since 1.0
 */
public interface SnakerEngine {
	static final String ADMIN = "snaker.admin";
	static final String AUTO = "snaker.auto";
    static final String ID = "snaker.orderNo";
    /**
     * 根据Configuration对象配置实现类
     * @param config 全局配置对象
     * @return SnakerEngine 流程引擎
     */
    public SnakerEngine configure(Configuration config);
    /**
     * 获取process服务
     * @return IProcessService 流程定义服务
     */
    public IProcessService process();
    /**
	 * 获取查询服务
	 * @return IQueryService 常用查询服务
	 */
	public IQueryService query();
    /**
     * 获取流程实例服务
     * @return IProcessService 流程实例服务
     */
    public IOrderService order();
    /**
     * 获取任务服务
     * @return
     */
    public ITaskService task();
    /**
     * 获取管理服务
     * @return
     */
    public IManagerService manager();
    /**
     * 根据流程定义ID，启动流程实例
     * @param id 流程定义ID
     * @return Order 流程实例
     * @see #startInstanceById(String, String, Map)
     */
    public Order startInstanceById(String id);
    /**
     * 根据流程定义ID、操作人ID启动流程实例
     * @param id 流程定义ID
     * @param operator 操作人ID
     * @return Order 流程实例
     * @see #startInstanceById(String, String, Map)
     */
    public Order startInstanceById(String id, String operator);
    /**
     * 根据流程定义ID、操作人ID、启动参数启动流程实例
     * @param id 流程定义ID
     * @param operator 操作人ID
     * @param args 流程启动参数
     * @return Order 流程实例
     */
    public Order startInstanceById(String id, String operator, Map<String, Object> args);
    /**
     * 根据流程定义名称启动流程实例
     * @param name 流程定义名称
     * @return Order 流程实例
     */
    public Order startInstanceByName(String name);
    /**
     * 根据流程定义名称、版本号启动流程实例
     * @param name 流程定义名称
     * @param version 流程定义版本号
     * @return Order 流程实例
     */
    public Order startInstanceByName(String name, Integer version);
    /**
     * 根据流程定义名称、版本号、操作人ID启动流程实例
     * @param name 流程定义名称
     * @param version 版本号
     * @param operator 操作人ID
     * @return Order 流程实例
     */
    public Order startInstanceByName(String name, Integer version, String operator);
    /**
     * 根据流程定义名称、版本号、操作人、启动参数启动流程实例
     * @param name 流程定义名称
     * @param version 版本号
     * @param operator 操作人
     * @param args 启动参数
     * @return Order 流程实例
     */
    public Order startInstanceByName(String name, Integer version, String operator, Map<String, Object> args);
    /**
     * 根据父执行对象启动子流程实例
     * @param execution 父执行对象
     * @return Order 子流程实例
     * @see #executeTask(String, String, Map)
     */
    public Order startInstanceByExecution(Execution execution);
    /**
     * 根据任务主键ID执行任务
     * @param taskId 任务ID
     * @return List<Task> 任务集合
     */
    public List<Task> executeTask(String taskId);
    /**
     * 根据任务主键ID、操作人ID执行任务
     * @param taskId 任务ID
     * @param operator 操作人ID
     * @return List<Task> 任务集合
     * @see #executeTask(String, String, Map)
     */
    public List<Task> executeTask(String taskId, String operator);
    /**
     * 根据任务主键ID、操作人ID、执行参数执行任务
     * @param taskId 任务ID
     * @param operator 操作人ID
     * @param args 执行参数
     * @return List<Task> 任务集合
     */
    public List<Task> executeTask(String taskId, String operator, Map<String, Object> args);
    /**
     * 根据任务主键ID、操作人ID、执行参数执行任务，并根据nodeName跳转至任意节点
     * 1、nodeName为null，跳转至上一步处理
     * 2、nodeName不为null，跳转至nodeName节点，即动态创建转移
     * @param taskId 任务ID
     * @param operator 操作人ID
     * @param args 执行参数
     * @param nodeName 节点名称
     * @return List<Task> 任务集合
     */
    public List<Task> executeAndJumpTask(String taskId, String operator, Map<String, Object> args, String nodeName);
    /**
     * 根据流程实例ID、操作人ID、参数列表按照节点模型创建新的自由任务
     * @param orderId 流程实例ID
     * @param operator 操作人ID
     * @param args 参数列表
     * @param taskModel 节点模型
     * @return List<Task> 任务集合
     */
    public List<Task> createFreeTask(String orderId, String operator, Map<String, Object> args, TaskModel taskModel);
}
