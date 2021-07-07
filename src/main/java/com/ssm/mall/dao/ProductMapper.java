package com.ssm.mall.dao;

import com.ssm.mall.dao.pojo.Product;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface ProductMapper {
    int deleteByPrimaryKey(Integer productId);

    int insert(Product record);

    int insertSelective(Product record);

    Product selectByPrimaryKey(Integer productId);

    int updateByPrimaryKeySelective(Product record);

    int updateByPrimaryKey(Product record);

    List<Product> selectList();

    List<Product> searchByIdName(@Param("id") Integer id,@Param("name") String name);

    List<Product> selectByKeywordsCategoryIds(@Param("keywords") String s,@Param("categoryIds") List<Integer> integers);
}