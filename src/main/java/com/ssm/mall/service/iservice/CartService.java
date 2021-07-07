package com.ssm.mall.service.iservice;

import com.ssm.mall.common.ServerRes;
import com.ssm.mall.dao.vo.CartVo;

public interface CartService {
    ServerRes<CartVo> list(Integer userId);

    ServerRes<CartVo> addProduct(Integer userId, Integer productId, Integer num);

    ServerRes<CartVo> updateProductNum(Integer userId, Integer productId, Integer num);

    ServerRes<CartVo> deleteProductBatch(Integer userId, String productIds);

    ServerRes<CartVo> updateAllCheck(Integer userId, Integer checked);

    ServerRes<CartVo> updateProductCheck(Integer userId, Integer productId, Integer checked);

    ServerRes<Integer> productCount(Integer userId);
}
