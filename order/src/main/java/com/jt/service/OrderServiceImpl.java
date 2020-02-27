package com.jt.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jt.mapper.OrderItemMapper;
import com.jt.mapper.OrderMapper;
import com.jt.mapper.OrderShippingMapping;
import com.jt.pojo.Order;
import com.jt.pojo.OrderItem;
import com.jt.pojo.OrderShipping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class OrderServiceImpl implements DubboOrderService {
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private OrderShippingMapping orderShippingMapping;
    @Autowired
    private OrderItemMapper orderItemMapper;

    @Transactional
    @Override
    public String saveOrder(Order order) {
        String orderId=""+order.getUserId()+System.currentTimeMillis();
        Date date=new Date();
        order.setOrderId(orderId).setStatus(1).setCreated(date).setUpdated(date);
        orderMapper.insert(order);
        System.out.println("訂單入庫成功");


        OrderShipping shipping=order.getOrderShipping();
        shipping.setOrderId(orderId).setCreated(date).setUpdated(date);
        orderShippingMapping.insert(shipping);
        System.out.println("訂單物流入庫成功！！！");
        //订单商品入库
        List<OrderItem> orderItems = order.getOrderItems();
        for (OrderItem orderItem : orderItems) {
            orderItem.setOrderId(orderId)
                    .setCreated(date)
                    .setUpdated(date);
            orderItemMapper.insert(orderItem);
        }
        System.out.println("订单入库全部成功!!!!!");
        return orderId;

    }

    @Override
    public Order findOrderById(String id) {
         Order order=orderMapper.selectById(id);
         OrderShipping orderShipping=orderShippingMapping.selectById(id);
        QueryWrapper<OrderItem>     queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("order_id",id);
        List<OrderItem> items=orderItemMapper.selectList(queryWrapper);
        order.setOrderItems(items).setOrderShipping(orderShipping);

        return order;
    }
}
