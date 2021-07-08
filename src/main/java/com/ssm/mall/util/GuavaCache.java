package com.ssm.mall.util;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class GuavaCache {

    private static final Logger logger = LoggerFactory.getLogger(GuavaCache.class);

    private static LoadingCache<String,String> tokenCache = CacheBuilder.newBuilder()

            .initialCapacity(1000)

            .maximumSize(10000)//超过最大值，则使用LRU算法删除缓存数据

            .expireAfterAccess(12, TimeUnit.HOURS)//在缓存中保留的时间12小时

            .build(new CacheLoader<String, String>() {

                @Override

                //当key没有命中缓存数据时，guava会自动调用该方法

                public String load(String s) throws Exception {

                    return null;//当key没有命中缓存数据时，返回默认值null

                }

            });

    public static void setTokenToCache(String key,String value){

        tokenCache.put(key,value);
    }

    public static String getTokenFromCache(String key){

        String value = null;

        try {

            value = tokenCache.get(key);

        } catch (ExecutionException e) {
            System.out.println(e.getCause());
            e.printStackTrace();
        }

        return value;

    }

}
