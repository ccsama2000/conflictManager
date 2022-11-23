package com.example.filemanager.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 页面跳转控制
 */
@Controller
public class PageController {

    @GetMapping({"/", "/index"})
    public String index() {
        return "views/index";
    }

    @GetMapping({"/login"})
    public String login() {
        return "views/pages/login";
    }




}
