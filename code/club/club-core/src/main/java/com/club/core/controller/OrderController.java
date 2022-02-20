package com.club.core.controller;

import com.club.base.bean.Page;
import com.club.base.bean.Result;
import com.club.base.bean.User;
import com.club.base.helper.ResultHelper;
import com.club.core.service.OrderService;
import com.club.core.vo.OrderResult;
import com.club.core.vo.OrderVo;
import com.club.framework.mybatis.helper.GridResultHelper;
import com.club.framework.spring.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

/**
 * 订单处理全部接口分组 ， 处理订单相关的所有操作
 *
 * @author 阳春白雪 | sample@gmail.com
 * @action 订单分组
 * @date 2022年2月20日
 * @since 1.0
 */
@RestController
@RequestMapping("order")
public class OrderController extends BaseController {

    @Autowired
    protected OrderService orderService;

    /**
     * 场地订单前置处理(相当于加入购物车) 将场地加入后端购物车中
     *
     * @param code     |String|场地编号
     * @return String|prefixId 预处理id 购物车ID
     * @summary 场地订单前置处理
     * @error 在别人的购物车中，已被别人引用，超时时间为3分钟
     * @author 阳春白雪 | sample@gmail.com
     * @date 2022年2月20日
     * @since 1.0
     */
    @RequestMapping(value = "court/prefix")
    public Object prefix(@RequestParam String code) {
        return ResultHelper.success(orderService.prefix(code, getUser()));
    }

    /**
     * 器材订单前置处理(相当于加入购物车) 将器材加入后端购物车中
     *
     * @param codes     |String|器材编号
     * @param prefixId |String|前置处理id 购物车ID
     * @return String[]|codes 没有被引用的器材编码
     * @summary 器材订单前置处理
     * @error 在别人的购物车中，已被别人引用，超时时间为3分钟
     * @author 阳春白雪 | sample@gmail.com
     * @date 2022年2月20日
     * @since 1.0
     */
    @RequestMapping(value = "appa/prefix")
    public Object prefix2(@RequestParam List<String> codes, @RequestParam String prefixId) {
        return ResultHelper.success(orderService.prefix(codes, prefixId, getUser()));
    }

    /**
     * 移除订单中的物品(购物车) 将场地或者器材从后端购物车中移除
     *
     * @param codes     |String|场地,器材编号
     * @param mode |int|类型 0 器材 1 场地
     * @param prefixId |String|前置处理id 购物车ID
     * @return boolean|成功移除
     * @summary 移除购物车中的物品
     * @error 无数据
     * @author 阳春白雪 | sample@gmail.com
     * @date 2022年2月20日
     * @since 1.0
     */
    @RequestMapping(value = "clear/prefix")
    public Object clear(@RequestParam List<String> codes, @RequestParam String prefixId, @RequestParam(name = "mode",defaultValue = "0") int mode) {
        return ResultHelper.success(orderService.clear(codes, prefixId,mode, getUser()));
    }

    /**
     * 移除订单中的器材 (购物车 移除数据库，一般情况是预定的情况)
     *
     * @param codes     |String|器材编号
     * @param uuid |String|预约订单id
     * @param prefixId |String|前置处理id 购物车ID
     * @return boolean|成功移除
     * @summary 移除购物车中的物品
     * @error 无数据
     * @author 阳春白雪 | sample@gmail.com
     * @date 2022年2月20日
     * @since 1.0
     */
    @RequestMapping(value = "db/prefix")
    public Object clear2(@RequestParam List<String> codes, @RequestParam String uuid, @RequestParam String prefixId) {
        return ResultHelper.success(orderService.clear(codes, uuid,prefixId, getUser()));
    }

    /**
     * 场地器材出借、或者现场预约 (从后端购物车获取实际需求的产品)
     *
     * @param vo |com.club.core.vo.OrderVo|订单
     * @return int|借用记录条数
     * @summary 场地器材出借
     * @error 没有关联数据
     * @author 阳春白雪 | sample@gmail.com
     * @date 2022年2月20日
     * @since 1.0
     */
    @RequestMapping(value = "lend")
    public Object lend(@RequestBody @Validated OrderVo vo) {
        return ResultHelper.success(orderService.lend(vo, getUser()));
    }

