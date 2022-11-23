package com.example.filemanager.controller;


import com.example.filemanager.pojo.User;
import com.example.filemanager.utils.LayuiUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;

import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Controller
public class UserController {

    //注入业务层，controller调用业务层

    /**
     * 用户注册
     *
     * @return
     * @throws Exception
     */
    @RequestMapping("/userRegister")
    public String register(User user, Model model) throws Exception{

        if (user.getEmail().equals("") || user.getPassword().equals("") || user.getUserName().equals("")) {
            model.addAttribute("msg", "请先填写必须信息才可注册！");
            return "views/pages/login";
        }else{
            model.addAttribute("msg", "恭喜登录成功，进入主页");
            return "views/index";
        }



    }

    /**
     * 用户登录
     *
     * @param user
     * @param model
     * @param session
     * @param modelMap
     * @return
     * @throws Exception
     */
    @RequestMapping("/userToLogin")
    public String toLogin(User user,
                          Model model,
                          HttpSession session,
                          RedirectAttributesModelMap modelMap) throws Exception {

        //对比Model 与 RedirectAttributesModelMap ，后者重定向后可以带参数！
        if (user.getEmail() == null || user.getPassword() == null || "".equals(user.getEmail()) || "".equals(user.getPassword())) {
            model.addAttribute("msg", "请先输入账号密码才可登录！");
            return "views/pages/login";
        }else{
            return "views/index";
        }

    }

    /**
     * 退出登录
     */
    @RequestMapping("/userLogout")
    public String logout(HttpSession session, RedirectAttributesModelMap modelMap) {
        session.invalidate();
        modelMap.addFlashAttribute("msg", "退出成功，可重新登录！");
        return "redirect:/login";//redirect重定向干净利落，不能用model参数
    }


    /**
     * 获取身份
     * @param session
     * @return
     */
    @ResponseBody
    @RequestMapping("/getRole")
    public Map<String, Object> getRole(HttpSession session) {

        int userRole = (int) session.getAttribute("loginRole");
        String userName = (String) session.getAttribute("loginName");
        Map<String, Object> map=new HashMap();
        map.put("userRole",userRole);
        map.put("userName",userName);

        return LayuiUtil.setResultMap(0, "请求成功", map, 1);


    }


}
