package com.sean.init.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.util.Properties;
import javax.sql.DataSource;
import org.apache.ibatis.session.AutoMappingBehavior;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

/**
 * @author : breezezephyr
 * @version : 1.0.0
 * @since : 2018/9/18 3:35 PM
 */
@Configuration
public class MysqlConfig {

    @Value("${mysql.db.init.url}")
    private String dbUrl;

    @Value("${mysql.db.init.username}")
    private String dbUsername;

    @Value("${mysql.db.init.password}")
    private String dbPassword;

    @Value("${mysql.db.init.readonly:false}")
    private boolean dbReadOnly;

    // 连接数据库的超时时间，单位毫秒
    @Value("${mysql.db.init.connection-timeout:30000}")
    private long dbConnectionTimeout;

    // 最大空闲时间，非核心线程的空闲时间如果超过此阈值，则被线程池销毁掉, 单位毫秒
    @Value("${mysql.db.init.idle-timeout:60000}")
    private long dbIdleTimeout;

    // 最大生存时间，核心线程如果存活的时间超过此阈值，会被销毁, 单位毫秒
    @Value("${mysql.db.init.max-lifetime:1800000}")
    private long dbMaxLifetime;

    // 最大线程池容量
    @Value("${mysql.db.init.maximum-pool-size:100}")
    private int dbMaximumPoolSize;

    @Value("${mysql.db.init.drive:com.mysql.jdbc.Driver}")
    private String dbDrive;

    public String getDbUrl() {
        return dbUrl;
    }

    public void setDbUrl(String dbUrl) {
        this.dbUrl = dbUrl;
    }

    public String getDbUsername() {
        return dbUsername;
    }

    public void setDbUsername(String dbUsername) {
        this.dbUsername = dbUsername;
    }

    public String getDbPassword() {
        return dbPassword;
    }

    public void setDbPassword(String dbPassword) {
        this.dbPassword = dbPassword;
    }

    public boolean isDbReadOnly() {
        return dbReadOnly;
    }

    public void setDbReadOnly(boolean dbReadOnly) {
        this.dbReadOnly = dbReadOnly;
    }

    public long getDbConnectionTimeout() {
        return dbConnectionTimeout;
    }

    public void setDbConnectionTimeout(long dbConnectionTimeout) {
        this.dbConnectionTimeout = dbConnectionTimeout;
    }

    public long getDbIdleTimeout() {
        return dbIdleTimeout;
    }

    public void setDbIdleTimeout(long dbIdleTimeout) {
        this.dbIdleTimeout = dbIdleTimeout;
    }

    public long getDbMaxLifetime() {
        return dbMaxLifetime;
    }

    public void setDbMaxLifetime(long dbMaxLifetime) {
        this.dbMaxLifetime = dbMaxLifetime;
    }

    public int getDbMaximumPoolSize() {
        return dbMaximumPoolSize;
    }

    public void setDbMaximumPoolSize(int dbMaximumPoolSize) {
        this.dbMaximumPoolSize = dbMaximumPoolSize;
    }

    public String getDbDrive() {
        return dbDrive;
    }

    public void setDbDrive(String dbDrive) {
        this.dbDrive = dbDrive;
    }

    @Bean(name = "dataSource")
    public DataSource dataSource() {
        return new HikariDataSource(
                createHikariConfig(
                        dbUrl,
                        dbUsername,
                        dbPassword,
                        dbReadOnly,
                        dbConnectionTimeout,
                        dbIdleTimeout,
                        dbMaxLifetime,
                        dbMaximumPoolSize,
                        dbDrive
                )
        );
    }

    private HikariConfig createHikariConfig(String url, String username, String password, boolean readonly, long connectionTimeout, long idleTimeout,
            long maxLifeTime, int maxPoolSize, String driver) {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(url);
        config.setUsername(username);
        config.setPassword(password);
        config.setReadOnly(readonly);
        config.setConnectionTimeout(connectionTimeout);
        config.setIdleTimeout(idleTimeout);
        config.setMaxLifetime(maxLifeTime);
        config.setMaximumPoolSize(maxPoolSize);
        config.setDriverClassName(driver);
        return config;
    }


    @Bean
    public PlatformTransactionManager transactionManager() {
        DataSourceTransactionManager transactionManager = new DataSourceTransactionManager();
        transactionManager.setDataSource(dataSource());
        return transactionManager;
    }

    @Primary
    @Bean(name = "sqlSessionFactory")
    public SqlSessionFactory sqlSessionFactoryBean() throws Exception {
        org.apache.ibatis.session.Configuration configuration = new org.apache.ibatis.session.Configuration();
        // 全局映射器启用缓存
        configuration.setCacheEnabled(true);
        // 查询时，关闭关联对象即时加载以提高性能
        configuration.setLazyLoadingEnabled(true);
        // 设置关联对象加载的形态，此处为按需加载字段(加载字段由SQL指 定)，不会加载关联表的所有字段，以提高性能
        configuration.setAggressiveLazyLoading(false);
        // 对于未知的SQL查询，允许返回不同的结果集以达到通用的效果
        configuration.setMultipleResultSetsEnabled(true);
        // 允许使用列标签代替列名
        configuration.setUseColumnLabel(true);
        // 给予被嵌套的resultMap以字段-属性的映射支持
        configuration.setAutoMappingBehavior(AutoMappingBehavior.FULL);
        // 对于批量更新操作缓存SQL以提高性能
        configuration.setDefaultExecutorType(ExecutorType.SIMPLE);
        // 数据库超过25000秒仍未响应则超时
        configuration.setDefaultStatementTimeout(25000);
        configuration.setMapUnderscoreToCamelCase(true);
        // 允许使用自定义的主键值(比如由程序生成的UUID 32位编码作为键值)，数据表的PK生成策略将被覆盖
        //configuration.setUseGeneratedKeys(true);

        Properties properties = new Properties();
        properties.setProperty("dialect", "mysql");

        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();

        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource());
        sqlSessionFactoryBean.setConfiguration(configuration);
        sqlSessionFactoryBean.setConfigurationProperties(properties);
        sqlSessionFactoryBean.setMapperLocations(resolver.getResources("classpath*:mapper/*.xml"));
        return sqlSessionFactoryBean.getObject();
    }

}
