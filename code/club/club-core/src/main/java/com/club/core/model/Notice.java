package com.club.core.model;

import com.club.base.model.BaseModel;
import com.club.core.constant.TableLib;
import com.club.framework.valid.groups.Insert;
import com.club.framework.valid.groups.Update;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;

/**
 * 公告
 *
 * @author 阳春白雪 | sample@gmail.com
 * @model 公告
 * @date 2022年2月20日
 * @since 1.0
 */
@Table(name = Notice.TABLE_NAME)
public class Notice extends BaseModel implements Serializable {

    private static final long serialVersionUID = -378119039240349761L;

    public static final String TABLE_NAME = TableLib.CLUB + "NOTICE";
    public static final String FIELD_UUID = "uuid";
    public static final String FIELD_UPDATE_USER_ID = "updateUserId";

    /**
     * 公告id
     */
    @Id
    @NotBlank(groups = {Insert.class})
    private String uuid;

    /**
     * 公告内容
     * @required
     */
    @NotBlank(groups = {Insert.class, Update.class})
    @Size(max = 300,groups = {Insert.class, Update.class})
    private String descripter;

    /**
     * 1 发布,0 草稿，2 过期
     * @required
     */
    private int mode;

    /**
     * 创建时间
     */
    private Date createTime;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getDescripter() {
        return descripter;
    }

    public void setDescripter(String descripter) {
        this.descripter = descripter;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }
}
