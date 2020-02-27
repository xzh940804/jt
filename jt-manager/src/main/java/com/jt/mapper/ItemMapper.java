package com.jt.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jt.pojo.Item;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemMapper extends BaseMapper<Item> {

    @Select("SELECT * FROM tb_item ORDER BY updated desc limit #{start},#{rows}")
    List<Item> findItemByPage(int start, int rows);
}
