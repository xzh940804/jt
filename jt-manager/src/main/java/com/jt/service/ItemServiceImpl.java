package com.jt.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jt.mapper.ItemDescMapper;
import com.jt.pojo.Item;
import com.jt.pojo.ItemDesc;
import com.jt.vo.EasyUITable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jt.mapper.ItemMapper;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Service
public class ItemServiceImpl implements ItemService {
	
	@Autowired
	private ItemMapper itemMapper;
	@Autowired
	private ItemDescMapper itemDescMapper;

	/**
	 * select * from tb_item limit 起始位置:每页条数
	 * 第一页: 每页20条
	 *  select * from tb_item limit 0,20;  0-19
	 * 第二页:
	 * 	select * from tb_item limit 20,20; 20-39
	 * 第三页:
	 * 	select * from tb_item limit 40,20;
	 * 第N页:
	 * 	select * from tb_item limit (page-1)rows,rows;
	 */
	@Override
	public EasyUITable findItemByPage(int page, int rows) {
		/**
		 * int total = itemMapper.selectCount(null);	//查询记录总数
		 int start = (page-1) * rows;
		 List<Item> itemList =
		 itemMapper.findItemByPage(start,rows);
		 **/
		System.out.println(page+rows);
	     IPage<Item> ipage =new Page<>(page,rows);
	     QueryWrapper<Item> queryWrapper=new QueryWrapper<>();
	     queryWrapper.orderByDesc("updated");
	     IPage<Item> itemIPage=itemMapper.selectPage(ipage,queryWrapper);
	     int total=(int)itemIPage.getTotal();
	     List<Item> itemList=itemIPage.getRecords();

		return new EasyUITable(total, itemList);

//		IPage<Item> iPage = new Page<>(page, rows);
//		QueryWrapper<Item> queryWrapper = new QueryWrapper<>();
//		queryWrapper.orderByDesc("updated");
//		IPage<Item> itemPage = itemMapper.selectPage(iPage, queryWrapper);
//		int total = (int) itemPage.getTotal();
//		List<Item> itemList = itemPage.getRecords();


	}
@Transactional
	@Override
	public void save(Item item, ItemDesc itemDesc) {
		item.setStatus(1)
		.setCreated(new Date())
		.setUpdated(item.getCreated());
		//当数据入库之后  会自动回显主键
		itemMapper.insert(item);

		itemDesc.setItemId(item.getId())
				.setCreated(item.getCreated())
				.setUpdated(item.getCreated());
		itemDescMapper.insert(itemDesc);
	}

	@Override
	public ItemDesc findItemDescById(Long itemId) {
		itemDescMapper.selectById(itemId);
		return itemDescMapper.selectById(itemId);
	}

	@Override
	public void updateItem(Item item, ItemDesc itemDesc) {
		item.setUpdated(new Date());
		itemMapper.updateById(item);

		itemDesc.setItemId(item.getId())
				.setUpdated(item.getUpdated());
		itemDescMapper.updateById(itemDesc);

	}

	@Override
	public void deleteItem(Long[] ids) {
		List<Long> itemList= Arrays.asList(ids);
		itemMapper.deleteBatchIds(itemList);
		itemDescMapper.deleteBatchIds(itemList);

	}


	public void updateStatus(Long[] ids, int status) {
		Item item=new Item();
		item.setStatus(status).setUpdated(new Date());
		UpdateWrapper<Item> updateWrapper=new UpdateWrapper<Item>();
		List<Long>  idList=Arrays.asList(ids);
		updateWrapper.in("id",idList);
		itemMapper.update(item,updateWrapper);

	}

	@Override
	public Item findItemById(Long itemId) {
		return itemMapper.selectById(itemId);
	}


}
