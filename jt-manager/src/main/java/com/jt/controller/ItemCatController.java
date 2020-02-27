package com.jt.controller;

import com.jt.pojo.Item;
import com.jt.pojo.ItemCat;
import com.jt.service.ItemCatService;
import com.jt.vo.EasyUITree;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/item/cat")
public class ItemCatController {
    @Autowired
    private ItemCatService itemCatService;
    ///item/cat/queryItemName
    @RequestMapping("/queryItemName")
    @ResponseBody
    public String findItemCatByName(long itemCatId){
      //  System.out.println(itemCatService.findItemCatByName(itemCatId).getName());
       return itemCatService.findItemCatByName(itemCatId).getName();

    }
    @RequestMapping("/list")
    @ResponseBody
    public List<EasyUITree> findItemCatByParentId(@RequestParam(defaultValue="0",name="id") Long parentId){
        return itemCatService.findItemCatByParentId(parentId);
        //return itemCatService.findItemCatByCache(parentId);
    }
}
