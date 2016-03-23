package com.bxtpw.study.controller;

import com.bxtpw.study.context.ConfigContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

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
public class ConfigController {

    @RequestMapping("show")
    public String show(ModelMap model) {
        return "show";
    }

    @RequestMapping("update/{key}/{value}")
    @ResponseBody
    public boolean update(@PathVariable String key, @PathVariable String value) {
        try {
            ConfigContext.update(key, value);
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}
