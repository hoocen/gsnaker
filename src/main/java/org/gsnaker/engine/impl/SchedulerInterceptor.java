package org.gsnaker.engine.impl;

import java.util.Date;
import java.util.Map;

import org.apache.commons.lang3.math.NumberUtils;
import org.gsnaker.engine.SnakerInterceptor;
import org.gsnaker.engine.core.Execution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.gsnaker.engine.core.ServiceContext;
import org.gsnaker.engine.entity.Task;
import org.gsnaker.engine.model.TaskModel;
import org.gsnaker.engine.scheduling.IScheduler;
import org.gsnaker.engine.scheduling.JobEntity;
import org.gsnaker.engine.scheduling.JobEntity.JobType;
/**
 * 时限控制拦截器
 * 主要拦截任务的expireDate(期望完成时间)
 * 再交给具体的调度器完成调度处理
 * @author hoocen
 * @since
 */
public class SchedulerInterceptor implements SnakerInterceptor{
	private static final Logger log = LoggerFactory.getLogger(SchedulerInterceptor.class);
	/**
	 * 调度器接口
	 */
	private IScheduler scheduler;
	/**
	 * 是否调度
	 */
	private boolean isScheduled = true;
	/**
	 * 时限控制拦截方法
	 */
	public void intercept(Execution execution) {
		if(!isScheduled) return;
		for(Task task : execution.getTasks()) {
			String id = execution.getProcess().getId() 
					+ "-" + execution.getOrder().getId() 
					+ "-" + task.getId();
			Date expireDate = task.getExpireDate();
			if(expireDate != null) {
				schedule(id, task, expireDate, JobType.EXECUTER.ordinal(), execution.getArgs());
			}
			Date remindDate = task.getRemindDate();
			if(remindDate != null) {
				schedule(id, task, remindDate, JobType.REMINDER.ordinal(), execution.getArgs());
			}
		}
	}
	
	public void schedule(String id, Task task, Date startDate, int jobType, Map<String, Object> args) {
		try {
		    JobEntity entity = new JobEntity(id, task, startDate, args);
		    entity.setModelName(task.getTaskName());
		    entity.setJobType(jobType);
		    if(jobType == JobType.REMINDER.ordinal()) {
		    	TaskModel model = (TaskModel)task.getModel();
		    	if(model != null && NumberUtils.isNumber(model.getReminderRepeat())) {
		    		entity.setPeriod(Integer.parseInt(model.getReminderRepeat()));
		    	}
		    }
		    schedule(entity);
		} catch (Exception e) {
			log.error(e.getMessage());
			log.info("scheduler failed.task is:" + task);
		}
	}
	
	private void schedule(JobEntity entity) {
	    if(scheduler == null) {
	    	scheduler = ServiceContext.getContext().find(IScheduler.class);
	    }
	    if(scheduler != null) {
	    	scheduler.schedule(entity);
	    } else {
	    	isScheduled = false;
	    }
	}

}
