package com.fw.controller;


import com.fw.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;
import com.fw.common.util.StringUtil;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

@RestController
@RequestMapping(value ="mnms")
public class UserController {
    @Autowired
    private UserService userService;

    @RequestMapping(value = "login.do")
    @ResponseBody
    public Map<String, Object> login(@RequestBody Map<String, Object> map, HttpServletResponse response)
            throws Exception {

        Map<String, Object> result_map = new LinkedHashMap<String, Object>();

        try {
            result_map = userService.login(map);
        } catch (Exception e) {
            String msg = StringUtil.simplifyErrMsg(e.getLocalizedMessage());
            result_map.put("RSLT_CD", "999");
            result_map.put("RSLT_MSG", msg);
            result_map.put("status", "-1");
            result_map.put("payload", "");
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
        return result_map;

    }


    @GetMapping("/testLogin")
    @ResponseBody
    public Map<String, Object> testLogin(@RequestBody Map<String, Object> map) {
        Map<String, Object> users = new HashMap<>();
        try {
            users = userService.testLogin(map);
            return users;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return users;
    }
}
