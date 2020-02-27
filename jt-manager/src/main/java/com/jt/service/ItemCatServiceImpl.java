package com.jt.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jt.ano.CacheFind;
import com.jt.mapper.ItemCatMapper;
import com.jt.pojo.ItemCat;
import com.jt.utils.ObjectMapperUtils;
import com.jt.vo.EasyUITree;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import redis.clients.jedis.Jedis;

import java.util.ArrayList;
import java.util.List;

@Service
public class ItemCatServiceImpl implements ItemCatService{
    @Autowired
    ItemCatMapper itemCatMapper;
    @Autowired(required =false)
    private Jedis jedis;

    @Override
    public ItemCat findItemCatByName(long itemCatId) {
        return itemCatMapper.selectById(itemCatId);
    }

    @Override
    @CacheFind
    public List<EasyUITree> findItemCatByParentId(Long parentId) {
        List<EasyUITree> treeList=new ArrayList<>();
        //查询数据库记录
        List<ItemCat> itemCatList=findItemCatListByParentId(parentId);
        for (ItemCat itemCat:itemCatList
             ) {
            Long id=itemCat.getId();
            String text=itemCat.getName();
            String state=itemCat.getIsParent()?"closed":"open";
            EasyUITree uiTree=new EasyUITree(id,text,state);
            treeList.add(uiTree);

        }

        return treeList;
    }

//    @Override
//    public List<EasyUITree> findItemCatByCache(Long parentId) {
//        Long startTime = System.currentTimeMillis();
//        String key ="com.jt.service.ItemCatServiceImpl.findItemCatByCache::"+parentId;
//        String value=jedis.get(key);
//        System.out.println(value);
//        List<EasyUITree> treeList =new ArrayList<>();
//        if(StringUtils.isEmpty(value)){
//            treeList=findItemCatByParentId(parentId);
//            String json=ObjectMapperUtils.toJson(treeList);
//            jedis.set(key,json);
//            Long endTime = System.currentTimeMillis();
//            Long time = endTime - startTime;
//            System.out.println("第一次查询,耗时:"+time+"毫秒");
//
//        }else{
//            treeList=ObjectMapperUtils.toObj(value,treeList.getClass());
//            Long endTime = System.currentTimeMillis();
//            Long time = endTime - startTime;
//
//            System.out.println("查询缓存,耗时:"+time+"毫秒");
//        }
//        return treeList;
//    }

    public List<ItemCat> findItemCatListByParentId(Long parentId){
        QueryWrapper<ItemCat> queryWrapper=new QueryWrapper<ItemCat>();
        queryWrapper.eq("parent_id",parentId);
        return itemCatMapper.selectList(queryWrapper);
    }
}
