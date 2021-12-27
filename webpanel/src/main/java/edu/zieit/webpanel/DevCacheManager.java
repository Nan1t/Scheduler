package edu.zieit.webpanel;

import org.thymeleaf.cache.ExpressionCacheKey;
import org.thymeleaf.cache.ICache;
import org.thymeleaf.cache.ICacheManager;
import org.thymeleaf.cache.TemplateCacheKey;
import org.thymeleaf.engine.TemplateModel;

import java.util.List;

public class DevCacheManager implements ICacheManager {

    public static final DevCacheManager INSTANCE = new DevCacheManager();

    @Override
    public ICache<TemplateCacheKey, TemplateModel> getTemplateCache() {
        return null;
    }

    @Override
    public ICache<ExpressionCacheKey, Object> getExpressionCache() {
        return null;
    }

    @Override
    public <K, V> ICache<K, V> getSpecificCache(String name) {
        return null;
    }

    @Override
    public List<String> getAllSpecificCacheNames() {
        return null;
    }

    @Override
    public void clearAllCaches() {

    }
}
