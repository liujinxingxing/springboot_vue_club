package com.club.base.util;

import com.club.base.constant.StringPool;
import org.apache.commons.lang.StringUtils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字符串操作
 * @author 阳春白雪 | sample@gmail.com
 * @since 1.0
 * @ignore
 * @date 2022年2月20日
 */
public class StringUtil extends StringUtils {

    private static SimpleDateFormat df = new SimpleDateFormat(StringPool.PATTERN_DATE);

    private static SimpleDateFormat df2 = new SimpleDateFormat(StringPool.PATTERN_TIME);

    /**
     * 利用java原生的摘要实现SHA256加密
     * @param str 加密后的报文
     * @return
     */
    public static String getSHA256StrJava(String str){
        MessageDigest messageDigest;
        String encodeStr = "";
        try {
            messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(str.getBytes("UTF-8"));
            encodeStr = byte2Hex(messageDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return encodeStr;
    }
    /**
     * 将byte转为16进制
     * @param bytes
     * @return
     */
    private static String byte2Hex(byte[] bytes){
        StringBuffer stringBuffer = new StringBuffer();
        String temp = null;
        for (int i=0;i<bytes.length;i++){
            temp = Integer.toHexString(bytes[i] & 0xFF);
            if (temp.length()==1){
                //1得到一位的进行补0操作
                stringBuffer.append("0");
            }
            stringBuffer.append(temp);
        }
        return stringBuffer.toString();
    }

    /**
     * 功能描述: 驼峰转换
     * @param underline
     * @return String - 返回值类型
     * @author 阳春白雪 | sample@gmail.com
     * @date 2022年2月20日
     */
    public static String getHump(String underline){
        underline = StringUtils.trim(underline);
        if (StringUtils.isEmpty(underline)) {
            return StringPool.EMPTY;
        }
        StringBuilder sb = new StringBuilder(underline);
        Matcher mc = Pattern.compile(StringPool.UNDERLINE).matcher(underline);
        for (int i = 0; mc.find();) {
            int position = mc.end() - (i++);
            try {
                sb.replace(position - 1, position + 1, sb.substring(position, position + 1).toUpperCase());
            } catch (Exception e) {
                sb.replace(position - 1, position, StringPool.EMPTY);
            }
        }
        return sb.toString();
    }

    /**
     * 功能描述: 驼峰转换下划线
     * @param hump
     * @return String - 返回值类型
     * @author 阳春白雪 | sample@gmail.com
     * @date 2022年2月20日
     */
    public static String getUnderline(String hump){
        hump = StringUtils.trim(hump);
        if (StringUtils.isEmpty(hump)) {
            return StringPool.EMPTY;
        }
        hump = String.valueOf(hump.charAt(0)).toUpperCase()
                .concat(hump.substring(1));
        StringBuilder sb = new StringBuilder();
        Matcher mc = Pattern.compile(StringPool.UPPERCHAR).matcher(hump);
        while (mc.find()) {
            sb.append(StringUtils.lowerCase(mc.group()));
            sb.append(mc.end() == hump.length() ? StringPool.EMPTY : StringPool.UNDERLINE);
        }
        return sb.toString();
    }

    public static String formatWithSecond(Object content) {
        if (content == null) {
            return "";
        }
        if (content instanceof Date) {
            return df2.format(content);
        }
        return content.toString();
    }

    /**
     * <pre>
     * format(String) = String
     * format(int) = String
     * format(BigDecimal) = String
     * format(Time||Date||Timestamp||TIMESTAMP) = String
     * </pre>
     *
     * @param content
     * @return
     */
    public static String format(Object content) {
        return format(content, false);
    }

    public static String format(Object content, boolean isSecond) {
        if (content == null) {
            return "";
        }
        if (content instanceof Date || content instanceof Timestamp) {
//            if (content instanceof TIMESTAMP) {
//                try {
//                    if (isSecond) {
//                        return df2.format(((TIMESTAMP) content).dateValue());
//                    } else {
//                        return df.format(((TIMESTAMP) content).dateValue());
//                    }
//                } catch (SQLException e) {
//                    logger.error(e.getMessage(), e);
//                }
//            }
            if (isSecond) {
                return df2.format(content);
            } else {
                return df.format(content);
            }
        }
        return content.toString();
    }

    public static Date parse(String content) {
        if (StringUtils.isNotEmpty(content)) {
            try {
                return df2.parse(content);
            } catch (ParseException e) {
                try {
                    return df.parse(content);
                } catch (ParseException e1) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    public static boolean isEmpty(Map<?, ?> map) {
        return map == null || map.isEmpty();
    }

    public static boolean isEmpty(List<?> list) {
        return list == null || list.isEmpty();
    }

    static final String PATTERN_DIGIT = "\\d+";

    public static boolean isNumber(String value) {
        return (value != null && value.matches(PATTERN_DIGIT));
    }

    /**
     * 格式化搜索字符串
     *
     * <pre>
     * StringUtil.keywords(null)          = null
     * StringUtil.keywords("")            = null
     * StringUtil.keywords("     ")       = null
     * StringUtil.keywords("abc")         = "%abc%"
     * StringUtil.keywords("    abc    ") = "%abc%"
     * StringUtil.keywords("  a bc  ")    = "%a%bc%"
     * </pre>
     *
     * @param keyword
     * @return
     */
    public static String keyword(String keyword){
        return startKeyword(endKeyword(keyword));
    }

    public static String startKeyword(String keyword){
        keyword = StringUtils.trimToNull(keyword);
        if(isNotEmpty(keyword)) {
            return "%" + keyword;
        }
        return  null;
    }

    public static String endKeyword(String keyword){
        keyword = StringUtils.trimToNull(keyword);
        if(isNotEmpty(keyword)){
            return keyword + "%";
        }
        return null;
    }


    /**
     * 比较目标与一堆“字符串”
     *
     * @param s1
     * @param objs
     * @return
     */
    public static boolean equalAll(Object s1, Object... objs) {
        if (s1 == null || objs.length < 1) {
            return false;
        }
        String source = (String.valueOf(s1)).trim().toLowerCase();
        for (Object o : objs) {
            String target = String.valueOf(o).trim().toLowerCase();
            if (!source.equals(target)) {
                return false;
            }
        }
        return true;
    }

    public static boolean contains(Object s1, Object s2) {
        if (s1 == null || s2 == null) {
            return false;
        }
        return (String.valueOf(s1)).trim().toLowerCase().contains((String.valueOf(s2)).trim().toLowerCase());
    }

    /**
     * 半角转全角
     *
     * @param input String.
     * @return 全角字符串.
     */
    public static String toSBC(String input) {
        char[] c = input.toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (c[i] == ' ') {
                c[i] = '\u3000';
            } else if (c[i] < '\177') {
                c[i] = (char) (c[i] + 65248);

            }
        }
        return new String(c);
    }

    /**
     * 全角转半角
     *
     * @param input String.
     * @return 半角字符串
     */
    public static String toDBC(String input) {

        char[] c = input.toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (c[i] == '\u3000') {
                c[i] = ' ';
            } else if (c[i] > '\uFF00' && c[i] < '\uFF5F') {
                c[i] = (char) (c[i] - 65248);

            }
        }
        return new String(c);
    }

    public static String camelToUnderline(String param) {
        if (param == null || "".equals(param.trim())) {
            return "";
        }
        int len = param.length();
        StringBuilder sb = new StringBuilder(len);

        char first = param.charAt(0);
        sb.append(first);
        for (int i = 1; i < len; i++) {
            char c = param.charAt(i);
            if (Character.isUpperCase(c)) {
                sb.append(StringPool.UNDERLINE);
                sb.append(Character.toLowerCase(c));
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    public static String underlineToCamel(String param) {
        if (param == null || "".equals(param.trim())) {
            return "";
        }
        int len = param.length();
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            char c = param.charAt(i);
            if (StringPool.UNDERLINE.equals(c)) {
                if (++i < len) {
                    sb.append(Character.toUpperCase(param.charAt(i)));
                }
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    public static String underlineToCamel2(String param) {
        if (param == null || "".equals(param.trim())) {
            return "";
        }
        StringBuilder sb = new StringBuilder(param);
        Matcher mc = Pattern.compile(StringPool.UNDERLINE).matcher(param);
        int i = 0;
        while (mc.find()) {
            int position = mc.end() - (i++);
            sb.replace(position - 1, position + 1, sb.substring(position, position + 1).toUpperCase());
        }
        return sb.toString();
    }


    /**
     * 根据分隔符将list转换成String
     *
     * @param values
     * @param splitSymbol
     * @return
     */
    public static String listToString(Collection<String> values, String splitSymbol) {
        StringBuilder stringBuilder = new StringBuilder();
        for (String value : values) {
            stringBuilder.append(value);
            stringBuilder.append(splitSymbol);
        }
        return StringUtils.stripEnd(stringBuilder.toString(), splitSymbol);
    }

    /**
     * 判断业务数据字段是否为空
     *
     * @param value
     * @return
     */
    public static boolean bizDataIsEmpty(String value) {
        return value == null || value.length() == 0 || "-1".equals(value);
    }

    public static String substrLengthOfUTF8(String value, int length) {
        int valueLength = 0;
        if (value == null) {
            return value;
        }
        String chinese = "[\u0391-\uFFE5]";
        /* 获取字段值的长度，如果含中文字符，则每个中文字符长度为3，否则为1 */
        for (int i = 0; i < value.length(); i++) {
            /* 获取一个字符 */
            String temp = value.substring(i, i + 1);
            /* 判断是否为中文字符 */
            if (temp.matches(chinese)) {
                /* 中文字符长度为3 */
                valueLength += 3;
            } else {
                /* 其他字符长度为1 */
                valueLength += 1;
            }
            if (valueLength > length) {
                return value.substring(0, i);
            }
        }
        return value;
    }

    /**
     * @param target  - 目标字符串
     * @param start   - 起始位置字符串
     * @param end     - 结束位置字符串
     * @param replace - 替换的新字符
     * @return java.lang.String - 替换后的字符
     *  替换字符
     *
     * @author 阳春白雪 | sample@gmail.com
     * @date 2022年2月20日
     * @since 1.0
     */
    public static String replaceStartToEnd(String target, String start, String end, String replace) {
        StringBuilder tmp = new StringBuilder(target);
        int cursor = 0;
        while (true) {
            int firstIndex = tmp.indexOf(start, cursor);
            int secondIndex = tmp.indexOf(end, cursor);
            if (firstIndex == -1 || secondIndex == -1) {
                break;
            }
            if (firstIndex < secondIndex) {
                cursor = firstIndex;
                tmp.replace(firstIndex, secondIndex + end.length(), replace);
                continue;
            }
            cursor = secondIndex + 1;
        }
        return tmp.toString();
    }

    /**
     * @param target - 目标字符串
     * @param start  - 起始位置字符串
     * @param end    - 结束位置字符串
     * @return java.lang.String - 值
     *  替换字符
     *
     * @author 阳春白雪 | sample@gmail.com
     * @date 2022年2月20日
     * @since 1.0
     */
    public static String valueStartToEnd(String target, String start, String end) {
        StringBuilder tmp = new StringBuilder(target);
        int cursor = 0;
        int firstIndex = tmp.indexOf(start, cursor);
        int secondIndex = tmp.indexOf(end, cursor);
        if (firstIndex != -1 && secondIndex != -1) {
            return tmp.substring(firstIndex + start.length(), secondIndex);
        }
        return target;
    }
}
