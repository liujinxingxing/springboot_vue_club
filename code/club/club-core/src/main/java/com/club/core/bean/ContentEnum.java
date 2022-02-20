package com.club.core.bean;

import com.club.base.util.StringUtil;

/**
 * ContentEnum的描述
 *
 * @author 阳春白雪 | sample@gmail.com
 * @ignore
 * @date 2022年2月20日
 * @since 1.0
 */
public enum  ContentEnum {
    COURT("court",1),
    APPA("appa",0)
    ;
    private String name;
    private int mode;

    public static ContentEnum of(String content){
        if(StringUtil.equalsIgnoreCase(APPA.getName(),content)){
            return APPA;
        }
        return COURT;
    }
    ContentEnum(String name, int mode) {
        this.name = name;
        this.mode = mode;
    }

    public String getName() {
        return name;
    }

    public int getMode() {
        return mode;
    }

}
