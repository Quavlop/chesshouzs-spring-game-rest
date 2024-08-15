package com.chesshouzs.server.repository;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.List;

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

    public Map<String, String> hmget(String key, List<String> fields) {
        List<String> values = hashOperations.multiGet(key, fields);
        return fields.stream().collect(Collectors.toMap(field -> field, field -> values.get(fields.indexOf(field))));
    }
}