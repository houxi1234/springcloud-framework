package cn.ce.st.framework.redis;

import io.lettuce.core.RedisException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName VehicleNumConsumer
 * @Descrition TODO
 * @Author houxi
 * @Date 2019/4/8 9:53
 * Version 1.0
 **/
@Component
public class RedisService<T> {

    private static final Logger logger = LoggerFactory.getLogger(RedisService.class);

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 指定缓存失效时间
     * @param k 键
     * @param time 时间
     * @param unit 单位
     * @return
     */
    public boolean expire(String k, long time, TimeUnit unit){
        try {
            redisTemplate.expire(k, time, unit);
            return true;
        } catch (Exception e) {
            logger.error("[RedisService] set expire error, key is {}", k, e);
            return false;
        }
    }

    /**
     * 根据key 获取过期时间
     * @param k 键 不能为null
     * @return 时间(秒) 返回0代表为永久有效, -1 不存在
     */
    public long getExpire(String k, TimeUnit unit){
        try {
            return redisTemplate.getExpire(k, unit);
        } catch (RedisException e) {
            return -1;
        }

    }

    /**
     * 判断key是否存在
     * @param k 键
     * @return true 存在 false不存在
     */
    public boolean hasKey(String k){
        try {
            return redisTemplate.hasKey(k);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 删除缓存
     * @param key 可以传一个值 或多个
     */
    @SuppressWarnings("unchecked")
    public void del(String ... key){
        try {
            redisTemplate.delete(CollectionUtils.arrayToList(key));
        } catch (RedisException e) {
            e.printStackTrace();
        }

    }

    public void set(String k, Object v, long ttl) {
        try {
            redisTemplate.opsForValue().set(k, v, ttl);
        } catch (RedisException e) {
            logger.error("[RedisService] set occur error, key is {}, value is {}", k, v, e);
        }
    }

    public T get(String k) {
        try {
            return (T) redisTemplate.opsForValue().get(k);
        } catch (RedisException e) {
            logger.error("[RedisService] get occur error, key is {}", k, e);
            return null;
        }
    }

    /**
     * HashSet 并设置时间
     * @param key 键
     * @param map 对应多个键值
     * @param time 时间
     * @return true成功 false失败
     */
    public boolean hmset(String key, Map<String,Object> map, long time, TimeUnit unit){
        try {
            redisTemplate.opsForHash().putAll(key, map);
            expire(key, time,unit);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 向一张hash表中放入数据,如果不存在将创建
     * @param k 键
     * @param item 项
     * @param value 值
     * @param time 时间
     * @param unit 时间单位
     * @return true 成功 false失败
     */
    public boolean hset(String k, String item, Object value, long time, TimeUnit unit) {
        try {
            redisTemplate.opsForHash().put(k, item, value);
            expire(k,time, unit);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * HashGet
     * @param key 键 不能为null
     * @param item 项 不能为null
     * @return 值
     */
    public Object hget(String key,String item){
        try {
            return redisTemplate.opsForHash().get(key, item);
        } catch (RedisException e) {
            e.printStackTrace();
            return null;
        }

    }

    /**
     * 获取hashKey对应的所有键值
     * @param key 键
     * @return 对应的多个键值
     */
    public Map<Object,Object> hmget(String key){
        try {
            return redisTemplate.opsForHash().entries(key);
        } catch (RedisException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 删除hash表中的值
     * @param key 键 不能为null
     * @param item 项 可以使多个 不能为null
     */
    public void hdel(String key, Object... item){
        try {
            redisTemplate.opsForHash().delete(key, item);
        } catch (RedisException e) {
            e.printStackTrace();
        }
    }

    /**
     * 判断hash表中是否有该项的值
     * @param key 键 不能为null
     * @param item 项 不能为null
     * @return true 存在 false不存在
     */
    public boolean hHasKey(String key, String item){
        try {
            return redisTemplate.opsForHash().hasKey(key, item);
        } catch (RedisException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 根据key获取Set中的所有值
     * @param key 键
     * @return
     */
    public Set<Object> sGet(String key){
        try {
            return redisTemplate.opsForSet().members(key);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 根据value从一个set中查询,是否存在
     * @param key 键
     * @param value 值
     * @return true 存在 false不存在
     */
    public boolean sHasKey(String key, Object value){
        try {
            return redisTemplate.opsForSet().isMember(key, value);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 将数据放入set缓存
     * @param key 键
     * @param values 值 可以是多个
     * @return 成功个数
     */
    public long sSet(String key, long time, TimeUnit unit, Object...values) {
        try {
            long count = redisTemplate.opsForSet().add(key, values);
            expire(key, time, unit);
            return count;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
    /**
     * 获取set缓存的长度
     * @param key 键
     * @return
     */
    public long sGetSetSize(String key){
        try {
            return redisTemplate.opsForSet().size(key);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 移除值为value的
     * @param key 键
     * @param values 值 可以是多个
     * @return 移除的个数
     */
    public long setRemove(String key, Object ...values) {
        try {
            Long count = redisTemplate.opsForSet().remove(key, values);
            return count;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 获取list缓存的内容
     * @param key 键
     * @param start 开始
     * @param end 结束  0 到 -1代表所有值
     * @return
     */
    public List<Object> lGet(String key, long start, long end){
        try {
            return redisTemplate.opsForList().range(key, start, end);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取list缓存的所有内容
     * @param key
     * @return
     */
    public List<Object> lGetAll(String key){
        try {
            return lGet(key,0,-1);
        } catch (RedisException e) {
            e.printStackTrace();
            return null;
        }

    }

    /**
     * 获取list缓存的长度
     * @param key 键
     * @return
     */
    public long lGetListSize(String key){
        try {
            return redisTemplate.opsForList().size(key);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 通过索引 获取list中的值
     * @param key 键
     * @param index 索引  index>=0时， 0 表头，1 第二个元素，依次类推；index<0时，-1，表尾，-2倒数第二个元素，依次类推
     * @return
     */
    public Object lGetIndex(String key, long index){
        try {
            return redisTemplate.opsForList().index(key, index);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 将list放入缓存
     * @param key 键
     * @param value 值
     * @param time 时间(秒)
     * @return
     */
    public boolean lSet(String key, Object value, long time, TimeUnit unit) {
        try {
            redisTemplate.opsForList().rightPush(key, value);
            expire(key, time, unit);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }







}
