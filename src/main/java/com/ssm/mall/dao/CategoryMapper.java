package com.ssm.mall.dao;

import com.ssm.mall.dao.pojo.Category;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CategoryMapper {
    int deleteByPrimaryKey(Integer categoryId);

    int insert(Category record);

    int insertSelective(Category record);

    Category selectByPrimaryKey(Integer categoryId);

    int updateByPrimaryKeySelective(Category record);

    int updateByPrimaryKey(Category record);

    int checkNameInParentId(@Param("parentId") Integer parentId,@Param("categoryName") String categoryName);

    List<Category> selectByParentId(Integer parentId);
}