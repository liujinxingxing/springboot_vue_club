package com.club.base.util;

import com.club.base.constant.StringPool;
import org.apache.commons.lang.ArrayUtils;

/**
 * 分页工具类
 * @author 阳春白雪 | sample@gmail.com
 * @since 1.0
 * @ignore
 * @date 2022年2月20日
 */
public class PageUtil {

    /**
     * 获取开始位置
     * @param pageNum - 第几页
     * @param pageSize - 分页大小
     * @return int - 开始位置
     * @author 阳春白雪 | sample@gmail.com
     * @date 2022年2月20日
     * @since 1.0
     */
    public static int getStart(int pageNum, int pageSize) {
        return pageSize * (pageNum - 1);
    }

    /**
     * 获取结束位置
     * @param pageNum - 第几页
     * @param pageSize - 分页大小
     * @return int - 结束位置
     * @author 阳春白雪 | sample@gmail.com
     * @date 2022年2月20日
     * @since 1.0
     */
    public static int getEnd(int pageNum, int pageSize) {
        return pageSize * pageNum;
    }

    /**
     * 通过对比实体类返回排序语句
     *
     * <pre>
     * getOrderCause("p1,p2","asc,desc")   = "p1 asc,p2 desc"
     * getOrderCause("p1,p2","asc")        = "p1 asc,p2"
     * getOrderCause("","")                = ""
     * getOrderCause(null,null)            = ""
     * </pre>
     *
     * 日后将修改为通过反射获取实体映射到数据库的字段名称，当前实现是直接把字段从驼峰转换为下划线形式
     *
     * @param sort - 排序
     * @param order - 排序方式
     * @param entityClass - 实体类
     * @return java.lang.String -
     * @author 阳春白雪 | sample@gmail.com
     * @date 2022年2月20日
     * @since 1.0
     */
    public static String getOrderCause(String sort, String order, Class<?> entityClass) {
        String[] attrs = StringUtil.split(sort, StringPool.COMMA);
        String[] orders = StringUtil.split(order, StringPool.COMMA);
        StringBuilder sb = new StringBuilder();
        if (ArrayUtils.isNotEmpty(attrs)) {
            for (int i = 0; i < attrs.length; i++) {
                sb.append(StringUtil.camelToUnderline(attrs[i]));
                sb.append(StringPool.SPACE);
                if (orders != null && i < orders.length) {
                    // 如果是逆序，则添加逆序
                    if (StringUtil.equalsIgnoreCase(orders[i], StringPool.ORDER_DESC)) {
                        sb.append(StringPool.ORDER_DESC);
                    }
                }
                sb.append(StringPool.COMMA);
            }
            if (sb.length() > 0) {
                sb.deleteCharAt(sb.length() - 1);
            }
        }
        return sb.toString();
    }

}

