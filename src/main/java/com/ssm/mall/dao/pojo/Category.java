package com.ssm.mall.dao.pojo;

import java.util.Date;
import java.util.Objects;

public class Category {


    public Category(Integer categoryId, String categoryName) {
        this.categoryId=categoryId;
        this.name=categoryName;
    }

    @Override
    public String toString() {
        return "Category{" +
                "categoryId=" + categoryId +
                ", parentId=" + parentId +
                ", name='" + name + '\'' +
                ", status=" + status +
                ", sort_id=" + sort_id +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }
    private Integer categoryId;

    private Integer parentId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Category)) return false;
        Category category = (Category) o;
        return Objects.equals(getCategoryId(), category.getCategoryId()) &&
                Objects.equals(getParentId(), category.getParentId()) &&
                Objects.equals(getName(), category.getName()) &&
                Objects.equals(getStatus(), category.getStatus()) &&
                Objects.equals(getSort_id(), category.getSort_id()) &&
                Objects.equals(getCreateTime(), category.getCreateTime()) &&
                Objects.equals(getUpdateTime(), category.getUpdateTime());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCategoryId(), getParentId(), getName(), getStatus(), getSort_id(), getCreateTime(), getUpdateTime());
    }

    private String name;

    private Integer status;

    private Integer sort_id;

    private Date createTime;

    private Date updateTime;

    public Category(Integer categoryId, Integer parentId, String name, Integer status, Integer sort_id, Date createTime, Date updateTime) {
        this.categoryId = categoryId;
        this.parentId = parentId;
        this.name = name;
        this.status = status;
        this.sort_id = sort_id;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }

    public Category() {
        super();
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getSort_id() {
        return sort_id;
    }

    public void setSort_id(Integer sort_id) {
        this.sort_id = sort_id;
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