package org.gsnaker.engine.core;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.gsnaker.engine.IManagerService;
import org.gsnaker.engine.IOrderService;
import org.gsnaker.engine.IProcessService;
import org.gsnaker.engine.IQueryService;
import org.gsnaker.engine.ITaskService;
import org.gsnaker.engine.SnakerEngine;
import org.gsnaker.engine.access.transaction.TransactionInterceptor;
import org.gsnaker.engine.cfg.Configuration;
import org.gsnaker.engine.entity.Order;
import org.gsnaker.engine.entity.Task;
import org.gsnaker.engine.entity.Process;
import org.gsnaker.engine.helper.AssertHelper;
import org.gsnaker.engine.helper.DateHelper;
import org.gsnaker.engine.model.NodeModel;
import org.gsnaker.engine.model.ProcessModel;
import org.gsnaker.engine.model.StartModel;
import org.gsnaker.engine.model.TaskModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.gsnaker.engine.core.Execution;
import org.gsnaker.engine.helper.StringHelper;
import org.gsnaker.engine.model.TransitionModel;
import org.gsnaker.engine.cache.CacheManager;
import org.gsnaker.engine.cache.CacheManagerAware;
import org.gsnaker.engine.cache.memory.MemoryCacheManager;
import org.gsnaker.engine.DBAccess;

public class SnakerEngineImpl implements SnakerEngine {
	private static final Logger log = LoggerFactory.getLogger(SnakerEngineImpl.class);
	/**
	 * Snaker配置对象
	 */
	protected Configuration configuration;
	/**
	 * 流程定义业务类
	 */
	protected IProcessService processService;
	/**
	 * 流程实例业务类
	 */
	protected IOrderService orderService;
	/**
	 * 任务业务类
	 */
	protected ITaskService taskService;
	/**
	 * 查询业务类
	 */
	protected IQueryService queryService;
	/**
	 * 管理业务类
	 */
	protected IManagerService managerService;
	
	@Override
	public SnakerEngine configure(Configuration config) {
		this.configuration = config;
		processService = ServiceContext.find(IProcessService.class);
		queryService = ServiceContext.find(IQueryService.class);
		orderService = ServiceContext.find(IOrderService.class);
		taskService = ServiceContext.find(ITaskService.class);
		managerService = ServiceContext.find(IManagerService.class);
		
		//无spring的情况下，DBAccess对象通过上下文获取
		if(!configuration.isCMB()){
			DBAccess dbaccess = ServiceContext.find(DBAccess.class);
			AssertHelper.notNull(dbaccess);
			TransactionInterceptor interceptor = ServiceContext.find(TransactionInterceptor.class);
			//如果初始化配置时提供了访问对象，就对DBAccess进行初始化
			Object accessObject = this.configuration.getAccessDBObject();
			if(accessObject != null) {
				if(interceptor != null) {
					interceptor.initialize(accessObject);
				}
				dbaccess.initialize(accessObject);
			}
			setDBAccess(dbaccess);
			dbaccess.runScript();
		}
		CacheManager cacheManager = ServiceContext.find(CacheManager.class);
		if(cacheManager == null) {
			//默认使用内存缓存管理器
			cacheManager = new MemoryCacheManager();
		}
		List<CacheManagerAware> cacheServices = ServiceContext.findList(CacheManagerAware.class);
		for(CacheManagerAware cacheService : cacheServices) {
			cacheService.setCacheManager(cacheManager);
		}
		return this;
	}
	/**
	 * 注入dbAccess
	 * @param access db访问对象
	 */
	protected void setDBAccess(DBAccess access) {
		List<AccessService> services = ServiceContext.findList(AccessService.class);
		for(AccessService service : services) {
			service.setAccess(access);
		}
	}

	/**
	 * 获取流程定义服务
	 */
	public IProcessService process() {
		AssertHelper.notNull(processService);
		return processService;
	}

	/**
	 * 获取查询服务
	 */
	public IQueryService query() {
		AssertHelper.notNull(queryService);
		return queryService;
	}
	
	/**
	 * 获取实例服务
	 * @since 1.2.2
	 */
	public IOrderService order() {
		AssertHelper.notNull(orderService);
		return orderService;
	}

	/**
	 * 获取任务服务
	 * @since 1.2.2
	 */
	public ITaskService task() {
		AssertHelper.notNull(taskService);
		return taskService;
	}
	
	/**
	 * 获取管理服务
	 * @since 1.4
	 */
	public IManagerService manager() {
		AssertHelper.notNull(managerService);
		return managerService;
	}

	@Override
	public Order startInstanceById(String id) {
		return startInstanceById(id, null);
	}

	@Override
	public Order startInstanceById(String id, String operator) {
		return startInstanceById(id, operator, null);
	}

	@Override
	public Order startInstanceById(String id, String operator, Map<String, Object> args) {
		if(args == null){
			args = new HashMap<String, Object>();
		}
		Process process = process().getProcessById(id);
		process().check(process, id);
		return startProcess(process, operator, args);
	}
	
	@Override
	public Order startInstanceByName(String name) {
		return startInstanceByName(name, null);
	}

	@Override
	public Order startInstanceByName(String name, Integer version) {
		return startInstanceByName(name, version, null);
	}

	@Override
	public Order startInstanceByName(String name, Integer version,
			String operator) {
		return startInstanceByName(name, version, operator, null);
	}

