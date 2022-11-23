package com.example.filemanager.config;

import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 拦截器 验证是否登录的拦截
 */
public class LoginHandlerInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {

//        Object loginUser = request.getSession().getAttribute("loginUser");
//        if (loginUser == null) {
//            System.out.println("警告：进入到了拦截器的拦截---->");
//            request.setAttribute("msg", "登录失效，请重新登录！");
//            try {
//                //这里是检查到没有登录，给你转到登录页面
//                request.getRequestDispatcher("/login").forward(request, response);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            return false;
//        } else {
//            return true;
//        }
        return true;


    }
}
