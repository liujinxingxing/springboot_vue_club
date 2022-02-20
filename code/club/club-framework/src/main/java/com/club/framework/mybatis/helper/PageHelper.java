package com.club.framework.mybatis.helper;

import com.club.base.bean.Page;

/**
 * 分页帮助简化
 * @author 阳春白雪 | sample@gmail.com
 * @since 1.0
 * @ignore
 * @date 2022年2月20日
 */
public class PageHelper {

    /**
     * 旧代码支持
     * @param isPage
     * @param page
     * @return
     */
    public static com.github.pagehelper.Page<Object> startPage(boolean isPage, Page page){
        if(isPage){
            return startPage(page);
        }
        return null;
    }

    public static com.github.pagehelper.Page<Object> startPage(Page page){
        if(page != null && page.isEnable()){
            if(page.isOffset()){
                /*
                 * 开始分页
                 * @param offset 页码
                 * @param limit  每页显示数量
                 * @param count  是否进行count查询
                 */
                return com.github.pagehelper.PageHelper.offsetPage(page.getPageNum(), page.getPageSize(), page.isCount());
            }
            /*
             * 开始分页
             * @param pageNum  页码
             * @param pageSize 每页显示数量
             * @param count    是否进行count查询
             */
            return com.github.pagehelper.PageHelper.startPage(page.getPageNum(), page.getPageSize(), page.isCount());
        }
        return null;
    }
}
