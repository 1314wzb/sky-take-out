package com.sky.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class ReisTemplateConfigure {

    //就为了干一件事: 构建一个RedisTemplate对象，交给IOC容器管理
    @Bean
    public RedisTemplate redisTemplate(RedisConnectionFactory factory){
        //1. 构建一个RedisTEmplate对象
        RedisTemplate template = new RedisTemplate();

        //2.设置序列化器
        template.setKeySerializer(new StringRedisSerializer());//键
        /*一般情况下不配置这个*/
        /*template.setValueSerializer(new StringRedisSerializer());//值

        template.setHashKeySerializer(new StringRedisSerializer());//hash的键
        template.setHashValueSerializer(new StringRedisSerializer());//hash的值*/

        //3. 设置连接工厂
        template.setConnectionFactory(factory);

        return template;
    }
}
