package com.bxtpw.study.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <pre>
 *
 * </pre>
 *
 * @author 夏集球
 * @version 0.1
 * @time 2015/12/29 21:31
 * @since 0.1
 */
@Controller
public class HelloController {

    @RequestMapping("sayHello")
    public String sayHello(String name, ModelMap model, HttpServletRequest request, HttpServletResponse response) {
        if (name.length() < 1) {
            throw new NumberFormatException();
        }
        model.put("name", name);
        return "hello";
    }
}
