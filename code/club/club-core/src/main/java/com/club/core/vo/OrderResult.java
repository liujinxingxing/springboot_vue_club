package com.club.core.vo;

import com.club.core.model.Order;

import java.util.Date;

/**
 * 订单结果
 * @model 订单结果包装类
 * @author 阳春白雪 | sample@gmail.com
 * @since 1.0
 * @date 2022年2月20日
 */
public class OrderResult extends Order {

    /**
     * 出借费用 单位/小时 ，不足就按小时计算
     */
    private Integer price;

    /**
     * 位置或者 器材名称
     */
    private String positon;

    /**
     * 累计使用小时
     */
    public int getHours() {
        Date startTime = getStartTime();
        if(getEntry() == 0 || startTime== null){
            return 0;
        }
        long millis = System.currentTimeMillis() - startTime.getTime();
        long m = (millis%3600000)/60000;
        if(m > 1){
            return (int)(millis/3600000 + 1);
        }
        return (int)(millis/3600000);
    }

    /**
     * 累计价格
     */
    public int getAllPrice() {
        Integer price = getPrice();
        if(getEntry() == 0 || price == null){
            return 0;
        }
        return (int)(getHours() * price.intValue());
    }

    public String getPositon() {
        return positon;
    }

    public void setPositon(String positon) {
        this.positon = positon;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }
}
