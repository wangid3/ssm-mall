package com.ssm.mall.dao.vo;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ssm.mall.dao.pojo.Item;
import org.springframework.format.annotation.DateTimeFormat;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
public class OrderVO{
    private Long orderNo;
    private BigDecimal payment;
    private Integer paymentType;
    private String paymentTypeDesc;
    private Integer postage;
    private Integer status;
    private String statusDesc;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date paymentTime;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date sendTime;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date closeTime;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    //订单明细
    List<ItemVO> itemVOList;
    //图片服务器地址
    private String imageHost;
    //收货信息
    private Integer shippingId;
    private String receiverName;
    private ShippingVO shippingVO;
    public Long getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(Long orderNo) {
        this.orderNo = orderNo;
    }
    public BigDecimal getPayment() {
        return payment;
    }
    public void setPayment(BigDecimal payment) {
        this.payment = payment;
    }
    public Integer getPaymentType() {
        return paymentType;
    }
    public void setPaymentType(Integer paymentType) {
        this.paymentType = paymentType;
    }
    public String getPaymentTypeDesc() {
        return paymentTypeDesc;
    }
    public void setPaymentTypeDesc(String paymentTypeDesc) {
        this.paymentTypeDesc = paymentTypeDesc;
    }
    public Integer getPostage() {
        return postage;
    }
    public void setPostage(Integer postage) {
        this.postage = postage;
    }
    public Integer getStatus() {
        return status;
    }
    public void setStatus(Integer status) {
        this.status = status;
    }
    public String getStatusDesc() {
        return statusDesc;
    }
    public void setStatusDesc(String statusDesc) {
        this.statusDesc = statusDesc;
    }
    public Date getPaymentTime() {
        return paymentTime;
    }
    public void setPaymentTime(Date paymentTime) {
        this.paymentTime = paymentTime;
    }
    public Date getSendTime() {
        return sendTime;
    }
    public void setSendTime(Date sendTime) {
        this.sendTime = sendTime;
    }
    public Date getEndTime() {
        return endTime;
    }
    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }
    public Date getCloseTime() {
        return closeTime;
    }
    public void setCloseTime(Date closeTime) {
        this.closeTime = closeTime;
    }
    public Date getCreateTime() {
        return createTime;
    }
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
    public List<ItemVO> getItemVOList() {
        return itemVOList;
    }
    public void setItemVOList(List<ItemVO> itemVOList) {
        this.itemVOList = itemVOList;
    }
    public String getImageHost() {
        return imageHost;
    }
    public void setImageHost(String imageHost) {
        this.imageHost = imageHost;
    }
    public Integer getShippingId() {
        return shippingId;
    }
    public void setShippingId(Integer shippingId) {
        this.shippingId = shippingId;
    }
    public String getReceiverName() {
        return receiverName;
    }
    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }
    public ShippingVO getShippingVO() {
        return shippingVO;
    }
    public void setShippingVO(ShippingVO shippingVO) {
        this.shippingVO = shippingVO;
    }
}