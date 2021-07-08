package com.ssm.mall.service.iservice;


import com.ssm.mall.common.ServerRes;
import com.ssm.mall.dao.pojo.PayInfo;

public interface PayInfoService {

    ServerRes insertPayInfo(PayInfo payInfo);
}
