
package com.aol.advertising.dealdiscovery.forecast.application;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.env.Environment;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Created by mcordones13 on 6/20/16.
 */
@Configuration
@PropertySource(value = "classpath:${env}/application.properties", ignoreResourceNotFound = true)
@ComponentScan("com.aol.advertising.dealdiscovery.forecast")
@MapperScan("com.aol.advertising.dealdiscovery.forecast.dao")
@EnableTransactionManagement
public class AppConfig {

    /** */
    public static final String PROFILE_PROD = "prod";
    /** */
    public static final String PROFILE_DEV = "dev";

    @Autowired
    Environment env;

    @Bean
    public Config config(){
        return new Config(env);
    }

    /**
     *
     * @return
     */
    @Bean
    public DriverManagerDataSource aceDataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("oracle.jdbc.driver.OracleDriver");
        dataSource.setUrl(env.getProperty("ace.db.jdbc.url"));
        dataSource.setUsername(env.getProperty("ace.db.username"));
        dataSource.setPassword(env.getProperty("ace.db.password"));
        return dataSource;
    }

    /**
     *
     * @return
     * @throws Exception
     */
    @Bean
    public SqlSessionFactory sqlSessionFactory() throws Exception {
        SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        sessionFactory.setDataSource(aceDataSource());
        return sessionFactory.getObject();
    }

    /**
     *
     * @return
     */
    @Bean
    public DataSourceTransactionManager transactionManager() {
        return new DataSourceTransactionManager(aceDataSource());
    }
}
