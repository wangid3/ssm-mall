package com.ssm.mall.dao;

import com.ssm.mall.dao.pojo.Item;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ItemMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Item record);

    int insertSelective(Item record);

    Item selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Item record);

    int updateByPrimaryKey(Item record);

    List<Item> selectAllByUseridAndOrderno(@Param("userId") Integer userId, @Param("orderNo") Long orderNo);

    int batchInsertItems(List<Item> itemList);

    List<Item> selectAllByOrderNo(Long orderNo);

}