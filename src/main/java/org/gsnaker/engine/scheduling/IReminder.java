package org.gsnaker.engine.scheduling;

import java.util.Map;

import org.gsnaker.engine.entity.Process;
import org.gsnaker.engine.model.NodeModel;

/**
 * 提醒接口
 * @author hoocen
 * @since 1.0
 */
public interface IReminder {
	/**
     * 提醒操作
     * @param process 流程定义对象
     * @param orderId 流程实例id
     * @param taskId 任务id
     * @param nodeModel 节点模型
     * @param data 数据
     */
    void remind(Process process, String orderId,
                String taskId, NodeModel nodeModel, Map<String, Object> data);
}
