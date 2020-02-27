package com.jt.quartz;


import java.util.Calendar;
import java.util.Date;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.jt.mapper.OrderMapper;
import com.jt.pojo.Order;

//准备订单定时任务
@Component
public class OrderQuartz extends QuartzJobBean{

	@Autowired
	private OrderMapper orderMapper;

	/**
	 * 如果任务执行,则调用该方法
	 * 将超时订单 修改状态status 由未付款改为交易关闭,规定30分钟超时
	   Sql:
	   	update tb_order set status=6,updated=#{date}
	   	where status=1 and (created < now - 30分钟)
	   						gt >大于  <lt  =eq
	   						ge >=    <=le 
	 */
	@Override
	@Transactional
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		//java 时间api 格林威治时间  获取当前时间
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.MINUTE, -30);
		Date timeOut = calendar.getTime();
		
		/**
		 * entity: 要修改的数据   挑选其中不为null的元素当set条件
		 * updateWrapper: 条件构造器
		 */
		Order orderTemp = new Order();
		orderTemp.setStatus(6)
				 .setUpdated(new Date());
		UpdateWrapper<Order> updateWrapper = 
				new UpdateWrapper();
		updateWrapper.eq("status", 1)
					 .lt("created", timeOut);
		orderMapper.update(orderTemp, updateWrapper);
		System.out.println("定时任务完成!!!!!!");
	}
}
