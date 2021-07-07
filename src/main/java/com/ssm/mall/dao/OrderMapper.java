package com.ssm.mall.dao;

import com.ssm.mall.dao.pojo.Item;
import com.ssm.mall.dao.pojo.Order;
import com.ssm.mall.dao.vo.SearchVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface OrderMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Order record);

    int insertSelective(Order record);

    Order selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Order record);

    int updateByPrimaryKey(Order record);

    Order selectByUseridAndOrderno(@Param("userId") Integer userId,@Param("orderNo") Long orderNo);


    List<Order> selectByUserid(@Param("userId") Integer userId);

    List<Order> selectAll();

    Order selectByOrderno(Long orderNo);

    List<Order> msearch(SearchVO sv);
}