package com.microservice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
@ConfigurationProperties
public class RedisProperties {

	@Value("${redis.minIdle}")
	private int minIdle = 256; 

	@Value("${redis.maxIdle}")
	private int maxIdle = 1024;
	
	@Value("${redis.maxWaitMillis}")
	private int maxWaitMillis = 5 * 1000;
	
	@Value("${redis.host}")
	private String host = "localhsot";
	
	@Value("${redis.port}")
	private int port = 6379;
	
	@Value("${redis.password}")
	private String password;
	
	@Value("${redis.timeout}")
	private int timeout = 5000;
	
	@Value("${redis.poolSize}")
	private int poolSize = 1024;
	
	@Value("${redis.poolName}")
	private String poolName ;
	
	private String directHost;

	public int getMinIdle() {
		return minIdle;
	}

	public void setMinIdle(int minIdle) {
		this.minIdle = minIdle;
	}

	public int getMaxIdle() {
		return maxIdle;
	}

	public void setMaxIdle(int maxIdle) {
		this.maxIdle = maxIdle;
	}

	public int getMaxWaitMillis() {
		return maxWaitMillis;
	}

	public void setMaxWaitMillis(int maxWaitMillis) {
		this.maxWaitMillis = maxWaitMillis;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getTimeout() {
		return timeout;
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

	public int getPoolSize() {
		return poolSize;
	}

	public void setPoolSize(int poolSize) {
		this.poolSize = poolSize;
	}

	public String getPoolName() {
		return poolName;
	}

	public void setPoolName(String poolName) {
		this.poolName = poolName;
	}

	public String getDirectHost() {
		return host + ":" + port;
	}
	
	
}
