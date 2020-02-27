package com.jt.service;

import com.jt.pojo.ItemCat;
import com.jt.vo.EasyUITree;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemCatService {
    ItemCat findItemCatByName(long itemId);

    List<EasyUITree> findItemCatByParentId(Long parentId);

   // List<EasyUITree> findItemCatByCache(Long parentId);
}
