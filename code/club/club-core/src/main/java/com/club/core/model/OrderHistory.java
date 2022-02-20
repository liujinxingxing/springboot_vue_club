package com.club.core.model;

import com.club.core.constant.TableLib;

import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Date;

/**
 * 订单历史
 * @model 订单历史
 * @author 阳春白雪 | sample@gmail.com
 * @since 1.0
 * @date 2022年2月20日
 */
@Table(name = OrderHistory.TABLE_NAME)
public class OrderHistory extends Order {

    public static final String TABLE_NAME = TableLib.CLUB + "ORDER_HIS";

    /**
     * 是否交易成功
     */
    private boolean result;

    /**
     * 出借小时
     */
    private int hours;

    /**
     * 出借总费用
     */
    private int price;

    /**
     * 结束时间
     */
    private Date finishTime;

    /**
     * 位置或者 器材名称
     */
    @Transient
    private String positon;

    public String getPositon() {
        return positon;
    }

    public void setPositon(String positon) {
        this.positon = positon;
    }

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public int getHours() {
        return hours;
    }

    public void setHours(int hours) {
        this.hours = hours;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public Date getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(Date finishTime) {
        this.finishTime = finishTime;
    }
}
