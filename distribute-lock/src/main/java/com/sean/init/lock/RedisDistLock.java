package com.sean.init.lock;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.testng.util.Strings;

import java.util.concurrent.TimeUnit;

/**
 * @author : sean.cai
 * @version : 1.0.0
 * @since : 2018/9/19 4:55 PM
 */
@Service
public class RedisDistLock implements Lock {

    private static final int DEFAULT_ACQUIRE_RESOLUTION_MILLIS = 100; //默认获取锁的时间

    private String key;
    private StringRedisTemplate redisTemplate;
    private long lockExpiredTimesMS = 600000;// 锁超时时间
    private long lockWaitingTimeMS =100000; //获取锁等待时间

    private final ThreadLocal<Boolean> locked = new ThreadLocal<>();

    public RedisDistLock(String key, long lockExpiredTimesMS, long lockWaitingTimeMS) {
        this.key = key;
        this.redisTemplate = redisTemplate;
        this.lockExpiredTimesMS = lockExpiredTimesMS;
        this.lockWaitingTimeMS = lockWaitingTimeMS;
    }

    @Override
    public boolean lock() throws InterruptedException {
        locked.set(false);
        long timeout = lockExpiredTimesMS;
        //loop
        while (timeout > 0) {
            final String redisValue = String.valueOf(System.currentTimeMillis() + this.lockExpiredTimesMS + 1);
            boolean lockFlag = redisTemplate.opsForValue().setIfAbsent(key, redisValue);
            //case1: 获取锁成功
            if (lockFlag) {
                locked.set(true);
                return true;
            }
            final String storedValue = redisTemplate.opsForValue().get(key);
            //case2: 获取redis value, 但是已被其他线程释放导致 value为空
            if (storedValue == null) {
                //立即重新获取锁
                continue;
            }
            //case3: redis中的锁过期了
            long existedLockExpireTime = Long.parseLong(storedValue);
            if (existedLockExpireTime < System.currentTimeMillis()) {
                String retValue = redisTemplate.opsForValue().getAndSet(key, redisValue);
                if (Strings.isNotNullAndNotEmpty(retValue) && retValue.equalsIgnoreCase(redisValue)) {
                    locked.set(true);
                    return true;
                }
            }
            timeout -= DEFAULT_ACQUIRE_RESOLUTION_MILLIS;
            // 延迟100毫秒, 这里使用随机时间可能会好一点,可以防止饥饿进程的出现
            TimeUnit.MILLISECONDS.sleep(DEFAULT_ACQUIRE_RESOLUTION_MILLIS);
        }
        return false;
    }

    @Override public void unlock() {
        if(locked.get()){
            redisTemplate.delete(key);
            locked.set(false);
        }
    }
}
