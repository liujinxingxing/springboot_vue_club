package com.club.base.constant;

/**
 *  字符符号常量池
 * @author 阳春白雪 | sample@gmail.com
 * @since 1.0
 * @ignore
 * @date 2022年2月20日
 */
public interface StringPool {

    /**
     * 默认编码
     */
    String DEFAULT_ENCODING = "UTF-8";

    String GBK_ENCODING = "ISO-8859-1";

    String QUERY = "?";
    String AND = "&";
    String START = "*";
    String XML_ROOT_START = "<data>";
    String XML_ROOT_END = "</data>";

    String TEMPLATE_INDEX = "/template/%s";
    String EMPTY = "";
    String TOKEN_USER = "token-auth-user";
    String REQUEST_USER = "request-auth-user";
    String AUTH_INFO = "auth-info";
    String TOKEN_INVOKE_PARAM_NAME = "token_invoke";

    String PARAM_COOKIES_NAME = "cookies";
    String COOKIES_SPLIT = ";";
    String PERMISSIONSPLIT = ":";
    String URL_PREFIX = "path";
    String PATHSPLIT = "/";
    String EQ = "=";
    String METHOD_GET = "GET";
    String METHOD_POST = "POST";
    String SCHEME_HTTP = "http";
    String SCHEME_HTTPS = "https";
    String X_REAL_IP = "X-REAL-HOST-IP";
    String CONTENT_DISPOSITION = "Content-Disposition";
    String DOWNLOAD_HEAND = "attachment; filename=%s";

    String X_FORWARDED_FOR = "X-Forwarded-For";
    String CONTENT_TYPE = "Content-Type";
    String CNT_HTML_UTF8 = "text/html;charset=UTF-8";
    String CACHE_CONTROL = "Cache-Control";
    String CNT_FORM = "application/x-www-form-urlencoded";
    String CNT_FORM_DATA = "multipart/form-data";
    String CNT_JSON = "application/json";
    String CNT_XML = "application/xml";
    String CNT_HTML = "text/html";
    String CNT_BINARY = "application/octet-stream";
    String URL_INDEX = "index";

    /**
     * 冒号 “：”
     */
    String COLON = ":";
    /**
     * 空格
     */
    String SPACE = " ";
    /**
     * 空
     */
    String BLANK = "";

    /**
     * 下划线
     */
    String UNDERLINE = "_";

    String UPPERCHAR = "[A-Z]([a-z\\d]+)?";

    /**
     * 左括号
     */
    String LEFT_BRACKET = "(";
    /**
     * 右括号
     */
    String RIGHT_BRACKET = ")";

    /**
     * 左中括号
     */
    String LEFT_MIDDLE_BRACKET = "[";
    /**
     * 右括号
     */
    String RIGHT_MIDDLE_BRACKET = "]";

    /**
     * 左中括号
     */
    String LEFT_BIG_BRACKET = "{";
    /**
     * 右括号
     */
    String RIGHT_BIG_BRACKET = "}";

    /**
     * 逗号
     */
    String COMMA =  ",";

    /**
     * 点
     */
    String DOT = ".";

    /**
     * 单引号
     */
    String SINGLE_QUOTE = "'";
    /**
     * 双引号
     */
    String QUOTE = "\"";

    /**
     *
     * 24小时制时间格式
     *
     * yyyy-MM-dd HH:mm:ss
     */
    String PATTERN_TIME = "yyyy-MM-dd HH:mm:ss";
    /**
     * 日期格式<br>
     * yyyy-MM-dd
     */
    String PATTERN_DATE = "yyyy-MM-dd";
    /**
     * 日期格式<br>
     * yyyyMMdd
     */
    String PATTERN_DATE2 = "yyyyMMdd";

    /**
     * 常见日式格式
     */
    static final String[] PATTERNS = new String[] { "y-M-d", "yMd", "y/M/d", "y-M-d H:m:s", "yMd H:m:s",
            "y/M/d H:m:s", "y-M-d H:m:s.S", "yMd H:m:s.S", "y/M/d H:m:s.S", "y-M-d'T'H:m:s.S", "y-M-d'T'H:m:s.S'Z'" };

    /**
     * 排序顺序
     */
    String ORDER_ASC = "asc";
    /**
     * 排序逆序
     */
    String ORDER_DESC = "desc";

    String INDEX_ROOT_ID = "0";

    String FAKE = "fake";

}
