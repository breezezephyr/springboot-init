package com.sean.init.lock;

import com.zaxxer.hikari.HikariDataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import javax.activation.DataSource;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author : sean.cai
 * @version : 1.0.0
 * @since : 2018/9/19 4:55 PM
 */
@Service
public class MysqlDistLock implements Lock{

    @Autowired
    private HikariDataSource dataSource;

    @Autowired
    private SqlSessionFactory sqlSessionFactory;

    /**
     * 超时获取锁
     * @param lockID
     * @param timeOuts
     * @return
     * @throws InterruptedException
     */
    public boolean acquireByUpdate(String lockID, long timeOuts) throws InterruptedException, SQLException {
        try {
            Connection connection = dataSource.getConnection();
            SqlSession sqlSession = sqlSessionFactory.openSession();
            Connection connection1 = sqlSession.getConnection();
            connection.setAutoCommit(false);
            String sql = "SELECT lock_key from lock where lock_key = ? for UPDATE ";
            long futureTime = System.currentTimeMillis() + timeOuts;
            long remain = timeOuts;
            long timerange = 500;
            while (true) {
                CountDownLatch latch = new CountDownLatch(1);
                try {
                    PreparedStatement statement = connection.prepareStatement(sql);
                    statement.setString(1, lockID);
                    boolean ifsucess = statement.execute();//如果成功，那么就是获取到了锁
                    if (ifsucess) {
                        return true;
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                latch.await(timerange, TimeUnit.MILLISECONDS);
                remain = futureTime - System.currentTimeMillis();
                if (remain <= 0) {
                    break;
                }
                if (remain < timerange) {
                    timerange = remain;
                }
                continue;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override public boolean lock() throws InterruptedException {
        return false;
    }

    @Override public void unlock() {

    }
}
