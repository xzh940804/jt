package com.jt.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.jt.pojo.Cart;
import com.jt.service.DubboCartService;
import com.jt.util.UserThreadLocal;
import com.jt.vo.SysResult;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.jws.soap.SOAPBinding;
import java.util.List;

@Controller
@RequestMapping("/cart")
public class CartController {
    @Reference
    private DubboCartService cartService;

    @RequestMapping("/show")
    public String show(Model model){
        Long userId=UserThreadLocal.get().getId();
        List<Cart> cartList=cartService.findCartListByUserId(userId);
        model.addAttribute("cartList",cartList);
        return "cart";
    }
    @RequestMapping("/update/num/{itemId}/{num}")
    @ResponseBody
    public SysResult updateNum(Cart cart){
        long userId=UserThreadLocal.get().getId();
        cart.setUserId(userId);
        cartService.updateCartNum(cart);
        return SysResult.success();
    }
    @RequestMapping("/add/{itemId}")
    public String saveCart(Cart cart){
        Long userId = UserThreadLocal.get().getId();
        cart.setUserId(userId);

        cartService.saveCart(cart);
        return "redirect:/cart/show.html";
    }
    //http://www.jt.com/cart/delete/562379.html
    @RequestMapping("/delete/{itemId}")
    public String deleteCart(Cart cart){
        Long userId=UserThreadLocal.get().getId();
        cart.setUserId(userId);
        cartService.deleteCart(cart);
        return  "redirect:/cart/show.html";
    }
}