	@Override
	public Order startInstanceByName(String name, Integer version,
			String operator, Map<String, Object> args) {
		if(args == null){
			args = new HashMap<String, Object>();
		}
		Process process = process().getProcessByVersion(name, version);
		process().check(process, name);
		return startProcess(process, operator, args);
	}

	/**
	 * 根据父执行对象启动子流程实例（用于启动子流程）
	 */
	@Override
	public Order startInstanceByExecution(Execution execution) {
		Process process = execution.getProcess();
		StartModel startModel = process.getModel().getStart();
		AssertHelper.notNull(startModel, "流程定义[name:" + process.getName() + ",version:" + process.getVersion() + "]没有开始节点。");
		Execution current = execute(process, execution.getOperator(), execution.getArgs(), 
				execution.getParentOrder().getId(), execution.getParentNodeName());
		startModel.execute(current);
		return current.getOrder();
	}

	@Override
	public List<Task> executeTask(String taskId) {
		return executeTask(taskId, null);
	}

	@Override
	public List<Task> executeTask(String taskId, String operator) {
		return executeTask(taskId, operator, null);
	}

	@Override
	public List<Task> executeTask(String taskId, String operator, Map<String, Object> args) {
		//完成任务，并且构造执行对象
		Execution execution = execute(taskId, operator, args);
		if(execution == null){
			return Collections.emptyList();
		}
		ProcessModel model = execution.getModel();
		if(model != null){
			NodeModel node = model.getNode(execution.getTask().getTaskName());
			node.execute(execution);
		}
		return execution.getTasks();
	}

	@Override
	public List<Task> executeAndJumpTask(String taskId, String operator,
			Map<String, Object> args, String nodeName) {
		Execution execution = execute(taskId, operator, args);
		if(execution == null) return Collections.emptyList();
		ProcessModel model = execution.getProcess().getModel();
		AssertHelper.notNull(model, "当前任务未找到流程定义模型");
		if(StringHelper.isEmpty(nodeName)) {
			Task newTask = task().rejectTask(model, execution.getTask());
			execution.addTask(newTask);
		} else {
			NodeModel nodeModel = model.getNode(nodeName);
			AssertHelper.notNull(nodeModel, "根据节点名称[" + nodeName + "]无法找到节点模型");
			//动态创建转移对象，由转移对象执行execution实例
			TransitionModel tm = new TransitionModel();
			tm.setTarget(nodeModel);
			tm.setEnabled(true);
			tm.execute(execution);
		}

		return execution.getTasks();
	}

	@Override
	public List<Task> createFreeTask(String orderId, String operator,
			Map<String, Object> args, TaskModel taskModel) {
		Order order = query().getOrder(orderId);
		AssertHelper.notNull(order, "指定的流程实例[id=" + orderId + "]已完成或不存在");
		order.setLastUpdator(operator);
		order.setLastUpdateTime(DateHelper.getTime());
		Process process = process().getProcessById(order.getProcessId());
		Execution execution = new Execution(this, process, order, args);
		execution.setOperator(operator);
		return task().createTask(taskModel, execution);
	}
	/**
	 * 创建并启动流程实例
	 * @param process 流程定义
	 * @param operator 操作人
	 * @param args 参数
	 * @return Order 流程实例
	 */
	private Order startProcess(Process process, String operator, Map<String, Object> args){
		Execution execution = execute(process, operator, args, null, null);
		if(process.getModel() != null){
			StartModel startModel = process.getModel().getStart();
			AssertHelper.notNull(startModel, "流程定义[name:" + process.getName() + ",version:" + process.getVersion() + "]没有开始节点。");
			startModel.execute(execution);
		}
		return execution.getOrder();
	}
	/**
	 * 创建流程实例，并返回执行对象
	 * @param process 流程定义
	 * @param operator 操作人
	 * @param args 参数
	 * @param parentId 父实例id
	 * @param parentNodeName 启动子流程的父流程节点名称
	 * @return Execution 执行对象
	 */
	private Execution execute(Process process, String operator, Map<String, Object> args, String parentId, String parentNodeName) {
		Order order = order().createOrder(process, operator, args, parentId, parentNodeName);
		if(log.isDebugEnabled()){
			log.debug("启动流程实例:" + order);
		}
		Execution current = new Execution(this, process, order, args);
		current.setOperator(operator);
		return current;
	}
	
	private Execution execute(String taskId, String operator, Map<String, Object> args){
		if(args == null){
			args = new HashMap<String, Object>();
		}
		Task task = task().complete(taskId, operator, args);
		Order order = query().getOrder(task.getOrderId());
		AssertHelper.notNull(order, "指定的流程实例[id=" + task.getOrderId() + "]已完成或不存在");
		order.setLastUpdator(operator);
		order.setLastUpdateTime(DateHelper.getTime());
		order().updateOrder(order);
		//协办任务完成不产生执行对象
		if(!task.isMajor()){
			return null;
		}
		Map<String, Object> orderMaps = order.getVariableMap();
		if(orderMaps != null) {
			for(Map.Entry<String, Object> entry : orderMaps.entrySet()) {
				if(args.containsKey(entry.getKey())) {
					continue;
				}
				args.put(entry.getKey(), entry.getValue());
			}
		}
		Process process = process().getProcessById(order.getProcessId());
		Execution current = new Execution(this, process, order, orderMaps);
		current.setOperator(operator);
		current.setTask(task);
		return current;
	}

}
