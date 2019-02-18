package com.caixp.controller;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;


/**
 * 测试基础类
 * @author sean.cai
 */
@Sql(scripts = "classpath:embedded/test.sql")
@TestPropertySource("/application.properties")
public class BaseControllerTest {
    private static final Logger logger = LoggerFactory.getLogger(BaseControllerTest.class);

    /**
     * 数据准备
     */
    @Before
    public void prepare() {
        logger.info("Before run test Case, init data!");
    }

    @Test
    public void test() throws Exception {
    }
}