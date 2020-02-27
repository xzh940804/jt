package com.jt.controller;

import com.jt.pojo.Item;
import com.jt.pojo.ItemDesc;
import com.jt.vo.EasyUITable;
import com.jt.vo.SysResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jt.service.ItemService;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/item")
public class ItemController {
	//http://localhost:8091/item/query?page=1&rows=20
	@Autowired
	private ItemService itemService;

	@RequestMapping("/query")
	@ResponseBody
	public EasyUITable findItemByPage(int page,int rows){

		return itemService.findItemByPage(page,rows);
	}
	@RequestMapping("/save")
	@ResponseBody
	public SysResult saveItem(Item item, ItemDesc itemDesc){
		try{
			itemService.save(item,itemDesc);
			return SysResult.success();
		}catch (Exception e){
			e.printStackTrace();
			return SysResult.fail();
		}

	}
	//http://localhost:8091/item/query/item/desc/1474391
	@RequestMapping("/query/item/desc/{itemId}")
	@ResponseBody
	public SysResult findItemDescById(@PathVariable Long itemId){
		ItemDesc itemDesc=itemService.findItemDescById(itemId);
		return SysResult.success(itemDesc);
	}
	@RequestMapping("/update")
	@ResponseBody
	public SysResult updateItem(Item item,ItemDesc itemDesc){
		itemService.updateItem(item,itemDesc);
		return SysResult.success();
	}
	@RequestMapping("/delete")
	@ResponseBody
	public SysResult deleteItem( Long[] ids){
	itemService.deleteItem(ids);
		return SysResult.success();
	}
	@RequestMapping("/instock")
	@ResponseBody
	public SysResult instocksItems(Long[] ids){
		int status=2;
		itemService.updateStatus(ids,status);
		return SysResult.success();
	}
	@RequestMapping("/reshelf")
	@ResponseBody
	public SysResult reshelfsItems(Long[] ids){
		int status=1;
		itemService.updateStatus(ids,status);
		return SysResult.success();
	}
}
