package com.club.framework.valid.vo;

import java.io.Serializable;

/**
 * FieldErrorVo的包装类描述
 * @model 校验错误信息
 * @author 阳春白雪 | sample@gmail.com
 * @since 1.0
 * @date 2022年2月20日
 */
public class FieldErrorVo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 字段名
     */
    private String field;

    /**
     * 校验规则
     */
    private String rule;

    /**
     * 非法值
     */
    private Object rejectedValue;

    public FieldErrorVo() {

    }

    public FieldErrorVo(String field, String rule, Object rejectedValue) {
        this.field = field;
        this.rule = rule;
        this.rejectedValue = rejectedValue;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getRule() {
        return rule;
    }

    public void setRule(String rule) {
        this.rule = rule;
    }

    public Object getRejectedValue() {
        return rejectedValue;
    }

    public void setRejectedValue(Object rejectedValue) {
        this.rejectedValue = rejectedValue;
    }

}
