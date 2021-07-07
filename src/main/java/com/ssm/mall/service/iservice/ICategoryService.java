package com.ssm.mall.service.iservice;

import com.ssm.mall.common.ServerRes;
import com.ssm.mall.dao.pojo.Category;

import java.util.List;

public interface ICategoryService {
    ServerRes addCategory(Integer parentId,String categoryName);

    ServerRes updateCategory(Integer categoryId, String categoryName);

    ServerRes<List<Category>> getParallelCategories(Integer parentId);

    ServerRes getDeepCategory(Integer categoryId);
}
