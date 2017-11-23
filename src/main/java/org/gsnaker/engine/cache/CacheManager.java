package org.gsnaker.engine.cache;

/**
 * 缓存管理器接口
 * @author hoocen
 * @since
 */
public interface CacheManager {

	/**
	 * 根据cache的名称获取cache。如果不存在，默认新建并返回
	 * @param name
	 * @return Cache
	 * @throws CacheException
	 */
	public <K, V> Cache<K, V> getCache(String name) throws CacheException;

    /**
     * 销毁cache
     * @throws CacheException
     */
    public void destroy() throws CacheException;
}
