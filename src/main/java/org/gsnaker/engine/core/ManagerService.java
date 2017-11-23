package org.gsnaker.engine.core;

import java.util.List;

import org.gsnaker.engine.IManagerService;
import org.gsnaker.engine.access.Page;
import org.gsnaker.engine.access.QueryFilter;
import org.gsnaker.engine.entity.Surrogate;
import org.gsnaker.engine.helper.AssertHelper;
import org.gsnaker.engine.helper.DateHelper;
import org.gsnaker.engine.helper.StringHelper;

/**
 * 管理服务类
 * @author yuqs
 * @since 1.4
 */
public class ManagerService extends AccessService implements IManagerService {
	public void saveOrUpdate(Surrogate surrogate) {
		AssertHelper.notNull(surrogate);
		surrogate.setState(STATE_ACTIVE);
		if(StringHelper.isEmpty(surrogate.getId())) {
			surrogate.setId(StringHelper.getPrimaryKey());
			access().saveSurrogate(surrogate);
		} else {
			access().updateSurrogate(surrogate);
		}
	}

	public void deleteSurrogate(String id) {
		Surrogate surrogate = getSurrogate(id);
		AssertHelper.notNull(surrogate);
		access().deleteSurrogate(surrogate);
	}

	public Surrogate getSurrogate(String id) {
		return access().getSurrogate(id);
	}
	
	public List<Surrogate> getSurrogate(QueryFilter filter) {
		AssertHelper.notNull(filter);
		return access().getSurrogate(null, filter);
	}

	public List<Surrogate> getSurrogate(Page<Surrogate> page, QueryFilter filter) {
		AssertHelper.notNull(filter);
		return access().getSurrogate(page, filter);
	}
	
	public String getSurrogate(String operator, String processName) {
		AssertHelper.notEmpty(operator);
		QueryFilter filter = new QueryFilter().
				setOperator(operator).
				setOperateTime(DateHelper.getTime());
		if(StringHelper.isNotEmpty(processName)) {
			filter.setName(processName);
		}
		List<Surrogate> surrogates = getSurrogate(filter);
		if(surrogates == null || surrogates.isEmpty()) return operator;
		StringBuffer buffer = new StringBuffer(50);
		for(Surrogate surrogate : surrogates) {
			String result = getSurrogate(surrogate.getSurrogate(), processName);
			buffer.append(result).append(",");
		}
		buffer.deleteCharAt(buffer.length() - 1);
		return buffer.toString();
	}
}
