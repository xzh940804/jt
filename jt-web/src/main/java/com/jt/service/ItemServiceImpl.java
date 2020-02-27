package com.jt.service;

import com.jt.ano.CacheFind;
import com.jt.pojo.Item;
import com.jt.pojo.ItemDesc;
import com.jt.util.HttpClientService;
import com.jt.utils.ObjectMapperUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ItemServiceImpl implements  ItemService{
    @Autowired
    private HttpClientService httpclient;

    @Override
    @CacheFind
    public Item findItemById(Long itemId) {
        String url = "http://manage.jt.com/web/item/findItemById/"+itemId;
        String itemJSON = httpclient.doGet(url);

        return ObjectMapperUtils.toObj(itemJSON, Item.class);

    }

    @Override
    @CacheFind
    public ItemDesc findItemDescById(Long itemId) {
        String url = "http://manage.jt.com/web/item/findItemDescById/"+itemId;
        String itemJSON = httpclient.doGet(url);

        return ObjectMapperUtils.toObj(itemJSON, ItemDesc.class);

    }
}
