package com.ssm.mall.dao;

import com.ssm.mall.dao.pojo.Cart;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CartMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Cart record);

    int insertSelective(Cart record);

    Cart selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Cart record);

    int updateByPrimaryKey(Cart record);

    List<Cart> selectByUserid(Integer userId);

    Cart selectByUseridAndProduct(@Param("userId") Integer userId,@Param("productId") Integer productId);

    int deleteProductByUserid(@Param("userId") Integer userId,@Param("productIds") List<String> productIds);

    void updateAllCheck(@Param("userId") Integer userId,@Param("checked") Integer checked);

    void updateUpdateProductCheck(@Param("userId") Integer userId,@Param("productId") Integer productId,@Param("checked") Integer checked);

    Integer productCount(Integer userId);

    List<Cart> selectCheckedCarts(@Param("userId") Integer userId);
}