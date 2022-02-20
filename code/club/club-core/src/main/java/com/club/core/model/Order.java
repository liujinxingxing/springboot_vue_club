package com.club.core.model;

import com.club.base.model.BaseModel;
import com.club.core.constant.TableLib;
import com.club.framework.valid.groups.Insert;
import com.club.framework.valid.groups.Update;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;

/**
 * 订单
 * @model 订单
 * @author 阳春白雪 | sample@gmail.com
 * @since 1.0
 * @date 2022年2月20日
 */
@Table(name = Order.TABLE_NAME)
public class Order extends BaseModel implements Serializable {

    private static final long serialVersionUID = -379039240349761L;
    public static final String FIELD_UUID = "uuid";
    public static final String FIELD_ENTRY = "entry";
    public static final String FIELD_USER = "updateUserId";
    public static final String TABLE_NAME = TableLib.CLUB + "ORDER";

    /**
     * 订单id
     */
    @Id
    private String uuid;

    /**
     * 器材或场地编号
     */
    @Id
    private String code;

    /**
     * 订单类型 0 器材 1 场地
     */
    @Id
    private int mode;

    /**
     * 订单模式 0 预约 1 订单
     */
    private int entry;

    /**
     * 使用者信息
     */
    private String useInfo;

    /**
     * 使用者电话
     */
    private String contact;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 开始时间
     */
    private Date startTime;

    /**
     * 备注
     */
    private String descripter;

    public Order() {
    }

    public Order(String uuid, String code, int mode) {
        this.uuid = uuid;
        this.code = code;
        this.mode = mode;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getEntry() {
        return entry;
    }

    public void setEntry(int entry) {
        this.entry = entry;
    }

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
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

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public String getDescripter() {
        return descripter;
    }

    public void setDescripter(String descripter) {
        this.descripter = descripter;
    }
}
