package org.gsnaker.engine.core;

import java.util.List;

import org.gsnaker.engine.IQueryService;
import org.gsnaker.engine.core.AccessService;
import org.gsnaker.engine.entity.Order;
import org.gsnaker.engine.entity.Task;
import org.gsnaker.engine.entity.TaskActor;
import org.gsnaker.engine.entity.HistoryTaskActor;
import org.gsnaker.engine.access.Page;
import org.gsnaker.engine.access.QueryFilter;
import org.gsnaker.engine.entity.HistoryOrder;
import org.gsnaker.engine.entity.HistoryTask;
import org.gsnaker.engine.entity.WorkItem;
import org.gsnaker.engine.helper.AssertHelper;
/**
 * 查询服务实现类
 * @author hoocen
 * @since 1.0
 */
public class QueryService extends AccessService implements IQueryService {

	public Order getOrder(String orderId) {
		return access().getOrder(orderId);
	}
	
	public Task getTask(String taskId) {
		return access().getTask(taskId);
	}
	
	public String[] getTaskActorsByTaskId(String taskId) {
		List<TaskActor> actors = access().getTaskActorsByTaskId(taskId);
		if(actors == null || actors.isEmpty()) return null;
		String[] actorIds = new String[actors.size()];
		for(int i = 0; i < actors.size(); i++) {
			TaskActor ta = actors.get(i);
			actorIds[i] = ta.getActorId();
		}
		return actorIds;
	}
	
	public String[] getHistoryTaskActorsByTaskId(String taskId) {
		List<HistoryTaskActor> actors = access().getHistTaskActorsByTaskId(taskId);
		if(actors == null || actors.isEmpty()) return null;
		String[] actorIds = new String[actors.size()];
		for(int i = 0; i < actors.size(); i++) {
			HistoryTaskActor ta = actors.get(i);
			actorIds[i] = ta.getActorId();
		}
		return actorIds;
	}
	
	public HistoryOrder getHistOrder(String orderId) {
		return access().getHistOrder(orderId);
	}

	public HistoryTask getHistTask(String taskId) {
		return access().getHistTask(taskId);
	}
	
	public List<Task> getActiveTasks(QueryFilter filter) {
		AssertHelper.notNull(filter);
		return access().getActiveTasks(null, filter);
	}
	
	public List<Task> getActiveTasks(Page<Task> page, QueryFilter filter) {
		AssertHelper.notNull(filter);
		return access().getActiveTasks(page, filter);
	}
	
	public List<Order> getActiveOrders(QueryFilter filter) {
		AssertHelper.notNull(filter);
		return access().getActiveOrders(null, filter);
	}
	
	public List<Order> getActiveOrders(Page<Order> page, QueryFilter filter) {
		AssertHelper.notNull(filter);
		return access().getActiveOrders(page, filter);
	}
	
	public List<HistoryOrder> getHistoryOrders(QueryFilter filter) {
		AssertHelper.notNull(filter);
		return access().getHistoryOrders(null, filter);
	}

	public List<HistoryOrder> getHistoryOrders(Page<HistoryOrder> page, QueryFilter filter) {
		AssertHelper.notNull(filter);
		return access().getHistoryOrders(page, filter);
	}

	public List<HistoryTask> getHistoryTasks(QueryFilter filter) {
		AssertHelper.notNull(filter);
		return access().getHistoryTasks(null, filter);
	}

	public List<HistoryTask> getHistoryTasks(Page<HistoryTask> page, QueryFilter filter) {
		AssertHelper.notNull(filter);
		return access().getHistoryTasks(page, filter);
	}
	
	public List<WorkItem> getWorkItems(Page<WorkItem> page, QueryFilter filter) {
		AssertHelper.notNull(filter);
		return access().getWorkItems(page, filter);
	}
	
	public List<HistoryOrder> getCCWorks(Page<HistoryOrder> page, QueryFilter filter) {
		AssertHelper.notNull(filter);
		return access().getCCWorks(page, filter);
	}

	public List<WorkItem> getHistoryWorkItems(Page<WorkItem> page, QueryFilter filter) {
		AssertHelper.notNull(filter);
		return access().getHistoryWorkItems(page, filter);
	}

	public <T> T nativeQueryObject(Class<T> T, String sql, Object... args) {
		AssertHelper.notEmpty(sql);
		AssertHelper.notNull(T);
		return access().queryObject(T, sql, args);
	}

	public <T> List<T> nativeQueryList(Class<T> T, String sql, Object... args) {
		AssertHelper.notEmpty(sql);
		AssertHelper.notNull(T);
		return access().queryList(T, sql, args);
	}

	public <T> List<T> nativeQueryList(Page<T> page, Class<T> T, String sql,
			Object... args) {
		AssertHelper.notEmpty(sql);
		AssertHelper.notNull(T);
		return access().queryList(page, new QueryFilter(), T, sql, args);
	}
}
