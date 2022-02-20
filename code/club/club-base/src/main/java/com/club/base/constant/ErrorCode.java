package com.club.base.constant;

import com.club.base.bean.ResultMessage;

/**
 * 错误代码枚举类,门类太多了，要全局改改
 *
 * @author 阳春白雪 | sample@gmail.com
 * @ignore
 * @date 2022年2月20日
 * @since 1.0
 */
public enum ErrorCode implements ResultMessage {

    /**
     * 错误代码枚举, 500 运行系统错误，100 用户业务错误，200 消息， 300 数据库， 400 资源方面，600 数据校验，700
     */
    /**
     * 业务 100 用户业务错误
     */
    ACCOUNT_INCORRECT(100,"账号错误"),
    PASSWORD_INCORRECT(100,"密码错误"),
    VALID_CODE_INCORRECT(101, "验证码错误"),
    VALID_CODE_INVALID(102, "验证码失效"),
    ENCODE_INCORRECT(103,"用户信息编码错误"),

    REQ_PARAM_NOT_IS_JSON(115, "仅仅接受JSON参数"),

    REQ_PARAM_ERROR(116, "参数校验失败"),
    /**
     * 400 资源方面
     */
    ACCOUNT_ACCESS(403,"权限不足，请联系管理员升级账号权限"),
    NO_PERMISSION_TO_ACCESS(403,"无访问权限"),
    NO_FOUND_SERVICE(404, "没有此类资源！"),
    /**
     * 500 运行系统错误
     */
    URL_ERROR(500,"url格式有误！"),
    INVOKE_IS_NULL(501,"不存在此执行器！"),
    PARAM_ERROR(502,"业务参数错误！"),
    METHOD_NO_PERMISSION(503, "没有权限执行该执行器！"),
    METHOD_TARGET_ARGUMENT_ERROR(504, "目标执行器参数注入不正确！"),
    SERVER_ERROR(505, "服务器内部错误！"),
    UNDEFINED_ERROR(506, "未知错误"),
    QUERY_DATABASE_ERROR(507, "查询数据库错误"),
    PARAM_IS_NULL(508, "参数为空"),
    PARAM_ILLEGAL_FAIL(509, "找不到参数对应的文件名称"),
    NO_FOUND_FAIL(510, "上传文件为空或不能解析"),
    JSON_FORMAT_ERROR(511, "不能解析的json"),
    NO_DATA(512,"没有关联数据"),
    DATA_USE(514,"数据被引用中"),
    NO_UPDATE(513,"更新数据失败"),;

    private String message;

    private int code;

    private ErrorCode(int code,String message) {
        this.message = message;
        this.code = code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
