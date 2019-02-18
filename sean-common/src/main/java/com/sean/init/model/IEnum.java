package com.sean.init.model;

/**
 * 实现描述：枚举类接口
 * m uds-common
 * @author : sean.cai
 * @version : 1.0.0
 * @since : 04/05/2018 11:59 AM
 */
public interface IEnum<T> {
    public T getValue();
    public String getDesc();
}