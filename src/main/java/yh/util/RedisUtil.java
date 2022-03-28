package yh.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @projectName: webtags
 * @package: yh.util
 * @className: RedisUtil
 * @author: Ethan
 * @description: TODO
 * @version: 1.0
 */

@Component
public class RedisUtil {
    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 判断redis是否存在该key
     * @param key 键
     * @return    true存在 false不存在
     */
    public boolean hasKey(String key) {
        try{
            return redisTemplate.hasKey(key);
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 普通缓存放入
     * @param key   键
     * @param value 值
     * @return      true成功 false失败
     */
    public boolean set(String key, Object value) {
        try {
            redisTemplate.opsForValue().set(key, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    /**
     * 普通缓存放入并设置时间
     * @param key   键
     * @param value 值
     * @param time  时间(秒) time要大于0 如果time小于等于0 将设置无限期
     * @return      true成功 false失败
     */
    public boolean set(String key, Object value, long time) {
        try {
            redisTemplate.opsForValue().set(key, value, time, TimeUnit.SECONDS);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 普通缓存获取
     * @param key 键
     * @return    值
     */
    public Object get(String key) {
        return key == null ? null : redisTemplate.opsForValue().get(key);
    }
}