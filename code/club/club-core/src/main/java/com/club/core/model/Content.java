package com.club.core.model;

import com.club.base.model.BaseModel;
import com.club.core.constant.TableLib;
import com.club.framework.valid.groups.Insert;
import com.club.framework.valid.groups.Update;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;

/**
 * 场地，器材信息
 *
 * @author 阳春白雪 | sample@gmail.com
 * @model 场地，器材信息
 * @date 2022年2月20日
 * @since 1.0
 */
@Table(name = Content.TABLE_NAME)
public class Content extends BaseModel implements Serializable {

    private static final long serialVersionUID = -378119039761L;

    public static final String TABLE_NAME = TableLib.CLUB + "CONTENT";
    public static final String FIELD_CODE = "code";
    public static final String FIELD_MODE = "mode";

    /**
     * id 场地编号
     */
    @Id
    @NotBlank
    @Size(max = 50)
    private String code;

    /**
     * 订单类型 0 器材 1 场地
     */
    @Id
    private int mode;

    /**
     * 场地 时场地详细地点
     */
    @NotBlank
    @Size(max = 500)
    private String positon;

    /**
     * 出借费用 单位/小时 ，不足就按小时计算
     */
    @NotNull
    private Integer price;

    /**
     * 备注
     */
    @Size(max = 500)
    private String descripter;

    /**
     * 创建时间
     */
    private Date createTime;


    public Content() {
    }

    public Content(String code, int mode) {
        this.code = code;
        this.mode = mode;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
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

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getDescripter() {
        return descripter;
    }

    public void setDescripter(String descripter) {
        this.descripter = descripter;
    }
}
