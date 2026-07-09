package com.community.communityservice.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.math.BigDecimal;
import java.sql.Timestamp;

@TableName("s_community")
public class Community {

    @TableId(value = "community_id", type = IdType.INPUT)
    private String communityId;

    @TableField("b_id")
    private String bId;

    private String name;

    private String address;

    @TableField("nearby_landmarks")
    private String nearbyLandmarks;

    @TableField("city_code")
    private String cityCode;

    @TableField("map_x")
    private String mapX;

    @TableField("map_y")
    private String mapY;

    @TableField("create_time")
    private Timestamp createTime;

    @TableField("status_cd")
    private String statusCd;

    private String state;

    @TableField("community_area")
    private BigDecimal communityArea;

    private String tel;

    @TableField("pay_fee_month")
    private Long payFeeMonth;

    @TableField("fee_price")
    private BigDecimal feePrice;

    @TableField("qr_code")
    private String qrCode;

    public Community() {
    }

    public String getCommunityId() {
        return communityId;
    }

    public void setCommunityId(String communityId) {
        this.communityId = communityId;
    }

    public String getbId() {
        return bId;
    }

    public void setbId(String bId) {
        this.bId = bId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getNearbyLandmarks() {
        return nearbyLandmarks;
    }

    public void setNearbyLandmarks(String nearbyLandmarks) {
        this.nearbyLandmarks = nearbyLandmarks;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public String getMapX() {
        return mapX;
    }

    public void setMapX(String mapX) {
        this.mapX = mapX;
    }

    public String getMapY() {
        return mapY;
    }

    public void setMapY(String mapY) {
        this.mapY = mapY;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public String getStatusCd() {
        return statusCd;
    }

    public void setStatusCd(String statusCd) {
        this.statusCd = statusCd;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public BigDecimal getCommunityArea() {
        return communityArea;
    }

    public void setCommunityArea(BigDecimal communityArea) {
        this.communityArea = communityArea;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public Long getPayFeeMonth() {
        return payFeeMonth;
    }

    public void setPayFeeMonth(Long payFeeMonth) {
        this.payFeeMonth = payFeeMonth;
    }

    public BigDecimal getFeePrice() {
        return feePrice;
    }

    public void setFeePrice(BigDecimal feePrice) {
        this.feePrice = feePrice;
    }

    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }
}