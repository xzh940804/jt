package com.jt.service;

import com.jt.pojo.Item;
import com.jt.pojo.ItemDesc;
import com.jt.vo.EasyUITable;

public interface ItemService {

    EasyUITable findItemByPage(int page, int rows);

    void save(Item item, ItemDesc itemDesc);

    ItemDesc findItemDescById(Long itemId);

    void updateItem(Item item, ItemDesc itemDesc);

    void deleteItem(Long[] ids);



    void updateStatus(Long[] ids, int status);

    Item findItemById(Long itemId);
}
