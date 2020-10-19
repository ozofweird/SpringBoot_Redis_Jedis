package com.example.jedistest.config;


import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.*;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RedisRepositoryConfigTest {

    @Autowired
    StringRedisTemplate redisTemplate;

    // String
    @Test
    public void testString() {
        final String key = "testString";
        final ValueOperations<String, String> stringStringValueOperations = redisTemplate.opsForValue();

        stringStringValueOperations.set(key, "1");
        final String result_1 = stringStringValueOperations.get(key);

        System.out.println("result_1 = " + result_1);

        stringStringValueOperations.increment(key);
        final String result_2 = stringStringValueOperations.get(key);

        System.out.println("result_2 = " + result_2);

        /*
        redis-cli 명령어
        - GET key
         */
    }

    // List
    @Test
    public void testList() {
        final String key = "testList";
        final ListOperations<String, String> stringStringListOperations = redisTemplate.opsForList();

        stringStringListOperations.rightPush(key, "H");
        stringStringListOperations.rightPush(key, "E");
        stringStringListOperations.rightPush(key, "L");
        stringStringListOperations.rightPush(key, "L");
        stringStringListOperations.rightPush(key, "O");

        stringStringListOperations.rightPushAll(key, " ", "W", "O", "L", "D");

        final String character_1 = stringStringListOperations.index(key, 1);

        System.out.println("character_1 = " + character_1);

        final Long size = stringStringListOperations.size(key);

        System.out.println("size = " + size);

        final List<String> resultRange = stringStringListOperations.range(key, 0,9);

        assert resultRange != null;
        System.out.println("resultRange = " + Arrays.toString(resultRange.toArray()));

        /*
        redis-cli 명령어
        - INDEX key index
        - LRANGE key start stop
         */
    }

    // Set
    @Test
    public void testSet() {
        final String key = "testSet";
        SetOperations<String, String> stringStringSetOperations = redisTemplate.opsForSet();

        stringStringSetOperations.add(key, "H");
        stringStringSetOperations.add(key, "E");
        stringStringSetOperations.add(key, "L");
        stringStringSetOperations.add(key, "L");
        stringStringSetOperations.add(key, "O");

        Set<String> test =  stringStringSetOperations.members(key);

        assert test != null;
        System.out.println("member = " + Arrays.toString(test.toArray()));

        final Long size = stringStringSetOperations.size(key);

        System.out.println("size = " + size);

        Cursor<String> cursor = stringStringSetOperations.scan(key, ScanOptions.scanOptions().match("*").count(3).build());

        while(cursor.hasNext()) {
            System.out.println("cursor = " + cursor.next());
        }

        /*
        redis-cli 명령어
        - SMEMBERS key
         */
    }

    // Sorted Set
    @Test
    public void testSortedSet() {
        final String key = "testSortedSet";
        ZSetOperations<String, String> stringStringZSetOperations = redisTemplate.opsForZSet();

        stringStringZSetOperations.add(key, "H", 1);
        stringStringZSetOperations.add(key, "E", 5);
        stringStringZSetOperations.add(key, "L", 10);
        stringStringZSetOperations.add(key, "L", 15);
        stringStringZSetOperations.add(key, "O", 20);

        Set<String> range = stringStringZSetOperations.range(key, 0, 5);

        assert range != null;
        System.out.println("range = " + Arrays.toString(range.toArray()));

        final Long size = stringStringZSetOperations.size(key);

        System.out.println("size = " + size);

        Set<String> scoreRange = stringStringZSetOperations.rangeByScore(key, 0, 13);

        assert scoreRange != null;
        System.out.println("scoreRange = " + Arrays.toString(scoreRange.toArray()));

        /*
        redis-cli 명령어
        - ZRANGE key start stop [WITHSCORES]
        - ZRANGEBYSCORE key min max [WITHSCORES]
         */
    }

    // Hash
    @Test
    public void testHash() {
        final String key = "testHash";
        HashOperations<String, Object, Object> stringObjectObjectHashOperations = redisTemplate.opsForHash();

        stringObjectObjectHashOperations.put(key, "Hello", "testHash");
        stringObjectObjectHashOperations.put(key, "Hello2", "testHash2");
        stringObjectObjectHashOperations.put(key, "Hello3", "testHash3");

        Object hello = stringObjectObjectHashOperations.get(key, "Hello");

        System.out.println("hello = " + hello);

        Map<Object, Object> entries = stringObjectObjectHashOperations.entries(key);

        System.out.println("entries = " + entries.get("Hello2"));

        Long size = stringObjectObjectHashOperations.size(key);

        System.out.println("size = " + size);

        /*
        redis-cli 명령어
        - HGET key field
         */
    }

    /*
    redis-cli 키 전체 삭제 명령어
    - flushAll
    */
}