package org.gsnaker.engine.cache.memory;

import java.util.Map;

import org.gsnaker.engine.cache.Cache;
import org.gsnaker.engine.cache.CacheException;
import org.gsnaker.engine.helper.AssertHelper;

/**
 * 基于内存管理cache
 * @author hoocen
 * @since 1.0
 */
public class MemoryCache<K, V> implements Cache<K, V> {
	/**
	 * map cache
	 */
	private final Map<K, V> map;
	/**
	 * 通过Map实现类构造MemoryCache
	 * @param backingMap
	 */
	public MemoryCache(Map<K, V> backingMap) {
		AssertHelper.notNull(backingMap);
		this.map = backingMap;
	}
	
	public V get(K key) throws CacheException {
		return map.get(key);
	}

	public V put(K key, V value) throws CacheException {
		return map.put(key, value);
	}

	public V remove(K key) throws CacheException {
		return map.remove(key);
	}

	public void clear() throws CacheException {
		map.clear();
	}
}
