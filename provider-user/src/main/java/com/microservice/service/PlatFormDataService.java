package com.microservice.service;

import com.microservice.config.Constants;
import com.microservice.dao.PlatFormDataRepository;
import com.microservice.entity.PlatFormData;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.converters.DateConverter;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springside.modules.nosql.redis.JedisShardedTemplate;
import org.springside.modules.nosql.redis.JedisTemplate;
import org.springside.modules.utils.Collections3;
import redis.clients.jedis.Jedis;

import com.google.common.collect.Lists;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.*;

/**
 * Created by cmonkey on 3/20/17.
 */
@Service
public class PlatFormDataService {
    private final static String PLAT_FORM_DATA_KEY = "plat_data";
    private final static String PLAT_FORM_DATA_OBJECT_KEY = "plat_data_object_";
    private final static DateTime defaultDt = new DateTime().withYear(2016).withMonthOfYear(1).withDayOfMonth(1).withTime(0,0,0,000);

    @Autowired
    JedisShardedTemplate jedisShardedTemplate;
    private static final Logger logger = LoggerFactory.getLogger(PlatFormDataService.class);
    private final static DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
    @Autowired
    PlatFormDataRepository platFormDataRepository;

    public List<PlatFormData> getPlatFormData(DateTime startDt, DateTime endDt, int offset, int count){

        final int startDays = Days.daysBetween(defaultDt, startDt).getDays();
        final int endDays = Days.daysBetween(defaultDt, endDt).getDays();

        return jedisShardedTemplate.execute(PLAT_FORM_DATA_KEY, new JedisTemplate.JedisAction<List<PlatFormData>>() {
            @Override
            public List<PlatFormData> action(Jedis jedis) {

                List<PlatFormData> list = Lists.newArrayList();

                Set<String> set = jedis.zrevrangeByScore(PLAT_FORM_DATA_KEY, endDays, startDays, offset, count);

                if(Collections3.isNotEmpty(set)){
                    set.stream().forEach(s -> {
                        logger.info("cache object key = {}", s);
                        Map<String, String> map = jedis.hgetAll(s);

                        if(MapUtils.isNotEmpty(map)) {

                            PlatFormData platFormData = getCacheData(map);

                            list.add(platFormData);
                        }
                    });
                }

                return list;
            }
        });
    }

    private static void buildModelToCache(Jedis jedis, final String cacheKey, Object object){
        Class<?> clazz = object.getClass();
        Field[] fields = clazz.getDeclaredFields();

        for(Field field : fields){

            String fieldName = field.getName();
            String getterMethodName =  "get" + StringUtils.capitalize(fieldName);
            Method method = null;
            try {
                method = clazz.getDeclaredMethod(getterMethodName, new Class<?>[]{});
                Class<?> returnType = method.getReturnType();
                Object result = method.invoke(object, new Object[]{});

                if(null != result){
                    String v = String.valueOf(result);

                    if(returnType.equals(Date.class)){
                        Date date = (Date)result;
                        DateTime dateTime = new DateTime(date.getTime());
                        v = dateTime.toString(fmt);
                    }
                    logger.info("builderModelToCache fieldName = {}, method return Value  = {}", fieldName, v);
                    addCache(jedis, cacheKey, fieldName, v);
                }
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }

    private static void addCache(Jedis jedis, String cacheKey, String fieldName, Object value){
        jedis.hset(cacheKey, fieldName, String.valueOf(value));
    }

    private static PlatFormData getCacheData(Map<String, String> map){
        PlatFormData data = new PlatFormData();

        try {
            DateConverter converter = new DateConverter();
            //converter.setPattern("yyyy-MM-dd HH:mm:ss.SSS");
            converter.setPattern("yyyy-MM-dd HH:mm:ss");
            ConvertUtils.register(converter, Date.class);

            BeanUtils.populate(data, map);
        } catch (IllegalAccessException e) {
            logger.error("getCacheData Exception = {}", e.getMessage());
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            logger.error("getCacheData Exception = {}", e.getMessage());
            e.printStackTrace();
        }

        return data;
    }

    public boolean refreshPlatformData() {
        long startTime = System.nanoTime();

        boolean isSuccess = true;

        try {
            List<PlatFormData> list = platFormDataRepository.findAll();

            if (Collections3.isNotEmpty(list)) {
                list.stream().forEach(data -> {

                    Date createTime = data.getCreateTime();
                    DateTime dt = new DateTime(createTime.getTime());
                    final int days = Days.daysBetween(defaultDt, dt).getDays();

                    jedisShardedTemplate.execute(PLAT_FORM_DATA_KEY, new JedisTemplate.JedisActionNoResult() {

                        @Override
                        public void action(Jedis jedis) {

                            String cacheKey = PLAT_FORM_DATA_OBJECT_KEY + days;

                            jedis.zadd(PLAT_FORM_DATA_KEY, days, cacheKey);

                            buildModelToCache(jedis, cacheKey, data);
                        }
                    });
                });

            }
        }catch(Exception e){
            e.printStackTrace();
            logger.error("refreshPlatFormData Exception = {}", e);
            isSuccess = false;
        }
        logger.info("refreshPlatFormData time = {}, result = {}", (System.nanoTime() - startTime), isSuccess);

        return isSuccess;
    }
}
