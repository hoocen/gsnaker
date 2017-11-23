package org.gsnaker.engine.cache;
/**
 * Cache管理标识接口
 * @author hoocen
 * @since
 */
public interface CacheManagerAware {
	/**
	 * 设置cache管理器
	 * @param cacheManager
	 */
	void setCacheManager(CacheManager cacheManager);
}
