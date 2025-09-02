package com.yupi.springbootinit.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
     public class RedisConfig {
     
         @Bean
         public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
             RedisTemplate<String, Object> template = new RedisTemplate<>();
             template.setConnectionFactory(connectionFactory);
     
             // 使用 JSON 序列化方式（推荐）
             GenericJackson2JsonRedisSerializer serializer = new GenericJackson2JsonRedisSerializer();
     
             // key 采用 String 序列化方式
             template.setKeySerializer(new StringRedisSerializer());
             template.setHashKeySerializer(new StringRedisSerializer());
     
             // value 采用 JSON 序列化方式
             template.setValueSerializer(serializer);
             template.setHashValueSerializer(serializer);
     
             template.afterPropertiesSet();
             return template;
         }
     }
     