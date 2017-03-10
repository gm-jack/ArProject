package com.rtmap.game.model;

import java.io.Serializable;

/**
 * Created by yxy on 2017/3/8.
 */
public class Result implements Serializable {

    /**
     * code : 0
     * message : 已获奖
     * marketName : null
     * buildId : 860100010040500000
     * shopName : 致真大厦C座10层
     * logoUrl : http://res.rtmap.com/image/icon/1460434968795.jpg
     * level : -1
     * main : AR游戏礼品券
     * extend : AR游戏礼品券
     * startTime : 2017-03-06
     * endTime : 2018-05-10
     * position : 北京市海淀区知春路7号致真大厦
     * imgUrl : http://res.rtmap.com/image/prize_pic/2017-03/1488787034247.png
     * qr : 011488952536119406
     * status : null
     * template : http://res.rtmap.com/js/template/default_template_01.js
     * tt : 0
     * pid : 164329
     * num : 10000
     * issue : 10
     * coupon : 1
     * openId : 15210420307
     * nickname : null
     * head : null
     * desc : 无
     * refund : 0
     * wxsync : null
     * cardId : null
     * share : 0
     * follow : 1
     * price : 100
     * notifyType : 0
     * notifyMessage : null
     */

    private String code;
    private String message;
    private String marketName;
    private String buildId;
    private String shopName;
    private String logoUrl;
    private String level;
    private String main;
    private String extend;
    private String startTime;
    private String endTime;
    private String position;
    private String imgUrl;
    private String qr;
    private String status;
    private String template;
    private String tt;
    private String pid;
    private String num;
    private String issue;
    private String coupon;
    private String openId;
    private String nickname;
    private String head;
    private String desc;
    private String refund;
    private String wxsync;
    private String cardId;
    private String share;
    private String follow;
    private String price;
    private String notifyType;
    private String notifyMessage;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMarketName() {
        return marketName;
    }

    public void setMarketName(String marketName) {
        this.marketName = marketName;
    }

    public String getBuildId() {
        return buildId;
    }

    public void setBuildId(String buildId) {
        this.buildId = buildId;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getMain() {
        return main;
    }

    public void setMain(String main) {
        this.main = main;
    }

    public String getExtend() {
        return extend;
    }

    public void setExtend(String extend) {
        this.extend = extend;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getQr() {
        return qr;
    }

    public void setQr(String qr) {
        this.qr = qr;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public String getTt() {
        return tt;
    }

    public void setTt(String tt) {
        this.tt = tt;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getIssue() {
        return issue;
    }

    public void setIssue(String issue) {
        this.issue = issue;
    }

    public String getCoupon() {
        return coupon;
    }

    public void setCoupon(String coupon) {
        this.coupon = coupon;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getHead() {
        return head;
    }

    public void setHead(String head) {
        this.head = head;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getRefund() {
        return refund;
    }

    public void setRefund(String refund) {
        this.refund = refund;
    }

    public String getWxsync() {
        return wxsync;
    }

    public void setWxsync(String wxsync) {
        this.wxsync = wxsync;
    }

    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }

    public String getShare() {
        return share;
    }

    public void setShare(String share) {
        this.share = share;
    }

    public String getFollow() {
        return follow;
    }

    public void setFollow(String follow) {
        this.follow = follow;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getNotifyType() {
        return notifyType;
    }

    public void setNotifyType(String notifyType) {
        this.notifyType = notifyType;
    }

    public String getNotifyMessage() {
        return notifyMessage;
    }

    public void setNotifyMessage(String notifyMessage) {
        this.notifyMessage = notifyMessage;
    }

    @Override
    public String toString() {
        return "Result{" +
                "code='" + code + '\'' +
                ", message='" + message + '\'' +
                ", marketName='" + marketName + '\'' +
                ", buildId='" + buildId + '\'' +
                ", shopName='" + shopName + '\'' +
                ", logoUrl='" + logoUrl + '\'' +
                ", level='" + level + '\'' +
                ", main='" + main + '\'' +
                ", extend='" + extend + '\'' +
                ", startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                ", position='" + position + '\'' +
                ", imgUrl='" + imgUrl + '\'' +
                ", qr='" + qr + '\'' +
                ", status='" + status + '\'' +
                ", template='" + template + '\'' +
                ", tt='" + tt + '\'' +
                ", pid='" + pid + '\'' +
                ", num='" + num + '\'' +
                ", issue='" + issue + '\'' +
                ", coupon='" + coupon + '\'' +
                ", openId='" + openId + '\'' +
                ", nickname='" + nickname + '\'' +
                ", head='" + head + '\'' +
                ", desc='" + desc + '\'' +
                ", refund='" + refund + '\'' +
                ", wxsync='" + wxsync + '\'' +
                ", cardId='" + cardId + '\'' +
                ", share='" + share + '\'' +
                ", follow='" + follow + '\'' +
                ", price='" + price + '\'' +
                ", notifyType='" + notifyType + '\'' +
                ", notifyMessage='" + notifyMessage + '\'' +
                '}';
    }
}
