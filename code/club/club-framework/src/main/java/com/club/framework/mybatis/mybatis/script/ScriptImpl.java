package com.club.framework.mybatis.mybatis.script;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;

import javax.sql.DataSource;
import java.io.*;
import java.net.JarURLConnection;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * 脚本处理实现类
 * @author 阳春白雪 | sample@gmail.com
 * @since 1.0
 * @ignore
 * @date 2022年2月20日
 */
@Slf4j
public class ScriptImpl implements Script, Runnable {

    private Connection conn = null;
    private PreparedStatement ps = null;
    private Statement st = null;
    private ResultSet rs = null;

    private String versionTableName;
    private String dialect;
    private String directory;
    private String files;

    /**
     * 初始化方法
     * @param dataSource       - 数据源
     * @param versionTableName - 版本表的表名
     * @param dialect          - 方言
     * @param directory        - sql存放根目录
     * @param files            - sql文件用,分隔
     * @author 阳春白雪 | sample@gmail.com
     * @date 2022年2月20日
     * @since 1.0
     */
    public void init(DataSource dataSource, String versionTableName, String dialect, String directory, String files) {
        this.versionTableName = versionTableName;
        this.dialect = dialect;
        this.directory = directory;
        this.files = files;
        try {
            conn = dataSource.getConnection();
        } catch (SQLException e) {
            log.error("创建连接错误：{}", e);
        }
    }

    /**
     * 创建版本记录表,只有创建成功才往下执行
     * @return boolean - 是否错在版本表或者是否创建成功
     * @author 阳春白雪 | sample@gmail.com
     * @date 2022年2月20日
     * @since 1.0
     */
    public boolean createVersionTable() {
        boolean ret = true;
        try {
            String sql = String.format(SELECT_COUNT, versionTableName);
            log.debug("校验版本记录表是否存在 sql:{}", sql);
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            log.debug("版本记录表已存在，跳过创建版本记录表");
        } catch (SQLException e) {
            log.warn("版本记录表不存在，将创建版本记录表");
            closeResource();
            try {
                log.info("创建版本记录表,tableName:{}, sql:{}", versionTableName, String.format(CREATE_TABLE, versionTableName));
                ps = conn.prepareStatement(String.format(CREATE_TABLE, versionTableName));
                ps.executeUpdate();
            } catch (SQLException e1) {
                log.error("创建版本记录表错误:{}", e1);
                ret = false;
            }
        } finally {
            closeResource();
        }
        return ret;
    }

    /**
     * 执行脚本
     * @author 阳春白雪 | sample@gmail.com
     * @date 2022年2月20日
     * @since 1.0
     */
    public void runScript() {
        //创建版本记录表
        if (createVersionTable()) {
            List<Version> versions = loadScripts();
            if (versions != null && versions.size() > 0) {
                try {
                    ps = conn.prepareStatement(String.format(GROUP_VERSION, versionTableName));
                    rs = ps.executeQuery();
                    List<String> result = new ArrayList<String>();
                    while (rs.next()) {
                        result.add(rs.getString(1));
                    }
                    closeResource();
                    ps = conn.prepareStatement(String.format(INSERT_VERSION, versionTableName));

                    st = conn.createStatement();
                    st.setEscapeProcessing(true);
                    //设置手动提交
                    conn.setAutoCommit(false);
                    for (Version version : versions) {
                        if (version.isNoAvail() || result.contains(version.getVersion())) {
                            continue;
                        }
                        String script = version.getScript();
                        result.add(version.getVersion());
                        log.debug("正在执行SQL脚本-:{}【{}】", LINE_SEPARATOR, script);
                        String sql = "";
                        try {
                            String[] scripts = script.split(DEFAULT_DELIMITER);
                            for (String s : scripts) {
                                // 预处理脚本
                                sql = replaceStartToEnd(replaceStartToEnd(s, MARK_ALL_START, MARK_ALL_END, MARK_EMPTY), MARK_SINGLE_START, MARK_SINGLE__END, MARK_EMPTY);
                                sql = sql.replaceAll(MARK_SINGLE__END, MARK_EMPTY);
                                sql = sql.replaceAll(MARK_R, MARK_EMPTY);
                                sql = sql.trim();
                                if (MARK_EMPTY.equals(sql)) {
                                    continue;
                                }
                                sql = String.format("%s%s%s", LINE_SEPARATOR, sql, LINE_SEPARATOR);
                                st.execute(sql);
                                //TODO: 2019-11-22 好像DDL 不支持回滚-daysh
                                /*
                                    DDL （Data Definition Language 数据定义语言）
                                    create table 创建表
                                    alter table  修改表
                                    drop table 删除表
                                    truncate table 删除表中所有行
                                    create index 创建索引
                                    drop index  删除索引
                                    当执行DDL语句时，在每一条语句前后，oracle都将提交当前的事务。如果用户使用insert命令将记录插入到数据库后，执行了一条DDL语句(如create table)，此时来自insert命令的数据将被提交到数据库。当DDL语句执行完成时，DDL语句会被自动提交，不能回滚。
                                    DML （Data Manipulation Language 数据操作语言）
                                    insert 将记录插入到数据库
                                    update 修改数据库的记录
                                    delete 删除数据库的记录
                                    当执行DML命令如果没有提交，将不会被其他会话看到。除非在DML命令之后执行了DDL命令或DCL命令，或用户退出会话，或终止实例，此时系统会自动发出commit命令，使未提交的DML命令提交。
                                 */
                                // printResults(st, st.execute(sql));
                            }
                            // 将版本记录插入数据库
                            int index = 1;
                            ps.setString(index++, version.getVersion());
                            ps.setString(index++, version.getLogger());
                            // ps.setString(index++, script);
                            ps.executeUpdate();
                            conn.commit();
                        } catch (SQLException e1) {
                            log.error("执行脚本错误，version:【{}】,logger:【{}】,sql:【{}】", version.getVersion(), version.getLogger(), sql, e1);
                            conn.rollback();
                        }
                    }
                    conn.setAutoCommit(true);
                } catch (SQLException e1) {
                    log.error("执行脚本出错:{}", e1);
                } finally {
                    closeResource();
                }
            }
        }
        //关闭资源
        close();
    }

