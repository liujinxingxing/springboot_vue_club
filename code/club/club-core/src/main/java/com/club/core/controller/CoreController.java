package com.club.core.controller;

import com.club.base.bean.Page;
import com.club.base.constant.ErrorCode;
import com.club.base.helper.ResultHelper;
import com.club.core.bean.ContentEnum;
import com.club.core.model.Content;
import com.club.core.service.ContentService;
import com.club.framework.mybatis.helper.GridResultHelper;
import com.club.framework.spring.controller.BaseController;
import com.club.framework.valid.groups.Insert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 场地或者器材处理分组，处理场地和器材的所有操作
 * @action 场地或者器材处理分组
 * @author 阳春白雪 | sample@gmail.com
 * @since 1.0
 * @date 2022年2月20日
 */
@RestController
public class CoreController extends BaseController {

    @Autowired
    protected ContentService contentService;

    /**
     * 保存场地,器材信息 type = court 保存场地, type = appa 保存器材
     * @param content |com.club.core.model.Content|场地,器材信息
     * @param type |String|更新类型  court 场地,appa 器材
     * @summary 保存场地,器材信息
     * @error 没有更新
     * @return com.club.core.model.Content|场地,器材信息
     * @author 阳春白雪 | sample@gmail.com
     * @since 1.0
     * @date 2022年2月20日
     */
    @PostMapping("{type}/save")
    public Object save(@RequestBody @Validated(Insert.class) Content content, @PathVariable String type) {
        return ResultHelper.success(contentService.save(content,ContentEnum.of(type), getUser()));
    }

    /**
     * 场地,器材编号是否存在 ， 用于校验编号是否存在和重复 type = court 保存场地, type = appa 保存器材
     * @param code |String|编号
     * @param type |String|更新类型  court 场地,appa 器材
     * @summary 场地,器材编号是否存在
     * @error 没有更新
     * @return boolean|更新条数
     * @author 阳春白雪 | sample@gmail.com
     * @since 1.0
     * @date 2022年2月20日
     */
    @PostMapping("{type}/exsist")
    public Object exsist(@RequestParam String code,@PathVariable String type) {
        return ResultHelper.success(contentService.exsist(code,ContentEnum.of(type)));
    }

    /**
     * 获取场地,器材信息列表 type = court 保存场地, type = appa 保存器材
     * @param type |String|内容  court 场地,appa 器材
     * @param flag |String|是否全部数据  true 查看全部 ,false 查看闲置
     * @param keyword |String|关键搜索词
     * @param page |int|非必须|1|分页当前页
     * @param rows |int|非必须|10|分页行数
     * @param roll |boolean|非必须|false|是否滚动分页
     * @param enable |boolean|非必须|true|是否分页
     * @summary 获取信息列表
     * @return com.club.core.vo.ContentVo|场地,器材信息
     * @author 阳春白雪 | sample@gmail.com
     * @since 1.0
     * @date 2022年2月20日
     */
    @RequestMapping("{type}/{flag}/list")
    public Object list(String keyword, Page page,@PathVariable String type,@PathVariable boolean flag){
        return GridResultHelper.grid(contentService.list(keyword, page,ContentEnum.of(type),flag, getUser()),page);
    }

    /**
     * 删除场地,器材信息  type = court 删除场地, type = appa 删除器材
     * @param ids |String|场地,器材编码
     * @param type |String|更新类型  court 场地,appa 器材
     * @summary 删除信息
     * @return int|更新条数
     * @author 阳春白雪 | sample@gmail.com
     * @since 1.0
     * @date 2022年2月20日
     */
    @RequestMapping(value = "{type}/delete")
    public Object delete(@RequestParam(value = "ids") List<String> ids,@PathVariable String type){
        return ResultHelper.success(contentService.delete(ids,ContentEnum.of(type), getUser()));
    }

    /**
     * 导出场地,器材信息  type = court 导出场地, type = appa 导出器材
     * @param ids |String|编码，参数为空时获取全部
     * @param type |String|更新类型  court 场地,appa 器材
     * @param keyword |String|关键字搜索
     * @summary 导出场地,器材信息
     * @error 错误描述
     * @return void|场地,器材信息,文件流
     * @author 阳春白雪 | sample@gmail.com
     * @since 1.0
     * @date 2022年2月20日
     */
    @RequestMapping("{type}/export")
    public Object exportContent(@RequestParam(value = "ids",required = false) List<String> ids,String keyword,@PathVariable String type) {
        if(contentService.exportContent(request, response, ids, keyword,ContentEnum.of(type), getUser())){
            return null;
        }
        return ResultHelper.fail(ErrorCode.NO_DATA);
    }

    /**
     * 导入场地,器材数据  type = court 导入场地, type = appa 导入器材
     * @param contents |com.club.core.model.Content[]|场地,器材信息
     * @param type |String|更新类型  court 场地,appa 器材
     * @summary 导入数据
     * @error 错误描述
     * @return int|导入数量
     * @author 阳春白雪 | sample@gmail.com
     * @since 1.0
     * @date 2022年2月20日
     */
    @RequestMapping(value = "{type}/import" ,method = RequestMethod.POST)
    public Object importContent(@RequestBody List<Content> contents,@PathVariable String type){
        return ResultHelper.success(contentService.saveListContent(contents,ContentEnum.of(type), getUser()));
    }

}
