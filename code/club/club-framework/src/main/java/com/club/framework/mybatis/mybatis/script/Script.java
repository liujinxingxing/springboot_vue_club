package com.club.framework.mybatis.mybatis.script;

import javax.sql.DataSource;
import java.util.regex.Pattern;

/**
 * 脚本处理接口
 * @author 阳春白雪 | sample@gmail.com
 * @since 1.0
 * @ignore
 * @date 2022年2月20日
 */
public interface Script {

    /**
     * 执行脚本-接口方法
     * @param dataSource       - 数据源
     * @param versionTableName - 版本表名称
     * @param dialect          - 方言
     * @param directory        - 脚本根目录
     * @param files            - 脚本文件名称,多个用,分割
     * @author 阳春白雪 | sample@gmail.com
     * @date 2022年2月20日
     * @since 1.0
     */
    void runScript(DataSource dataSource, String versionTableName, String dialect, String directory, String files);

    String CREATE_TABLE = "CREATE TABLE %s(versional varchar(50),logger varchar(500),script varchar(1000))";

    String MAX_VERSION_TABLE = "select sequence_version from %s where sequence_version = (select max(sequence_version) from %s)";

    String SELECT_COUNT = "select count(1) from %s";

    String SELECT_VERSION = "";

    String GROUP_VERSION = "select t1.versional from %s t1 group by t1.versional";

    String INSERT_VERSION = "insert into %s (versional,logger) values(?,?)";

    String LINE_SEPARATOR = System.getProperty("line.separator", "\n");

    String DEFAULT_DELIMITER = ";";

    String MARK_START = "Version(";

    String MARK_END = ")";

    String MARK_SPLIT = ",";

    String MARK_EMPTY = "";

    String MARK_ALL_START = "/*";

    String MARK_ALL_END = "*/";

    String MARK_SINGLE_START = "--";

    String MARK_SINGLE__END = "\n";

    String MARK_R = "\r";
}
