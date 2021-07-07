package com.ssm.mall.dao.vo;
import java.math.BigDecimal;

import java.util.List;

public class ItemsPreviewVO {
    private List<ItemVO> itemVOList;
    private BigDecimal orderTotalPrice;
    private String imageHost;
    public List<ItemVO> getItemVOList() {
        return itemVOList;
    }
    public void setItemVOList(List<ItemVO> itemVOList) {
        this.itemVOList = itemVOList;
    }
    public BigDecimal getOrderTotalPrice() {
        return orderTotalPrice;
    }
    public void setOrderTotalPrice(BigDecimal orderTotalPrice) {
        this.orderTotalPrice = orderTotalPrice;
    }
    public String getImageHost() {
        return imageHost;
    }
    public void setImageHost(String imageHost) {
        this.imageHost = imageHost;
    }
}