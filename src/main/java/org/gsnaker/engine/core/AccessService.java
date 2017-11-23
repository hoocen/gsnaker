package org.gsnaker.engine.core;

import org.gsnaker.engine.Completion;
import org.gsnaker.engine.DBAccess;
import org.gsnaker.engine.core.ServiceContext;
import org.gsnaker.engine.impl.GeneralCompletion;

/**
 * 数据访问抽象父类
 * @author hoocen
 * @since 1.0
 */
public abstract class AccessService {

	/**
	 * 状态；活动状态
	 */
	public static final Integer STATE_ACTIVE = 1;
	/**
	 * 状态：结束状态
	 */
	public static final Integer STATE_FINISH = 0;
	/**
	 * 状态：终止状态
	 */
	public static final Integer STATE_TERMINATION = 2;
	/**
	 * 数据库的access
	 */
	protected DBAccess access;
    /**
     * 完成触发接口
     */
    private Completion completion = null;
	/**
	 * 获取DBAccess，供子类使用
	 */
	public DBAccess access() {
		return access;
	}
	/**
	 * setter
	 * @param access 访问对象
	 */
	public void setAccess(DBAccess access) {
		this.access = access; 
	}

    /**
     * setter
     * @param completion 完成对象
     */
    public void setCompletion(Completion completion) {
        this.completion = completion;
    }
    public Completion getCompletion() {
        if(completion != null) {
            return completion;
        }

        completion = ServiceContext.find(Completion.class);
        if(completion == null) {
            ServiceContext.put(Completion.class.getName(), GeneralCompletion.class);
            completion = ServiceContext.find(Completion.class);
        }
        return completion;
    }
}
