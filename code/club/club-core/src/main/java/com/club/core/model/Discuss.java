package com.club.core.model;

import com.club.core.constant.TableLib;
import com.club.framework.valid.groups.Insert;
import com.club.framework.valid.groups.Update;

import javax.persistence.Table;
import javax.validation.constraints.Size;

/**
 * 留言板
 * @model 留言板
 * @author 阳春白雪 | sample@gmail.com
 * @since 1.0
 * @date 2022年2月20日
 */
@Table(name = Discuss.TABLE_NAME)
public class Discuss extends Notice {

    public static final String TABLE_NAME = TableLib.CLUB + "DISCUSS";

    /**
     * 匿名名称
     */
    @Size(max = 30, groups = {Insert.class, Update.class})
    private String nickname;
    /**
     * 评分
     */
    private int score;

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    /**
     * 1 置顶,0 普通
     * @required
     */
    @Override
    public int getMode() {
        return super.getMode();
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
