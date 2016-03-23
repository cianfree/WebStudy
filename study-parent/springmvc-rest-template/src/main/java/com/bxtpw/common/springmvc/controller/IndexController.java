package com.bxtpw.common.springmvc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author 夏集球
 * @time 2015年6月17日 上午8:22:42
 * @version 0.1
 * @since 0.1
 */
@Controller
@RequestMapping("index")
public class IndexController {

    @RequestMapping(value = { "", "index" })
    public String index() {
        return "index";
    }
}
