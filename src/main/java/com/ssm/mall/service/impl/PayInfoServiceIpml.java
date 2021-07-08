package com.ssm.mall.service.impl;

import com.ssm.mall.common.Result;
import com.ssm.mall.common.ServerRes;
import com.ssm.mall.dao.PayInfoMapper;
import com.ssm.mall.dao.pojo.PayInfo;
import com.ssm.mall.service.iservice.PayInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("payInfoService")
public class PayInfoServiceIpml implements PayInfoService {
    @Autowired
    PayInfoMapper payInfoMapper;

    @Override
    public ServerRes insertPayInfo(PayInfo payInfo) {
        payInfoMapper.insertSelective(payInfo);
        return ServerRes.success(Result.RESULT_SUCCESS);
    }
}
