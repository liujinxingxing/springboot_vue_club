package com.club.core.controller;

import com.club.base.bean.Page;
import com.club.base.helper.ResultHelper;
import com.club.core.model.Discuss;
import com.club.core.model.Notice;
import com.club.core.service.DiscussService;
import com.club.core.service.NoticeService;
import com.club.framework.mybatis.helper.GridResultHelper;
import com.club.framework.spring.controller.BaseController;
import com.club.framework.valid.groups.Update;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 公告或者留言板信息的操作
 *
 * @author 阳春白雪 | sample@gmail.com
 * @action 留言板、公告
 * @date 2022年2月20日
 * @since 1.0
 */
@RestController
@RequestMapping("opt")
public class OptController extends BaseController {

    @Autowired
    protected NoticeService noticeService;

    @Autowired
    protected DiscussService discussService;

    /**
     * 保存公告
     *
     * @param notice |com.club.core.model.Notice|公告
     * @return com.club.core.model.Notice|公告
     * @summary 保存公告
     * @error 没有更新
     * @author 阳春白雪 | sample@gmail.com
     * @date 2022年2月20日
     * @since 1.0
     */
    @PostMapping("notice/save")
    public Object save(@RequestBody @Validated(Update.class) Notice notice) {
        return ResultHelper.success(noticeService.save(notice, getUser()));
    }

    /**
     * 获取公告信息列表
     *
     * @param keyword |String|关键搜索词
     * @param mode    |int|非必须|-1|类型 -1 全部，0 草稿，1 发布，2 过期
     * @param page    |int|非必须|1|分页当前页
     * @param rows    |int|非必须|10|分页行数
     * @param roll    |boolean|非必须|false|是否滚动分页
     * @param enable  |boolean|非必须|true|是否分页
     * @return com.club.core.model.Notice[]|公告信息
     * @summary 获取公告信息列表
     * @author 阳春白雪 | sample@gmail.com
     * @date 2022年2月20日
     * @since 1.0
     */
    @RequestMapping("notice/list")
    public Object list(String keyword, @RequestParam(name = "mode", defaultValue = "-1") int mode, Page page) {
        return GridResultHelper.grid(noticeService.list(keyword, mode, page, getUser()), page);
    }

    /**
     * 删除公告或者留言板信息
     *
     * @param ids  |String|公告ids
     * @param type |String|类型type ：notice公告类型，discuss留言板
     * @return int|更新条数
     * @summary 删除公告或者留言板信息
     * @author 阳春白雪 | sample@gmail.com
     * @date 2022年2月20日
     * @since 1.0
     */
    @RequestMapping(value = "{type}/delete")
    public Object delete(@RequestParam(value = "ids") List<String> ids, @PathVariable String type) {
        if ("notice".equals(type)) {
            return ResultHelper.success(noticeService.delete(ids, getUser()));
        }
        return ResultHelper.success(discussService.delete(ids, getUser()));
    }

    /**
     * 保存留言板
     *
     * @param discuss |com.club.core.model.Discuss|公告
     * @return com.club.core.model.Discuss|留言板
     * @summary 保存留言板
     * @error 没有更新
     * @author 阳春白雪 | sample@gmail.com
     * @date 2022年2月20日
     * @since 1.0
     */
    @PostMapping("discuss/save")
    public Object save2(@RequestBody @Validated(Update.class) Discuss discuss) {
        return ResultHelper.success(discussService.save(discuss, getUser()));
    }

    /**
     * 获取留言板信息列表
     *
     * @param keyword |String|关键搜索词
     * @param page    |int|非必须|1|分页当前页
     * @param rows    |int|非必须|10|分页行数
     * @param roll    |boolean|非必须|false|是否滚动分页
     * @param enable  |boolean|非必须|true|是否分页
     * @return com.club.core.model.Discuss[]|留言板信息
     * @summary 获取公告信息列表
     * @author 阳春白雪 | sample@gmail.com
     * @date 2022年2月20日
     * @since 1.0
     */
    @RequestMapping("discuss/list")
    public Object list2(String keyword, Page page) {
        return GridResultHelper.grid(discussService.list(keyword, page, getUser()), page);
    }

    /**
     * 留言板信息置顶/取消置顶
     *
     * @param ids  |String|留言板信息ids
     * @param mode |int|是否置顶 ：1 置顶，0 取消置顶
     * @return int|留言板信息
     * @summary 留言板信息置顶/取消置顶
     * @author 阳春白雪 | sample@gmail.com
     * @date 2022年2月20日
     * @since 1.0
     */
    @RequestMapping("discuss/top")
    public Object top(@RequestParam(value = "ids") List<String> ids,@RequestParam(name = "mode",defaultValue = "0") int mode) {
        return ResultHelper.success(discussService.top(ids, mode, getUser()));
    }

    /**
     * 公告发布/取消公告发布
     *
     * @param ids  |String|公告ids
     * @param mode |int|是否置顶 ：1 发布，0 取消发布
     * @return int|留言板信息
     * @summary 公告发布/取消公告发布
     * @author 阳春白雪 | sample@gmail.com
     * @date 2022年2月20日
     * @since 1.0
     */
    @RequestMapping("notice/push")
    public Object push(@RequestParam(value = "ids") List<String> ids,@RequestParam(name = "mode",defaultValue = "0") int mode) {
        return ResultHelper.success(noticeService.push(ids, mode, getUser()));
    }
}
