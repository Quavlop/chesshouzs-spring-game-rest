package com.chesshouzs.server.repository;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Repository;

import com.chesshouzs.server.model.redis.GameMove;

@Repository
public class RedisBaseRepository {

    private RedisTemplate<String, String> redisTemplate;
    private HashOperations<String, String, String> hashOperations;
    private ZSetOperations<String, String> zSetOperations;

    @Autowired
    public RedisBaseRepository(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
        this.hashOperations = redisTemplate.opsForHash();
        this.zSetOperations = redisTemplate.opsForZSet();
    }

    public Map<String, String> hgetall(String key) {
        return hashOperations.entries(key);
    }
}