    /**
     * 替换字符
     * @param target  - 目标字符串
     * @param start   - 起始位置字符串
     * @param end     - 结束位置字符串
     * @param replace - 替换的新字符
     * @return java.lang.String - 替换后的字符
     * @author 阳春白雪 | sample@gmail.com
     * @date 2022年2月20日
     * @since 1.0
     */
    private String replaceStartToEnd(String target, String start, String end, String replace) {
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

//    private String printResults(Statement statement, boolean hasResults) {
//        StringBuffer buf = new StringBuffer();
//        try {
//            if (hasResults) {
//                ResultSet rs = statement.getResultSet();
//                if (rs != null) {
//                    ResultSetMetaData md = rs.getMetaData();
//                    int cols = md.getColumnCount();
//                    int i;
//                    String value;
//                    for(i = 0; i < cols; ++i) {
//                        value = md.getColumnLabel(i + 1);
//                        buf.append(value + "\t");
//                    }
//                    buf.append("");
//                    while(rs.next()) {
//                        for(i = 0; i < cols; ++i) {
//                            value = rs.getString(i + 1);
//                            buf.append(value + "\t");
//                        }
//                        buf.append("");
//                    }
//                }
//            }
//        } catch (SQLException var8) {
//            log.error("Error printing results: {}", var8.getMessage());
//        }
//        return buf.toString();
//    }

    /**
     * 加载sql文件,并分块处理
     * @return java.util.List<com.daysh.base.conf.mybatis.script.ScriptImpl.Version> - 返回所有块的sql脚本
     * @author 阳春白雪 | sample@gmail.com
     * @date 2022年2月20日
     * @since 1.0
     */
    private List<Version> loadScripts() {
        // TODO: 2020-02-05 包不会递归加载 
        List<Version> result = new ArrayList<>();
        if (files != null && files.length() > 0) {
            String[] file = files.split(MARK_SPLIT);
            for (String name : file) {
                // TODO: 2020-02-20 webapp 这里有问题
                String filename = String.format("%s/%s/%s", directory, dialect, name);
                log.debug("filename:{}",filename);
                Enumeration<URL> resources = null;
                try {
                    resources = getClass().getClassLoader().getResources(filename);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (resources != null) {
                    while (resources.hasMoreElements()) {
                        URL url = resources.nextElement();
                        String protocol = url.getProtocol();
                        if ("file".equalsIgnoreCase(protocol)) {
                            try {
                                readSQL(new FileInputStream(new File(url.getFile())),result);
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            }
                        } else if ("jar".equalsIgnoreCase(protocol)) {
                            JarFile jares = null;
                            try {
                                jares = ((JarURLConnection) url.openConnection()).getJarFile();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            if (jares != null) {
                                Enumeration<JarEntry> entries = jares.entries();
                                while (entries.hasMoreElements()) {
                                    JarEntry jarEntry = entries.nextElement();
                                    if (jarEntry != null) {
                                        String className = jarEntry.getName();
                                        if (className.startsWith(filename)) {
                                            try {
                                                readSQL(jares.getInputStream(jarEntry),result);
                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        /**
         * InputStream inputStream = StatisticCodeLines.class.getClassLoader().getResourceAsStream("StringUtils.java");
         */
        return result;
    }

    protected void readSQL(InputStream in,List<Version> result){
        if(in == null){
            return;
        }
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(in));
            StringBuffer buf = null;
            for (String line = ""; (line = reader.readLine()) != null; ) {
                if (line.indexOf(MARK_START) != -1) {
                    result.add(getVersion(buf));
                    buf = new StringBuffer();
                }
                if (buf != null && !MARK_EMPTY.equals(line)) {
                    buf.append(line).append(LINE_SEPARATOR);
                }
            }
            //处理最后一个代码块
            result.add(getVersion(buf));
        } catch (Exception e) {
            log.info("脚本处理错误：{}", e);
        }finally {
            IOUtils.closeQuietly(in);
            IOUtils.closeQuietly(reader);
        }
    }

    /**
     * 资源关闭
     * @author 阳春白雪 | sample@gmail.com
     * @date 2022年2月20日
     * @since 1.0
     */
    private void closeResource() {
        try {
            if (rs != null) {
                rs.close();
            }
            if (ps != null) {
                ps.close();
            }
            if (st != null) {
                st.close();
            }
        } catch (SQLException e) {
            log.error("资源关闭错误:{}", e);
        }
    }

    /**
     * 关闭连接
     * @author 阳春白雪 | sample@gmail.com
     * @date 2022年2月20日
     * @since 1.0
     */
    public void close() {

        try {
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException e) {
            log.error("资源关闭错误:{}", e);
        }
    }

    /**
     * 开线程执行的脚本
     * @author 阳春白雪 | sample@gmail.com
     * @date 2022年2月20日
     * @since 1.0
     */
    @Override
    public void run() {
        Thread.currentThread().setName("script");
        runScript();
    }

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
    @Override
    public void runScript(DataSource dataSource, String versionTableName, String dialect, String directory, String files) {
        //初始化处理
        init(dataSource, versionTableName, dialect, directory, files);
        run();
//        new Thread(this).start();
    }

    /**
     * 脚本解析器
     * @param buf - 字符串
     * @return com.daysh.base.conf.mybatis.script.ScriptImpl.Version - 一个版本块的sql脚本包装类
     * @author 阳春白雪 | sample@gmail.com
     * @date 2022年2月20日
     * @since 1.0
     */
    private Version getVersion(StringBuffer buf) {
        if (buf == null) {
            return new Version(true);
        }
        int index = buf.indexOf(MARK_START);
        if (index == -1) {
            return new Version(true);
        }
        int lastIndex = buf.indexOf(MARK_END, index);
        if (lastIndex == -1) {
            return new Version(true);
        }
        String mark = buf.substring(index + MARK_START.length(), lastIndex);
        if (MARK_EMPTY.equals(mark)) {
            return new Version(true);
        }
        String[] args = mark.split(MARK_SPLIT, 2);
        if (args.length == 2) {
            return new Version(args[0], args[1], buf.toString());
        }
        return new Version(mark, MARK_EMPTY, buf.toString());
    }

    /**
     * 版本内部类,包装sql脚本块
     * @author 阳春白雪 | sample@gmail.com
     * @since 1.0
     * @ignore
     * @date 2022年2月20日
     */
    class Version {

        /**
         * 版本id
         */
        private String version;

        /**
         * 版本日志
         */
        private String logger;

        /**
         * 该版本的脚本
         */
        private String script;

        /**
         * 无效的版本代码块
         */
        private boolean noAvail = false;

        public Version() {
        }

        public Version(boolean noAvail) {
            this.noAvail = noAvail;
        }

        public Version(String version) {
            this.version = version;
        }

        public Version(String version, String logger) {
            this.version = version;
            this.logger = logger;
        }

        public Version(String version, String logger, String script) {
            this.version = version;
            this.logger = logger;
            this.script = script;
        }

        public String getVersion() {
            return version;
        }

        public String getLogger() {
            return logger;
        }

        public boolean isNoAvail() {
            return noAvail;
        }

        public void setNoAvail(boolean noAvail) {
            this.noAvail = noAvail;
        }

        public String getScript() {
            return script;
        }

        public void setVersion(String version) {
            this.version = version;
        }

        public void setLogger(String logger) {
            this.logger = logger;
        }

        public void setScript(String script) {
            this.script = script;
        }
    }
}
