package com.fw.service;

import com.fw.common.common.AbstractDAO;
import com.fw.model.ADRegCancelDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ADRegCancelServiceImpl implements ADRegCancelService{

    @Autowired
    ADRegCancelDao adRegCancelDao;

    @Override
    public Map<String, Object> getAdRegCancelByMonth() throws Exception {
        Map<String, Object> result_map = new HashMap<String, Object>();
        Map<String, Object> queryDate = new HashMap<String, Object>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        LocalDate localDate = LocalDate.now();
        String date_prvs_month = localDate.minusMonths(1).format(formatter).toString();
        String date_prvs_week  = localDate.minusWeeks(1).format(formatter).toString();
        String date_prvs_day   = localDate.minusDays(1).format(formatter).toString();
        String to_day          = localDate.format(formatter);
        queryDate.put("date_prvs_month" ,date_prvs_month);
        queryDate.put("date_prvs_week" ,date_prvs_week);
        queryDate.put("date_prvs_day" ,date_prvs_day);
        queryDate.put("to_day" ,to_day);
        List<Map<String, Object>> data = adRegCancelDao.selectList("AdSts.getAdRegCanByMonth", queryDate);
//        for (Map<String, Object> map : data) {
//            result_map.put("date",map.get("gijun_il").toString());
//            result_map.put("req_gb",map.get("req_gb").toString());
//            result_map.put("amt",map.get("amt").toString());
//        }
        result_map.put("status", "ok");
        result_map.put("payload", data);


        return result_map;
    }
}
