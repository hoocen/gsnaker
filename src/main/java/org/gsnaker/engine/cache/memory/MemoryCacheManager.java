package org.gsnaker.engine.cache.memory;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.gsnaker.engine.cache.Cache;
import org.gsnaker.engine.cache.CacheException;
import org.gsnaker.engine.cache.CacheManager;
import org.gsnaker.engine.helper.StringHelper;

/**
 * 基于虚拟机内存的cache管理器
 * @author hoocen
 * @since 1.0
 */
public class MemoryCacheManager implements CacheManager {

	private final ConcurrentMap<String, Cache> caches;
	
	public MemoryCacheManager() {
		this.caches = new ConcurrentHashMap<String, Cache>();
	}
	public <K, V> Cache<K, V> getCache(String name) throws CacheException {
		if(StringHelper.isEmpty(name)) {
			throw new IllegalArgumentException("Cache名称不能为空.");
		}
        Cache cache;

        cache = caches.get(name);
        if (cache == null) {
            cache = new MemoryCache<Object, Object>(new ConcurrentHashMap<Object, Object>());
            Cache existing = caches.putIfAbsent(name, cache);
            if (existing != null) {
                cache = existing;
            }
        }
        return cache;
	}

    public void destroy() throws CacheException {
        while (!caches.isEmpty()) {
            caches.clear();
        }
    }
}
