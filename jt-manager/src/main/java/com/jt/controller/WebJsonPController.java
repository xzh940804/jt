package com.jt.controller;

import com.fasterxml.jackson.databind.util.JSONPObject;
import com.jt.pojo.ItemDesc;
import com.jt.utils.ObjectMapperUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WebJsonPController {
//    @RequestMapping("/web/testJSON")
//    public String jsonp(String callback){
//        ItemDesc itemDesc=new ItemDesc();
//       itemDesc.setItemId(100L).setItemDesc("jsonp调用成功");
//       String json=ObjectMapperUtils.toJson(itemDesc);
//       return callback+"("+json+")";
//    }
    @RequestMapping("/web/testJSONP")
    public JSONPObject jsonp(String callback){
        ItemDesc itemDesc=new ItemDesc();
        itemDesc.setItemId(100L).setItemDesc("jsonp调用成功");

        return new JSONPObject(callback,itemDesc);
    }
}
