package com.club.core.vo;

import com.club.core.model.Content;

import javax.persistence.Id;
import java.util.Date;

/**
 * ContentVo的包装类描述
 * @model 器材或场地包装类
 * @author 阳春白雪 | sample@gmail.com
 * @since 1.0
 * @date 2022年2月20日
 */
public class ContentVo extends Content {

    /**
     * 订单id
     */
    private String uuid;

    /**
     * 使用者信息
     */
    private String useInfo;

    /**
     * 使用者电话
     */
    private String contact;

    /**
     * 订单模式 0 预约 1 订单
     */
    private Integer entry;

    /**
     * 开始时间
     */
    private Date startTime;

    /**
     * 累计使用小时
     */
    public long getHours() {
        if(entry == null || entry == 0 || startTime== null){
            return 0;
        }
        long millis = System.currentTimeMillis() - startTime.getTime();
        return millis/3600000;
    }

    /**
     * 累计价格
     */
    public long getAllPrice() {
        Integer price = getPrice();
        if(entry == null || entry == 0 || price == null){
            return 0;
        }
        return getHours() * price.intValue();
    }


    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
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

    public Integer getEntry() {
        return entry;
    }

    public void setEntry(Integer entry) {
        this.entry = entry;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }
}
