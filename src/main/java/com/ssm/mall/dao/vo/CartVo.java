package com.ssm.mall.dao.vo;

import java.math.BigDecimal;
import java.util.List;

public class CartVo {

    private String imageHost;//ftp图片服务器地址
    private List<CartItemVo> cartItemList;//购物车选项集合
    private BigDecimal cartTotalPrice;//购物车总价格
    private Boolean allChecked;//是否全部被选中

    public String getImageHost() {
        return imageHost;
    }

    public void setImageHost(String imageHost) {
        this.imageHost = imageHost;
    }

    public List<CartItemVo> getCartItemList() {
        return cartItemList;
    }

    public void setCartItemList(List<CartItemVo> cartItemList) {
        this.cartItemList = cartItemList;
    }

    public BigDecimal getCartTotalPrice() {
        return cartTotalPrice;
    }

    public void setCartTotalPrice(BigDecimal cartTotalPrice) {
        this.cartTotalPrice = cartTotalPrice;
    }

    public Boolean getAllChecked() {
        return allChecked;
    }

    public void setAllChecked(Boolean allChecked) {
        this.allChecked = allChecked;
    }
}