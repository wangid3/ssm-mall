package com.ssm.mall.service.iservice;

import com.ssm.mall.common.ServerRes;
import com.ssm.mall.dao.vo.SearchVO;

public interface OrderService {
    ServerRes pay(Integer userId, Long orderNo, String webServerPath);


    ServerRes createOrder(Integer userId, Integer shipping);

    ServerRes productsPreview(Integer userId);

    ServerRes listByKeyCategory(Integer userId, int pageNum, int pageSize);

    ServerRes getDetailByOrderNo(Integer userId, Long orderNo);

    ServerRes getDetailByOrderNo(Long orderNo);

    ServerRes cancelOrder(Integer userId, Long orderNo);

    ServerRes mlist(int pageNum, int pageSize);

    ServerRes msearch(SearchVO sv, int pageNum, int pageSize);

    ServerRes updateOrderStatusSend(Long orderNo);
}
