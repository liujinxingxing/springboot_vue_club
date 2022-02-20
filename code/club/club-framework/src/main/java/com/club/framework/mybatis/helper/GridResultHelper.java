package com.club.framework.mybatis.helper;

import com.club.base.bean.DataGrid;
import com.club.base.bean.Result;
import com.club.base.helper.ResultHelper;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * 分页返回
 * @author 阳春白雪 | sample@gmail.com
 * @since 1.0
 * @ignore
 * @date 2022年2月20日
 */
public class GridResultHelper extends ResultHelper {

    public static <T> Result<Object> grid(List<T> list) {
        return grid(list, false);
    }

    public static <T> Result<Object> grid(List<T> list, boolean roll) {
        PageInfo<T> pf = new PageInfo<T>(list);
        if(roll){
            Result result = new Result(list);
            result.setNext(pf.isHasNextPage());
            result.setTotal(pf.getTotal());
            return result;
        }
        DataGrid dg = new DataGrid<>(list, pf.getTotal());
        return new Result(dg);
    }

    public static <T> Result<Object> grid(List<T> list, com.club.base.bean.Page page) {
        return grid(list, page.isRoll());
    }
}
