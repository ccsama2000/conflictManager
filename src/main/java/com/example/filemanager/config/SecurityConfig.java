package com.example.filemanager.config;


import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {


    @Override
    protected void configure(HttpSecurity http) throws Exception {

        //首页所有人可以访问，功能页只有对应权限的人才可以访问
        http.authorizeRequests()
                .antMatchers("/").permitAll();


        /**

         //没有特权访问时，就会开启登录页面
         http.formLogin().successForwardUrl("/")//重定义登录成功后的页面
         .loginPage("/toLogin");//定制登录页
         *
         */

        //防止网站攻击
        http.csrf().disable();//关闭csrf功能，登录失败肯定存在的原因

        //开启记住我功能
        http.rememberMe()
                .rememberMeParameter("rememberMe");//自定义页面的记住我
        //开启注销功能
        http.logout().logoutSuccessUrl("/");

        //iframe可以访问页面
        http.headers().frameOptions().sameOrigin();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication().passwordEncoder(new BCryptPasswordEncoder())
                .withUser("dzy").password(new BCryptPasswordEncoder().encode("123456")).roles("vip2", "vip3")
                .and()
                .withUser("root").password(new BCryptPasswordEncoder().encode("123456")).roles("vip1", "vip2", "vip3")
                .and()
                .withUser("guest").password(new BCryptPasswordEncoder().encode("123456")).roles("vip1");

    }
}
