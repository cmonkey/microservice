package com.microservice.service;

import com.microservice.config.Constants;
import com.microservice.dao.PlatFormDataRepository;
import com.microservice.entity.PlatFormData;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springside.modules.nosql.redis.JedisShardedTemplate;
import org.springside.modules.nosql.redis.JedisShardedTemplate;
import org.springside.modules.nosql.redis.JedisTemplate;
import org.springside.modules.utils.Collections3;
import redis.clients.jedis.Jedis;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.joda.time.Days;
import com.google.common.collect.Lists;
/**
 * Created by cmonkey on 3/20/17.
 */
@Service
public class PlatFormService {
    private final static String PLAT_FORM_DATA_KEY = "plat_data";
    private final static String PLAT_FORM_DATA_OBJECT_KEY = "plat_data_object_";
    private final static DateTime defaultDt = new DateTime().withYear(2016).withMonthOfYear(1).withDayOfMonth(1).withTime(0,0,0,000);

    @Autowired
    JedisShardedTemplate jedisShardedTemplate;
    private static final Logger logger = LoggerFactory.getLogger(PlatFormService.class);
    DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd");
    @Autowired
    PlatFormDataRepository platFormDataRepository;

    public List<PlatFormData> getPlatFormData(boolean isReadDb, DateTime startDt, DateTime endDt){

        if(isReadDb){
            List<PlatFormData> list = platFormDataRepository.findAll();

            if(Collections3.isNotEmpty(list)){
                for (PlatFormData data: list) {
                    Date createTime = data.getCreateTime();
                    DateTime dt = new DateTime(createTime.getTime());
                    final int days = Days.daysBetween(defaultDt, dt).getDays();

                    jedisShardedTemplate.execute(PLAT_FORM_DATA_KEY, new JedisTemplate.JedisActionNoResult(){

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
                            addCache(jedis, objectKey, Constants.regCount,data.getRegCount());
                        }
                    });
                }
            }

            return list;
        }

        final int startDays = Days.daysBetween(defaultDt, startDt).getDays();
        final int endDays = Days.daysBetween(defaultDt, endDt).getDays();

        return jedisShardedTemplate.execute(PLAT_FORM_DATA_KEY, new JedisTemplate.JedisAction<List<PlatFormData>>() {
            @Override
            public List<PlatFormData> action(Jedis jedis) {

                List<PlatFormData> list = Lists.newArrayList();

                Set<String> set = jedis.zrevrangeByScore(PLAT_FORM_DATA_KEY, endDays, startDays);

                if(Collections3.isNotEmpty(set)){

                    for(String str : set){

                        logger.info("cache object key = {}", str);
                        Map<String, String> map = jedis.hgetAll(str);

                        if(null != map && !map.isEmpty()) {

                            PlatFormData platFormData = getCacheData(map);

                            list.add(platFormData);
                        }
                    }
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

        data.setId(Integer.valueOf(map.get(Constants.id.name())));
        data.setAnnualSum(BigDecimal.valueOf(Double.valueOf(map.get(Constants.annualSum.name()))));

        return data;
    }
}