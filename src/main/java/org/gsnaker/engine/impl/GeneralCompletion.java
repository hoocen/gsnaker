package org.gsnaker.engine.impl;

import org.gsnaker.engine.Completion;
import org.gsnaker.engine.entity.HistoryOrder;
import org.gsnaker.engine.entity.HistoryTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 默认的任务、实例完成时触发的动作
 * @author hoocen
 * @since 1.0
 */
public class GeneralCompletion implements Completion{

	private static final Logger log = LoggerFactory.getLogger(GeneralCompletion.class);

    public void complete(HistoryTask task) {
        log.info("The task[{}] has been user[{}] has completed", task.getId(), task.getOperator());
    }

    public void complete(HistoryOrder order) {
        log.info("The order[{}] has completed", order.getId());
    }
}
