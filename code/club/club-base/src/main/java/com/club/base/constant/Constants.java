package com.club.base.constant;

 /**
  * 基本常量
  * @author 阳春白雪 | sample@gmail.com
  * @since 1.0
  * @ignore
  * @date 2022年2月20日
  */
public final class Constants {

    private Constants() {
    }

     public static final String AUTH_CACHE_KEY = "AUTH_CACHE_KEY";

    public static final String DOT_SEPARATOR = ".";

    public static final String COMMA_SEPARATOR = ",";

    public static final String PATH_SEPARATOR = "/";

    public static final String ARROW_SYMBOL = ">";

    public static final String ESCAPE_CHAR = "\\";

    public static final String ORDER_BY_DESC = "desc";

    public static final String ORDER_BY_ASC = "asc";

    public static final String TIME_FORMAT_PATTERN_RANGE_MONTH = "yyyy-MM";
    
    /**
     * 操作标识：新增
     */
    public static final String DEAL_TYPE_SAVE = "save";
    
    /**
     * 操作标识：删除
     */
    public static final String DEAL_TYPE_DELETE = "delete";
    
    /**
     * JSON結果返回对象operaterResult属性success
     */
    public static final String OPERATER_RESULT_SUCCESS = "success";
    
    /**
     * JSON結果返回对象operaterResult属性data
     */
    public static final String OPERATER_RESULT_DATA = "data";
    
    /**
     * JSON結果返回对象operaterResult属性code
     */
    public static final String OPERATER_RESULT_CODE= "code";
    
    /**
     * 根目录标识
     */
    public static final String INDEX_ROOT_ID = "0";
    
    /**
     * mysql数据库
     */
    public static final String MYSQL_DIALECT = "mysql";
    
    /**
     * oracle数据库
     */
    public static final String ORACLE_DIALECT = "oracle";

    /**
     * oracle数据库
     */
    public static final String IMPALA_DIALECT = "impala";

    /**
     * oracle数据库
     */
    public static final String HIVE_DIALECT = "hive";
    
    /**
     * vertica数据库
     */
    public static final String VERTICA_DIALECT = "vertica";

    /**
     * inceptor 星环
     */
	public static final String INCEPTOR_DIALECT = "inceptor";
	
	/**
	 * 分区前缀
	 */
	public static final String PARTITION_PREFIX = "t_list";

	public static final String TABLE_PREFIX = "AUTOD_";
}
