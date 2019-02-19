package com.sean.init.lock;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * ZooKeeper分布式锁。
 */
public class ZkDistributedLock implements Lock {
    private static final Logger logger = LoggerFactory.getLogger(ZkDistributedLock.class);

    private final ThreadLocal<Boolean> locked = new ThreadLocal<>();

    // znode路径前缀
    private static final String KEY_PREFIX = "/lock/";

    // 锁的znode节点名
    private String key;

    // zk客户端
    private CuratorFramework client;

    // 分布式锁操作对象
    private InterProcessMutex mutex;

    // 线程等待锁的最长时间（单位：毫秒），等待超过该时间后，线程'获取锁失败
    private long maxWaitingMS;

    @Override
    synchronized public boolean lock() throws InterruptedException {
        //case 1: 没有连接，获取锁失败
        if (!client.getZookeeperClient().isConnected()) {
            logger.error("ZooKeeper is not connected while acquiring lock.");
            locked.set(false);
            return false;
        }

        try {
            //case2: 尝试获取锁
            if (mutex.acquire(maxWaitingMS, TimeUnit.MILLISECONDS)) {
                logger.info("Obtain lock for key : {}.", key);
                locked.set(true);
                return true;
            }
        } catch (Exception e) {
            logger.error("Failed to obtain lock, key : {}. error : {}", key, e.toString());
        }

        locked.set(false);
        return false;
    }

    @Override
    public void unlock() {
        if (!client.getZookeeperClient().isConnected()) {
            logger.error("ZooKeeper is not connected while releasing lock.");
            return;
        }

        if (locked.get() != null && locked.get()) {
            try {
                mutex.release();
                logger.info("Release lock for key : {}.", key);
            } catch (Exception e) {
                logger.error("Failed to release lock, key : {}. error : {}", key, e.toString());
            }
            locked.set(false);
        }
    }
}


