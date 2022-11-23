package com.example.filemanager.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.util.HashMap;


@Configuration
public class DruidConfig {

    @ConfigurationProperties(prefix = "spring.datasource")
    @Bean
    public DataSource druidDataSource() {
        return new DruidDataSource();
    }

    /**
     * 后台监控
     */


    @Bean
    public ServletRegistrationBean statViewServlet() {
        //设置访问哪个页面可以进入监控页面（/druid/*）--------http://localhost:9090/druid
        ServletRegistrationBean<StatViewServlet> bean = new ServletRegistrationBean<>(new StatViewServlet(), "/druid/*");

        //后台需要有人登录，账号密码配置
        HashMap<String, String> initParameters = new HashMap<>();
        initParameters.put("loginUsername", "admin");//登录key loginUsername,loginPassword 是固定的
        initParameters.put("loginPassword", "123456");

        //允许谁可以访问
        initParameters.put("allow", "");

        //禁止谁能访问
        //initParameters.put("","");

        //设置初始化参数
        bean.setInitParameters(initParameters);
        return bean;
    }
}

