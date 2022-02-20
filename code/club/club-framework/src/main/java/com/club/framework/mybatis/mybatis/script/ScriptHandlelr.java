package com.club.framework.mybatis.mybatis.script;

import com.club.framework.spring.execute.ExecuteMethon;
import com.club.framework.spring.execute.Invoker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

/**
 * sql脚本处理器
 * @author 阳春白雪 | sample@gmail.com
 * @since 1.0
 * @ignore
 * @date 2022年2月20日
 */
@Component
@Slf4j
public class ScriptHandlelr {

    @Value("${base.db.execute:false}")
    private boolean execute;


    /**
     * 版本记录表名称
     */
    @Value("${base.db.versionTableName:TB_VERSIONAL}")
    private String versionTableName;

    /**
     * 方言
     */
    @Value("${base.db.dialect:mysql}")
    private String dialect;

    /**
     * 脚本根目录
     */
    @Value("${base.db.directory:db}")
    private String directory;

    /**
     * 脚本文件名称中间用,分割
     */
    @Value("${base.db.files:}")
    private String files;

    /**
     * 实现类全名
     */
    @Value("${base.db.classImpl:com.club.framework.mybatis.mybatis.script.ScriptImpl}")
    private String classImpl;

    /**
     * 数据源
     */
    @Autowired
    private DataSource dataSource;

    /**
     * 事件重写，spring加载完成后执行
     * @author 阳春白雪 | sample@gmail.com
     * @date 2022年2月20日
     * @since 1.0
     */
    @ExecuteMethon(level = Invoker.FIRSTLY)
    public void onApplicationEvent() {
        if(!execute){
            return;
        }
        log.debug("自定义SQL脚本自动执行校验器");
        try {
            Script script = (Script) Class.forName(classImpl).newInstance();
            //执行sql脚本入口
            script.runScript(dataSource, versionTableName, dialect, directory, files);
        } catch (InstantiationException e) {
            log.error("创建脚本解析器错误:{}", classImpl);
        } catch (IllegalAccessException e) {
            log.error("脚本解析器错误:{}", classImpl);
        } catch (ClassNotFoundException e) {
            log.error("不存在脚本解析器:{}", classImpl);
        }
    }

}
