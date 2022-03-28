package yh;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

@SpringBootTest
class WebtagsApplicationTests {

    @Autowired
    RedisTemplate redisTemplate;

    /**
     * 测试redis
     */
    @Test
    void TestRedis() {
        ValueOperations ops = redisTemplate.opsForValue();
        ops.set("age",21);
        System.out.println(ops.get("age"));
    }

}
