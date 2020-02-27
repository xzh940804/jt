package com.jt.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.jt.mapper.CartMapper;
import com.jt.pojo.Cart;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;

@Service
public class CartServiceImpl implements  DubboCartService{
    @Autowired
    private CartMapper cartMapper;
    @Override
    public List<Cart> findCartListByUserId(Long userId) {
        QueryWrapper<Cart> queryWrapper=new QueryWrapper<Cart>();
        queryWrapper.eq("user_id",userId);

        return cartMapper.selectList(queryWrapper);
    }

    @Override
    public void updateCartNum(Cart cart) {
        Cart cartTemp  =new Cart();
        cartTemp.setNum(cart.getNum()).setUpdated(new Date());
        UpdateWrapper<Cart> updateWrapper=new UpdateWrapper<>();
        updateWrapper.eq("user_id",
                cart.getUserId()).eq("item_id",
                cart.getItemId());
        cartMapper.update(cartTemp,updateWrapper);
    }
    //存在更新 不存在添加
    @Override
    public void saveCart(Cart cart) {
        QueryWrapper<Cart> queryWrapper=new QueryWrapper<Cart>();
        queryWrapper.eq("user_id",cart.getUserId())
                .eq("item_id",cart.getItemId());
        Cart cartTemp = cartMapper.selectOne(queryWrapper);
        if(cartTemp==null){
            cart.setCreated(new Date())
                    .setUpdated(cart.getCreated());
            cartMapper.insert(cart);
        }else{
            int num=cart.getNum()+cartTemp.getNum();
            cartTemp.setNum(num).setUpdated(new Date());
            cartMapper.updateById(cartTemp);
        }
    }

    @Override
    public void deleteCart(Cart cart) {

        UpdateWrapper<Cart> updateWrapper=new UpdateWrapper<>();
        updateWrapper.eq("user_id",
                cart.getUserId()).eq("item_id",
                cart.getItemId());
        cartMapper.delete(updateWrapper);
    }
}