    /**
     * 获取订单内容列表，,参数全空时 查询登录用户的所有订单
     *
     * @param uuid |String|订单id 不为空时优先查询该订单
     * @param contact |String|客户联系方式，不为空时次优先查询该客户
     * @param detail |boolean|为true 且 entry = 0 时，将数据加入购物车 （一般情况是获取预约单详情）
     * @param entry |int|类型 0 预约， 1 订单
     * @return com.club.core.vo.OrderResult[]|订单列表
     * @summary 获取订单内容列表
     * @error 没有关联数据
     * @author 阳春白雪 | sample@gmail.com
     * @date 2022年2月20日
     * @since 1.0
     */
    @RequestMapping(value = "list")
    public Object list(String uuid,String contact,Integer entry,@RequestParam(name="detail",defaultValue = "false") boolean detail) {
        User user = getUser();
        List<OrderResult> list = orderService.list(uuid, contact, entry, user);
        Result<Object> ret = ResultHelper.success(list);
        if(detail && entry != null && entry.intValue() == 0){
            ret.setInfo(orderService.prefix(list,user));
        }
        return ret;
    }

    /**
     * 获取订单历史内容列表，,参数全空时 查询登录用户的所有订单
     *
     * @param uuid |String|订单id 不为空时优先查询该订单
     * @param contact |String|客户联系方式，不为空时次优先查询该客户
     * @return com.club.core.model.OrderHistory[]|订单历史列表
     * @summary 获取订单历史内容列表
     * @error 没有关联数据
     * @author 阳春白雪 | sample@gmail.com
     * @date 2022年2月20日
     * @since 1.0
     */
    @RequestMapping(value = "his/list")
    public Object listHis(String uuid,String contact) {
        return ResultHelper.success(orderService.listHis(uuid, contact, getUser()));
    }

    /**
     * 获取订单列表
     *
     * @param keyword |String|关键搜索词
     * @param page |int|非必须|1|分页当前页
     * @param rows |int|非必须|10|分页行数
     * @param roll |boolean|非必须|false|是否滚动分页
     * @param enable |boolean|非必须|true|是否分页
     * @return com.club.core.vo.OrderResult[]|订单列表
     * @summary 获取订单内容列表
     * @error 没有关联数据
     * @author 阳春白雪 | sample@gmail.com
     * @date 2022年2月20日
     * @since 1.0
     */
    @RequestMapping(value = "single/list")
    public Object listSingle(String keyword, Page page) {
        return GridResultHelper.grid(orderService.listSingle(keyword, page, getUser()),page);
    }

    /**
     * 删除历史订单
     * @param ids |String|订单ids
     * @summary 删除历史订单
     * @return int|更新条数
     * @author 阳春白雪 | sample@gmail.com
     * @since 1.0
     * @date 2022年2月20日
     */
    @RequestMapping(value = "his/delete")
    public Object delete(@RequestParam(value = "ids") List<String> ids){
        return ResultHelper.success(orderService.deleteHis(ids, getUser()));
    }

    /**
     * 获取订单历史列表
     *
     * @param keyword |String|关键搜索词
     * @param page |int|非必须|1|分页当前页
     * @param rows |int|非必须|10|分页行数
     * @param roll |boolean|非必须|false|是否滚动分页
     * @param enable |boolean|非必须|true|是否分页
     * @return com.club.core.model.OrderHistory[]|订单历史列表
     * @summary 获取订单历史内容列表
     * @error 没有关联数据
     * @author 阳春白雪 | sample@gmail.com
     * @date 2022年2月20日
     * @since 1.0
     */
    @RequestMapping(value = "his/single/list")
    public Object listHisSingle(String keyword, Page page) {
        return GridResultHelper.grid(orderService.listHisSingle(keyword, page, getUser()),page);
    }

    /**
     * 结算账单
     *
     * @param uuid |String|订单id
     * @param descripter |String|备注
     * @return int|修改记录数
     * @summary 结算
     * @error 没有关联数据
     * @author 阳春白雪 | sample@gmail.com
     * @date 2022年2月20日
     * @since 1.0
     */
    @RequestMapping(value = "back")
    public Object back(@RequestParam String uuid,String descripter) {
        return ResultHelper.success(orderService.back(uuid, descripter, getUser()));
    }

    /**
     * 场地器材预约
     *
     * @param prefixId |String|订单前置处理id 购物车id
     * @return int|借用记录条数
     * @summary 场地器材预约
     * @error 没有关联数据
     * @author 阳春白雪 | sample@gmail.com
     * @date 2022年2月20日
     * @since 1.0
     */
    @RequestMapping(value = "subscribe")
    public Object subscribe(@RequestParam String prefixId) {
        return ResultHelper.success(orderService.subscribe(prefixId, new Date(), getUser()));
    }

    /**
     * 取消预约
     *
     * @param uuid |String|订单id
     * @return int|修改记录数
     * @summary 取消预约
     * @error 没有关联数据
     * @author 阳春白雪 | sample@gmail.com
     * @date 2022年2月20日
     * @since 1.0
     */
    @RequestMapping(value = "unsubscribe")
    public Object unsubscribe(@RequestParam String uuid) {
        return ResultHelper.success(orderService.unsubscribe(uuid, getUser()));
    }

}
