package com.microservice.service;

import com.microservice.config.Constants;
import com.microservice.dao.PlatFormDataRepository;
import com.microservice.entity.PlatFormData;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.converters.DateConverter;
import org.apache.commons.collections.MapUtils;
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

import java.lang.reflect.InvocationTargetException;
import java.util.*;

import com.google.common.collect.Lists;
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
    DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd");
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

    private static void addCache(Jedis jedis, String cacheKey, Constants constants, Object value){
        jedis.hset(cacheKey, constants.name(), String.valueOf(value));
    }

    private static PlatFormData getCacheData(Map<String, String> map){
        PlatFormData data = new PlatFormData();

        try {
            DateConverter converter = new DateConverter();
            converter.setPattern("yyyy-MM-dd HH:mm:ss.SSS");
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

                            String objectKey = PLAT_FORM_DATA_OBJECT_KEY + days;

                            jedis.zadd(PLAT_FORM_DATA_KEY, days, objectKey);

                            addCache(jedis, objectKey, Constants.id, data.getId());
                            addCache(jedis, objectKey, Constants.annualSum, data.getAnnualSum());
                            addCache(jedis, objectKey, Constants.bankCount, data.getBankCount());
                            addCache(jedis, objectKey, Constants.cashSum, data.getCashSum());
                            addCache(jedis, objectKey, Constants.cashWithSum, data.getCashWithSum());
                            addCache(jedis, objectKey, Constants.collectSum, data.getCollectSum());
                            addCache(jedis, objectKey, Constants.createTime, data.getCreateTime());
                            addCache(jedis, objectKey, Constants.interestSum, data.getInterestSum());
                            addCache(jedis, objectKey, Constants.investConvert, data.getInvestConvert());
                            addCache(jedis, objectKey, Constants.investCount, data.getInvestCount());
                            addCache(jedis, objectKey, Constants.investCountOrDay, data.getInvestCountOrDay());
                            addCache(jedis, objectKey, Constants.originInvestCount, data.getOriginInvestCount());
                            addCache(jedis, objectKey, Constants.originInvestSum, data.getOriginInvestSum());
                            addCache(jedis, objectKey, Constants.redSum, data.getRedSum());
                            addCache(jedis, objectKey, Constants.regCount, data.getRegCount());
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
