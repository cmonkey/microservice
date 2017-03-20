package com.microservice.config;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springside.modules.nosql.redis.JedisShardedTemplate;
import org.springside.modules.nosql.redis.JedisTemplate.JedisAction;
import org.springside.modules.nosql.redis.pool.JedisPool;
import org.springside.modules.nosql.redis.pool.JedisPoolBuilder;

import redis.clients.jedis.Jedis;

@Configuration
public class RedisConfig {
	private static final Logger logger = LoggerFactory.getLogger(RedisConfig.class);
	
	@Bean(name = { "RedisProperties" })
	@ConditionalOnMissingBean
	public RedisProperties redisProperties() {
		return new RedisProperties();
	}

	@Autowired
	@Qualifier("RedisProperties")
	private RedisProperties properties;
	
	@Bean
	public JedisPoolBuilder jedisPoolBuilder(){
		JedisPoolBuilder builder = new JedisPoolBuilder();

        builder.setDirectHost(properties.getDirectHost());

        String password = properties.getPassword();

        if(StringUtils.isNotBlank(password)){
            builder.setPassword(properties.getPassword());
        }

        builder.setTimeout(properties.getTimeout());
        builder.setPoolSize(properties.getPoolSize());
        builder.setPoolName(properties.getPoolName());
        
        return builder;
	}
	
	@Bean
	public JedisPool jedisPool(){
		
		JedisPool jedisPool = jedisPoolBuilder().buildPool();
		
		return jedisPool;
	}
	
	@Bean(name = "jedisShardedTemplate")
	@ConditionalOnMissingBean(name = {"jedisShardedTemplate"})
	public JedisShardedTemplate jedisSharedTemplate(){
		
		JedisShardedTemplate jedisShardedTemplate = new JedisShardedTemplate(jedisPool());

		final String key = "ping";
		String ping = jedisShardedTemplate.execute(key, new JedisAction<String>(){
			
			@Override
			public String action(Jedis jedis){
				return jedis.ping();
			}
		});
		
		boolean isPing = (ping != null) && ping.equals("PONG");
		if (!isPing) {
			logger.error("RedisConfig jedisSharedTemplate Exception ping is error");
			throw new java.lang.NullPointerException(
	                    "RedisConfig jedisSharedTemplate ping is error");
		}

		return jedisShardedTemplate;
	}
}
