package org.gsnaker.engine.handlers.impl;

import java.util.List;

import org.gsnaker.engine.IQueryService;
import org.gsnaker.engine.access.QueryFilter;
import org.gsnaker.engine.core.Execution;
import org.gsnaker.engine.entity.Order;
import org.gsnaker.engine.entity.Task;
import org.gsnaker.engine.handlers.IHandler;
import org.gsnaker.engine.model.ProcessModel;
import org.gsnaker.engine.model.SubProcessModel;
import org.gsnaker.engine.model.TaskModel;
/**
 * 合并处理的抽象处理器
 * 需要子类提供查询无法合并的task集合的参数map
 * @author hoocen
 * @since 1.0
 */
public abstract class AbstractMergeHandler implements IHandler{

	@Override
	public void handle(Execution execution) {
		/**
		 * 查询当前流程实例中是否有需要合并的node列表
		 * 若所有中间node都完成，则设置为已合并状态，告诉model可继续执行join的输出变迁
		 */
		IQueryService query = execution.getEngine().query();
		Order order = execution.getOrder();
		ProcessModel model = execution.getModel();
		String[] activeNodes = findActiveNodes();
		boolean isSubProcessMerged = false;
		boolean isTaskMerged = false;
		if(model.containsNodeNames(SubProcessModel.class, activeNodes)){
			QueryFilter filter = new QueryFilter().setParentId(order.getId())
					.setExcludedIds(new String[]{execution.getChildOrderId()});
			List<Order> orders = query.getActiveOrders(filter);
			if(orders == null || orders.isEmpty()){
				isSubProcessMerged = true;
			}
		}else{
			isSubProcessMerged = true;
		}
		
		if(isSubProcessMerged && model.containsNodeNames(TaskModel.class, activeNodes)){
			QueryFilter filter = new QueryFilter().setOrderId(order.getId())
					.setExcludedIds(new String[]{execution.getTask().getId()})
					.setNames(activeNodes);
			List<Task> tasks = query.getActiveTasks(filter);
			if(tasks == null || tasks.isEmpty()){
				isTaskMerged = true;
			}
		}
		execution.setMerged(isSubProcessMerged && isTaskMerged);
	}
	/**
	 * 子类提供查询当前活动节点的方法
	 * @return String[] 活动节点
 	 */
	protected abstract String[] findActiveNodes();
}
