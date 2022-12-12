package com.hakutaku.service.api.service;

import com.hakutaku.service.api.common.SymbolConstants;
import com.hakutaku.service.common.config.security.TokenResolvable;
import org.apache.ibatis.mapping.ResultMap;
import org.apache.ibatis.mapping.ResultMapping;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public abstract class BaseService {

    private static final Logger log = LoggerFactory.getLogger(BaseService.class);

    private static final String SETNX_SCRIPT = "return redis.call('setnx',KEYS[1], ARGV[1])";

    @Autowired
    protected RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private SqlSessionFactory sqlSessionFactory;

    @Autowired
    protected TokenResolvable tokenResolvable;

    protected Long decrement(String key){
        return redisTemplate.opsForValue().decrement(key);
    }

    protected Long increment(String key){
        return redisTemplate.opsForValue().increment(key);
    }


    protected Object getCacheData(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    protected void setCacheData(String key, Object data) {
        redisTemplate.opsForValue().set(key, data, 60, TimeUnit.SECONDS);
    }

    protected void setCacheData(String key, Object data, int time, TimeUnit timeUnit) {
        redisTemplate.opsForValue().set(key, data, time, timeUnit);
    }

    protected Boolean deleteCacheData(String key) {
        return redisTemplate.delete(key);
    }

    protected String mapperOrderFields(Class clazz, String[] orders, int orderType) {
        return mapperOrderFields(clazz, orders, orderType, null);
    }

    protected String mapperOrderFields(Class clazz, String[] orders, int orderType, String tableAlias) {
        Configuration configuration = sqlSessionFactory.getConfiguration();
        ResultMap resultMap = configuration.getResultMap(clazz.getName()+ ".BaseResultMap");
        List<ResultMapping> mappings = resultMap.getResultMappings();
        Map<String, String> columnMapping = new HashMap<>();
        for(ResultMapping a : mappings){
            columnMapping.put(a.getProperty(), a.getColumn());
        }
        StringBuilder orderBuilder = new StringBuilder();
        String orderTypeString = orderType == 0 ? "asc" : "desc";
        for (String order : orders){
            if (columnMapping.containsKey(order)){
                if (null != tableAlias){
                    orderBuilder.append(tableAlias).append(SymbolConstants.DOT);
                }
                orderBuilder.append(columnMapping.get(order))
                        .append(SymbolConstants.BLANK)
                        .append(orderTypeString)
                        .append(SymbolConstants.COMMA);
            }
        }
        if (orderBuilder.length() > 0){
            orderBuilder.deleteCharAt(orderBuilder.length() - 1);
        }
        return orderBuilder.toString();
    }

    public boolean setNx(String key, int time, String value) {
        //自定义脚本
        DefaultRedisScript script = new DefaultRedisScript<>(SETNX_SCRIPT, List.class);
        //执行脚本,传入参数,value为pay
        List<Long> rst = (List) redisTemplate.execute(script, Collections.singletonList(key), value);
        //返回1,表示设置成功,拿到锁
        if(rst.get(0) == 1){
            log.info("get lock:{}", key);
            //设置过期时间
            redisTemplate.expire(key, time, TimeUnit.SECONDS);
            log.info("set lock {} expire time {} seconds:", key, time);
            return true;
        }else{

            return false;
        }
    }

}
