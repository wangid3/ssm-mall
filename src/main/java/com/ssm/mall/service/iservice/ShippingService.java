package com.ssm.mall.service.iservice;

import com.github.pagehelper.PageInfo;
import com.ssm.mall.common.ServerRes;
import com.ssm.mall.dao.pojo.Shipping;

public interface ShippingService {
    ServerRes deleteByUseridShippingid(Integer userId, Integer shippingId);

    ServerRes insert(Integer userId, Shipping shipping);

    ServerRes updateByUseridShipping(Integer userId, Shipping shipping);

    ServerRes<Shipping> selectByUseridShipping(Integer userId, Integer shippingId);

    ServerRes<PageInfo> selectByUserId(Integer userId, Integer pageNum, Integer pageSize);
}
