package com.ssm.mall.dao.pojo;

import java.math.BigDecimal;
import java.util.Date;

public class Product {
    private Integer productId;

    private Integer categoryId;

    public Product(Integer productId, Integer categoryId, String name, String subtitle, String mainImage,  BigDecimal price, Integer stock, Integer status, Date createTime, Date updateTime,String subImage, String detail) {
        this.productId = productId;
        this.categoryId = categoryId;
        this.name = name;
        this.subtitle = subtitle;
        this.mainImage = mainImage;
        this.subImage = subImage;
        this.detail = detail;
        this.price = price;
        this.stock = stock;
        this.status = status;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }

    public Product(Integer productId, Integer categoryId, String name, String subtitle, String mainImage,  BigDecimal price, Integer stock, Integer status, Date createTime, Date updateTime) {
        this.productId = productId;
        this.categoryId = categoryId;
        this.name = name;
        this.subtitle = subtitle;
        this.mainImage = mainImage;
        this.price = price;
        this.stock = stock;
        this.status = status;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }

    public Product(Integer categoryId, String name, String subtitle, BigDecimal price, Integer stock, Integer status,String subImage, String detail) {
        this.categoryId = categoryId;
        this.name = name;
        this.subtitle = subtitle;
        this.subImage = subImage;
        this.detail = detail;
        this.price = price;
        this.stock = stock;
        this.status = status;
    }

    @Override
    public String toString() {
        return "Product{" +
                "productId=" + productId +
                ", categoryId=" + categoryId +
                ", name='" + name + '\'' +
                ", subtitle='" + subtitle + '\'' +
                ", mainImage='" + mainImage + '\'' +
                ", subImage='" + subImage + '\'' +
                ", detail='" + detail + '\'' +
                ", price=" + price +
                ", stock=" + stock +
                ", status=" + status +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }

    public String getSubImage() {
        return subImage;
    }

    public void setSubImage(String subImage) {
        this.subImage = subImage;
    }

    private String name;

    private String subtitle;

    private String mainImage;

    private  String subImage;

    private String detail;

    private BigDecimal price;

    private Integer stock;

    private Integer status;

    private Date createTime;

    private Date updateTime;



    public Product() {
        super();
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle == null ? null : subtitle.trim();
    }

    public String getMainImage() {
        return mainImage;
    }

    public void setMainImage(String mainImage) {
        this.mainImage = mainImage == null ? null : mainImage.trim();
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}