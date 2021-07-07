package com.ssm.mall.dao.vo;

import java.math.BigDecimal;
import java.util.Date;

public class CartItemVo {
    private Integer id;
    private Integer userId;
    private Integer productId;
    private Integer quantity;//购买的数量
    private Integer checked;//是否被选中
    private String limitQuantity;//限制购买数量

    //商品信息
    private String proName;
    private String proSubtitle;
    private String proMainImage;
    private  String subImage;
    private Integer proStatus;
    private Integer proStock;
    private BigDecimal proPrice;

    //统计数据
    private BigDecimal cartItemTotalPrice;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getChecked() {
        return checked;
    }

    public void setChecked(Integer checked) {
        this.checked = checked;
    }

    public String getLimitQuantity() {
        return limitQuantity;
    }

    public void setLimitQuantity(String limitQuantity) {
        this.limitQuantity = limitQuantity;
    }

    public String getProName() {
        return proName;
    }

    public void setProName(String proName) {
        this.proName = proName;
    }

    public String getProSubtitle() {
        return proSubtitle;
    }

    public void setProSubtitle(String proSubtitle) {
        this.proSubtitle = proSubtitle;
    }

    public String getProMainImage() {
        return proMainImage;
    }

    public void setProMainImage(String proMainImage) {
        this.proMainImage = proMainImage;
    }

    public String getSubImage() {
        return subImage;
    }

    public void setSubImage(String subImage) {
        this.subImage = subImage;
    }

    public Integer getProStatus() {
        return proStatus;
    }

    public void setProStatus(Integer proStatus) {
        this.proStatus = proStatus;
    }

    public Integer getProStock() {
        return proStock;
    }

    public void setProStock(Integer proStock) {
        this.proStock = proStock;
    }

    public BigDecimal getProPrice() {
        return proPrice;
    }

    public void setProPrice(BigDecimal proPrice) {
        this.proPrice = proPrice;
    }

    public BigDecimal getCartItemTotalPrice() {
        return cartItemTotalPrice;
    }

    public void setCartItemTotalPrice(BigDecimal cartItemTotalPrice) {
        this.cartItemTotalPrice = cartItemTotalPrice;
    }
}
