package com.fw.controller;

import com.fw.service.ADRegCancelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@Controller
@RequestMapping(value = "mnms")
public class AdRegCancelController {
    @Autowired
    ADRegCancelService adRegCancelService;

    @RequestMapping(value = "getAdRegCancelByMonth.do")
    @ResponseBody
    Map<String, Object> getMenuList() throws Exception {

        Map<String, Object> result_map = adRegCancelService.getAdRegCancelByMonth();

        return result_map;
    }

}
