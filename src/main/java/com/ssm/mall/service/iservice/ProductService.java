package com.ssm.mall.service.iservice;

import com.github.pagehelper.PageInfo;
import com.ssm.mall.common.ServerRes;
import com.ssm.mall.dao.pojo.Product;
import com.ssm.mall.dao.vo.ProductDetailVo;

public interface ProductService {
    ServerRes saveOrUpdate(Product product);

    ServerRes setProductStatus(Integer id, Integer status);

    ServerRes getManagerDetail(Integer id);

    ServerRes getManagerList(int pageNum, int pageSize);

    ServerRes searchByIdName(Integer id,String name, int pageNum, int pageSize);

    ServerRes<ProductDetailVo> getProductDetail(Integer id);

    ServerRes<PageInfo> listByKeyCategory(String keywords, Integer categoryId, int pageNum, int pageSize, String orderBy);
}
