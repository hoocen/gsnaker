package org.gsnaker.engine.core;

import java.util.List;
import java.util.Map;

import org.gsnaker.engine.IOrderService;
import org.gsnaker.engine.core.AccessService;
import org.gsnaker.engine.entity.Order;
import org.gsnaker.engine.entity.Process;
import org.gsnaker.engine.SnakerEngine;
import org.gsnaker.engine.helper.DateHelper;
import org.gsnaker.engine.helper.JsonHelper;
import org.gsnaker.engine.helper.StringHelper;
import org.gsnaker.engine.model.ProcessModel;
import org.gsnaker.engine.entity.CCOrder;
import org.gsnaker.engine.entity.HistoryOrder;
import org.gsnaker.engine.helper.AssertHelper;
import org.gsnaker.engine.Completion;
import org.gsnaker.engine.access.QueryFilter;
import org.gsnaker.engine.core.ServiceContext;
import org.gsnaker.engine.entity.Task;
import org.gsnaker.engine.entity.HistoryTask;
/**
 * 流程实例业务类
 * @author hoocen
 * @since 1.0
 */
public class OrderService extends AccessService implements IOrderService{

	@Override
	public Order createOrder(Process process, String operator,Map<String, Object> args) {
		return createOrder(process, operator, args, null, null);
	}

	@Override
	public Order createOrder(Process process, String operator,
			Map<String, Object> args, String parentId, String parentNodeName) {
		Order order = new Order();
		order.setId(StringHelper.getPrimaryKey());
		order.setParentId(parentId);
		order.setParentNodeName(parentNodeName);
		order.setCreateTime(DateHelper.getTime());
		order.setLastUpdateTime(order.getCreateTime());
		order.setCreator(operator);
		order.setLastUpdator(order.getCreator());
		order.setProcessId(process.getId());
		ProcessModel model = process.getModel();
		if(model != null && args != null) {
			if(StringHelper.isNotEmpty(model.getExpireTime())) {
				String expireTime = DateHelper.parseTime(args.get(model.getExpireTime()));
				order.setExpireTime(expireTime);
			}
            String orderNo = (String)args.get(SnakerEngine.ID);
            if(StringHelper.isNotEmpty(orderNo)) {
                order.setOrderNo(orderNo);
            } else {
                order.setOrderNo(model.getGenerator().generate(model));
            }
		}

		order.setVariable(JsonHelper.toJson(args));
		saveOrder(order);
		return order;
	}

	@Override
	public void addVariable(String orderId, Map<String, Object> args) {
		Order order = access().getOrder(orderId);
        Map<String, Object> data = order.getVariableMap();
        data.putAll(args);
        order.setVariable(JsonHelper.toJson(data));
        access().updateOrderVariable(order);
	}

	@Override
	public void createCCOrder(String orderId, String creator, String... actorIds) {
		for(String actorId : actorIds) {
			CCOrder ccorder = new CCOrder();
			ccorder.setOrderId(orderId);
			ccorder.setActorId(actorId);
            ccorder.setCreator(creator);
			ccorder.setStatus(STATE_ACTIVE);
            ccorder.setCreateTime(DateHelper.getTime());
			access().saveCCOrder(ccorder);
		}
	}

	@Override
	public void complete(String orderId) {
		Order order = access().getOrder(orderId);
		HistoryOrder history = access().getHistOrder(orderId);
		history.setOrderState(STATE_FINISH);
		history.setEndTime(DateHelper.getTime());
		
		access().updateHistory(history);
		access().deleteOrder(order);
        Completion completion = getCompletion();
        if(completion != null) {
            completion.complete(history);
        }
	}

	@Override
	public void saveOrder(Order order) {
		HistoryOrder history = new HistoryOrder(order);
		history.setOrderState(STATE_ACTIVE);
		access().saveOrder(order);
		access().saveHistory(history);
	}

	@Override
	public void terminate(String orderId) {
		terminate(orderId, null);
	}

	@Override
	public void terminate(String orderId, String operator) {
		SnakerEngine engine = ServiceContext.getEngine();
		List<Task> tasks = engine
				.query()
				.getActiveTasks(new QueryFilter().setOrderId(orderId));
		for(Task task : tasks) {
			engine.task().complete(task.getId(), operator);
		}
		Order order = access().getOrder(orderId);
		HistoryOrder history = new HistoryOrder(order);
		history.setOrderState(STATE_TERMINATION);
		history.setEndTime(DateHelper.getTime());
		
		access().updateHistory(history);
		access().deleteOrder(order);
        Completion completion = getCompletion();
        if(completion != null) {
            completion.complete(history);
        }
	}

	@Override
	public Order resume(String orderId) {
		HistoryOrder historyOrder = access().getHistOrder(orderId);
        Order order = historyOrder.undo();
        access().saveOrder(order);
        historyOrder.setOrderState(STATE_ACTIVE);
        access().updateHistory(historyOrder);

        SnakerEngine engine = ServiceContext.getEngine();
        List<HistoryTask> histTasks = access().getHistoryTasks(null,
                new QueryFilter().setOrderId(orderId));
        if(histTasks != null && !histTasks.isEmpty()) {
            HistoryTask histTask = histTasks.get(0);
            engine.task().resume(histTask.getId(), histTask.getOperator());
        }
        return order;
	}

	@Override
	public void updateOrder(Order order) {
		access().updateOrder(order);
	}

	@Override
	public void updateCCStatus(String orderId, String... actorIds) {
		List<CCOrder> ccorders = access().getCCOrder(orderId, actorIds);
        AssertHelper.notNull(ccorders);
        for(CCOrder ccorder : ccorders) {
            ccorder.setStatus(STATE_FINISH);
            ccorder.setFinishTime(DateHelper.getTime());
            access().updateCCOrder(ccorder);
        }
	}

	@Override
	public void deleteCCOrder(String orderId, String actorId) {
		List<CCOrder> ccorders = access().getCCOrder(orderId, actorId);
		AssertHelper.notNull(ccorders);
        for(CCOrder ccorder : ccorders) {
		    access().deleteCCOrder(ccorder);
        }
	}

	@Override
	public void cascadeRemove(String id) {
		HistoryOrder historyOrder = access().getHistOrder(id);
		AssertHelper.notNull(historyOrder);
		List<Task> activeTasks = access().getActiveTasks(null, new QueryFilter().setOrderId(id));
		List<HistoryTask> historyTasks = access().getHistoryTasks(null, new QueryFilter().setOrderId(id));
		for(Task task : activeTasks) {
			access().deleteTask(task);
		}
		for(HistoryTask historyTask : historyTasks) {
			access().deleteHistoryTask(historyTask);
		}
		List<CCOrder> ccOrders = access().getCCOrder(id);
		for(CCOrder ccOrder : ccOrders) {
			access().deleteCCOrder(ccOrder);
		}

		Order order = access().getOrder(id);
		access().deleteHistoryOrder(historyOrder);
		if(order != null) {
			access().deleteOrder(order);
		}
	}

}
