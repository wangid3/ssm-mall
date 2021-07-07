package com.ssm.mall.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.ssm.mall.common.Result;
import com.ssm.mall.common.ServerRes;
import com.ssm.mall.dao.ShippingMapper;
import com.ssm.mall.dao.pojo.Product;
import com.ssm.mall.dao.pojo.Shipping;
import com.ssm.mall.dao.vo.ProductListVo;
import com.ssm.mall.service.iservice.ShippingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service("shoppingService")
public class ShippingServiceImpl implements ShippingService {

    @Autowired
    ShippingMapper shippingMapper;

    @Override
    public ServerRes deleteByUseridShippingid(Integer userId, Integer shippingId) {
        int result=shippingMapper.deleteByUseridShippingid(userId,shippingId);
        return result>0?
                ServerRes.success(Result.RESULT_SUCCESS)
                :ServerRes.error(Result.RESULT_ERROR);

    }

    @Override
    public ServerRes insert(Integer userId, Shipping shipping) {
        shipping.setUserId(userId);
        int result =shippingMapper.insert(shipping);
        if(result>0){
            Map resultMap= Maps.newHashMap();
            resultMap.put("shipping",shipping.getUserId());
            return ServerRes.success(Result.RESULT_SUCCESS,resultMap);
        }
        return ServerRes.error(Result.RESULT_ERROR);
    }

    @Override
    public ServerRes updateByUseridShipping(Integer userId, Shipping shipping) {
        shipping.setUserId(userId);
        int result=shippingMapper.updateByUseridShippingid(shipping);
        return result>0?
                ServerRes.success(Result.RESULT_SUCCESS)
                :ServerRes.error(Result.RESULT_ERROR);
    }

    @Override
    public ServerRes<Shipping> selectByUseridShipping(Integer userId, Integer shippingId) {
        Shipping shipping=(Shipping) shippingMapper.selectByUseridShippingid(userId,shippingId);
        if(shipping==null){
            return ServerRes.error(Result.RESULT_ERROR);
        }
        return ServerRes.success(Result.RESULT_SUCCESS,shipping);
    }

    @Override
    public ServerRes<PageInfo> selectByUserId(Integer userId, Integer pageNum, Integer pageSize) {
        //PageHelper面向切面编程
        //1.PageHelper设置启动
        PageHelper.startPage(pageNum,pageSize);
        //2 查询
        List<Shipping> productList=shippingMapper.selectByUserId(userId);
        PageInfo pageResult=new PageInfo(productList);
        return ServerRes.success(Result.RESULT_SUCCESS,pageResult);
    }
}
