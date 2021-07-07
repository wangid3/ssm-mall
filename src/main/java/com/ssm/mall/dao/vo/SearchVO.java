package com.ssm.mall.dao.vo;

import java.math.BigDecimal;

public class SearchVO {

    private Long orderNo;
    private String username;
    private BigDecimal minPayment;
    private BigDecimal maxPayment;

    public SearchVO(Long orderNo, String username, BigDecimal minPayment, BigDecimal maxPayment) {
        this.orderNo=orderNo;
        this.username=username;
        this.minPayment=minPayment;
        this.maxPayment=maxPayment;
    }

    public Long getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(Long orderNo) {
        this.orderNo = orderNo;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public BigDecimal getMinPayment() {
        return minPayment;
    }

    public void setMinPayment(BigDecimal minPayment) {
        this.minPayment = minPayment;
    }

    public BigDecimal getMaxPayment() {
        return maxPayment;
    }

    public void setMaxPayment(BigDecimal maxPayment) {
        this.maxPayment = maxPayment;
    }
}
