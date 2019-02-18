package com.sean.init.mapper;

import com.sean.init.model.Lock;
import java.util.List;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * @author : sean.cai
 * @version : 1.0.0
 * @since : 2018/9/18 4:06 PM
 */
@Mapper
public interface MyLockMapper {

    @Select("SELECT * FROM `lock`")
    List<Lock> getLocks();

    @Insert("INSERT INTO lock (lock_key, desc, update_time) VALUES (#{lockKey}, #{desc}, #{updateTime})")
    public int acquire(Lock lock);
}
