package com.club.core.vo;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * OrderVo的包装类描述
 * @model 订单(请求)包装
 * @author 阳春白雪 | sample@gmail.com
 * @since 1.0
 * @date 2022年2月20日
 */
public class OrderVo {

    /**
     * 订单id 存在订单id 则认为已经预约了
     */
    private String uuid;

    /**
     * 购物车
     * @required
     */
    @NotNull
    private String prefixId;

    /**
     * 订单模式 0 预约 1 订单
     * @required
     */
    private int entry;

    /**
     * 使用者信息
     * @required
     */
    @NotNull
    private String useInfo;

    /**
     * 使用者电话
     * @required
     */
    @NotNull
    private String contact;

    /**
     * 开始时间
     * @required
     */
    @NotNull
    private Date startTime;

    public String getPrefixId() {
        return prefixId;
    }

    public void setPrefixId(String prefixId) {
        this.prefixId = prefixId;
    }

    public int getEntry() {
        return entry;
    }

    public void setEntry(int entry) {
        this.entry = entry;
    }

    public String getUseInfo() {
        return useInfo;
    }

    public void setUseInfo(String useInfo) {
        this.useInfo = useInfo;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}
