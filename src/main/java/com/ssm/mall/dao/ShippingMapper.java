package com.ssm.mall.dao;

import com.ssm.mall.dao.pojo.Shipping;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ShippingMapper {
    int deleteByPrimaryKey(Integer shippingId);

    int insert(Shipping record);

    int insertSelective(Shipping record);

    Shipping selectByPrimaryKey(Integer shippingId);

    int updateByPrimaryKeySelective(Shipping record);

    int updateByPrimaryKey(Shipping record);

    int updateByUseridShippingid(Shipping shipping);

    int deleteByUseridShippingid(@Param("userId") Integer userId,@Param("shippingId") Integer shippingId);

    Shipping selectByUseridShippingid(@Param("userId") Integer userId,@Param("shippingId") Integer shippingId);

    List<Shipping> selectByUserId(Integer userId);
}