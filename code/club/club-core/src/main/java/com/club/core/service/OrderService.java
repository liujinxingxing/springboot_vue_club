package com.club.core.service;

import com.club.base.bean.Page;
import com.club.base.bean.User;
import com.club.base.constant.ErrorCode;
import com.club.base.exception.FrameworkException;
import com.club.base.util.StringUtil;
import com.club.core.mapper.OrderMapper;
import com.club.core.model.Content;
import com.club.core.model.Order;
import com.club.core.model.OrderHistory;
import com.club.core.vo.OrderResult;
import com.club.core.vo.OrderVo;
import com.club.framework.cache.CacheManager;
import com.club.framework.mybatis.helper.PageHelper;
import com.club.framework.mybatis.service.AbstractService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * OrderService的描述
 *
 * @author 阳春白雪 | sample@gmail.com
 * @ignore
 * @date 2022年2月20日
 * @since 1.0
 */
@Slf4j
@Service
public class OrderService extends AbstractService<Order> {

    private static final String CAR_CACHE = "CAR_CACHE";
    private static final int TIMEOUT = 200;
    @Value("${base.subscribe.timeout:1}")
    private int timeout;

    @Autowired
    protected OrderMapper orderMapper;

    @Autowired
    protected OrderHistoryService orderHistoryService;

    public boolean exsist(String code, int mode) {
        Example example = new Example(Order.class);
        example.createCriteria().andEqualTo(Content.FIELD_CODE, code).andEqualTo(Content.FIELD_MODE, mode);
        return orderMapper.selectCountByExample(example) > 0;
    }

    protected void clear(User user) {
        Set<String> cachKeys = CacheManager.getCachKeys(user.getSessionId());
        if (cachKeys != null) {
            for (String cachKey : cachKeys) {
                List<Content> list = CacheManager.get(user.getSessionId(), cachKey, List.class);
                if (list != null) {
                    for (Content content : list) {
                        CacheManager.evict(CAR_CACHE, String.format("%s.%d", content.getCode(), content.getMode()));
                    }
                }
                CacheManager.evict(user.getSessionId(), cachKey);
            }
        }
    }

    public boolean clear(List<String> codes, String uuid,String prefixId, User user) {
        Example example = new Example(Order.class);
        example.createCriteria().andIn(Content.FIELD_CODE, codes).andEqualTo(Content.FIELD_MODE, 0).andEqualTo(Order.FIELD_UUID, uuid);
        int i = orderMapper.deleteByExample(example);
        if(i > 0){
            clear(codes, prefixId, 0, user);
            return true;
        }
        return false;
    }

    public boolean clear(List<String> codes, String prefixId, int mode, User user) {
        if(mode == 1){
            clear(user);
            return true;
        }
        List<Content> list = CacheManager.get(user.getSessionId(), prefixId, List.class);
        if (list != null) {
            for (Content content : list) {
                if(0 == content.getMode() && codes.contains(content.getCode())){
                    CacheManager.evict(CAR_CACHE, String.format("%s.%d", content.getCode(), content.getMode()));
                    continue;
                }
                CacheManager.expire(CAR_CACHE, String.format("%s.%d", content.getCode(), content.getMode()), TIMEOUT, TimeUnit.SECONDS);
            }
            CacheManager.put(user.getSessionId(), prefixId, list, TIMEOUT, TimeUnit.SECONDS);
            return true;
        }
        throw new FrameworkException(ErrorCode.NO_DATA);
    }

    public String prefix(List<OrderResult> list,User user) {
        if(CollectionUtils.isNotEmpty(list)){
            String prefixId = UUID.randomUUID().toString();

            List<Content> arr = new ArrayList<>();
            for (OrderResult ret : list) {
                arr.add(new Content(ret.getCode(), ret.getMode()));
            }
            for (Content content : arr) {
                CacheManager.put(CAR_CACHE, String.format("%s.%d", content.getCode(), content.getMode()),"1", TIMEOUT, TimeUnit.SECONDS);
            }
            CacheManager.put(user.getSessionId(), prefixId, arr, TIMEOUT, TimeUnit.SECONDS);
            return prefixId;
        }
        throw new FrameworkException(ErrorCode.NO_DATA);
    }

    public String prefix(String code, User user) {
        // 清除过期的
        clear(user);
        String id = CacheManager.get(CAR_CACHE, String.format("%s.1", code), String.class);
        if (id == null) {
            // 查数据看是否被引用
            if (exsist(code, 1)) {
                throw new FrameworkException(ErrorCode.DATA_USE);
            }
            String prefixId = UUID.randomUUID().toString();
            Content content = new Content(code, 1);
            List<Content> list = new ArrayList<>();
            list.add(content);
            CacheManager.put(CAR_CACHE, String.format("%s.1", code), "1", TIMEOUT, TimeUnit.SECONDS);
            CacheManager.put(user.getSessionId(), prefixId, list, TIMEOUT, TimeUnit.SECONDS);
            return prefixId;
        }
        throw new FrameworkException(ErrorCode.DATA_USE);
    }

    public List<String> prefix(List<String> codes, String prefixId, User user) {
        List<String> ret = new ArrayList<>();
        List<Content> list = CacheManager.get(user.getSessionId(), prefixId, List.class);
        if (CollectionUtils.isNotEmpty(codes) && CollectionUtils.isNotEmpty(list)) {
            for (String code : codes) {
                String id = CacheManager.get(CAR_CACHE, String.format("%s.0", code), String.class);
                if (id == null) {
                    // 校验数据库
                    if (exsist(code, 0)) {
                        continue;
                    }
                    ret.add(code);
                }
            }

            for (String code : ret) {
                CacheManager.put(CAR_CACHE, String.format("%s.0", code), "1");
                list.add(new Content(code, 0));
            }
            for (Content content : list) {
                CacheManager.expire(CAR_CACHE, String.format("%s.%d", content.getCode(), content.getMode()), TIMEOUT, TimeUnit.SECONDS);
            }
            CacheManager.put(user.getSessionId(), prefixId, list, TIMEOUT, TimeUnit.SECONDS);
        }
        return ret;
    }

    @Transactional
    public int lend(OrderVo vo, User user) {
        List<Content> list = CacheManager.get(user.getSessionId(), vo.getPrefixId(), List.class);
        if (list != null) {
            String uuid = vo.getUuid();
            if(StringUtil.isEmpty(uuid)){
                uuid = IdGenerator.generateId(Order.class.getName());
            }else{
                Example example = new Example(Order.class);
                example.createCriteria().andEqualTo(Order.FIELD_UUID, uuid);
                orderMapper.deleteByExample(example);
            }
            Date date = new Date();
            int ret = 0;
            for (Content content : list) {
                Order order = new Order(uuid, content.getCode(), content.getMode());
                order.setCreateTime(date);
                order.setStartTime(vo.getEntry()==0?vo.getStartTime():new Date());
                order.setContact(vo.getContact());
                order.setEntry(vo.getEntry());
                order.setUseInfo(vo.getUseInfo());
                ret += insertSelective(order, user);
            }
            clear(user);
            return ret;
        }
        throw new FrameworkException(ErrorCode.NO_DATA);
    }

    public List<OrderHistory> listHis(String uuid, String contact, User user) {
        return orderMapper.listHis(uuid, contact, user.getAccount(), user.isAdmin());
    }

    public int deleteHis(List<String> ids, User user) {
        return orderMapper.deleteHis(ids);
    }

    public List<OrderHistory> listHisSingle(String keyword, Page page, User user) {
        PageHelper.startPage(page);
        return orderMapper.listHisSingle(StringUtil.keyword(keyword));
    }

    public List<OrderResult> listSingle(String keyword, Page page, User user) {
        PageHelper.startPage(page);
        return orderMapper.listSingle(StringUtil.keyword(keyword));
    }

    public List<OrderResult> list(String uuid, String contact, Integer entry, User user) {
        return orderMapper.list(uuid, contact, user.getAccount(), user.isAdmin(), entry);
    }

    @Transactional
    public int back(String uuid,String descripter, User user) {
        Date date = new Date();
        int ret = 0;
        List<OrderResult> list = list(uuid, null, 1, user);
        for (OrderResult order : list) {
            OrderHistory history = new OrderHistory();
            BeanUtils.copyProperties(order, history);
            history.setFinishTime(date);
            history.setPrice(order.getAllPrice());
            history.setResult(true);
            if(order.getMode() == 1){
                history.setDescripter(descripter);
            }
            ret += deleteByPrimaryKey(order);
            ret += orderHistoryService.insertSelective(history, user);
        }
        return ret;
    }

    @Transactional
    public int unsubscribe(String uuid, User user) {
        Example example = new Example(Order.class);
        Example.Criteria criteria = example.createCriteria().andEqualTo(Order.FIELD_UUID, uuid);
        if (!user.isAdmin()) {
            criteria.andEqualTo(Order.FIELD_USER, user.getAccount());
        }
        return orderMapper.deleteByExample(example);
    }

    @Scheduled(cron="0 */30 * * * ?")
    public void scan(){
        log.debug("取消预约扫描:超时时间{}小时",timeout);
        long timeout = 60 * 60 * 1000 * this.timeout;
        List<Order> subscribe = orderMapper.getSubscribe();
        if(CollectionUtils.isNotEmpty(subscribe)){
            for (Order order : subscribe) {
                boolean flag = false;
                Date startTime = order.getStartTime();
                if(startTime != null){
                    long end = startTime.getTime();
                    long now = System.currentTimeMillis();
                    if(now > end){
                        flag = true;
                    }
                    long time = end - now;
                    if(time < timeout){
                        flag = true;
                    }
                }else{
                    flag = true;
                }
                if(flag){
                    Example example = new Example(Order.class);
                    example.createCriteria().andEqualTo(Order.FIELD_UUID, order.getUuid()).andEqualTo(Order.FIELD_ENTRY,order.getEntry());
                    orderMapper.deleteByExample(example);
                }

            }
        }
    }

    public int subscribe(String prefixId, Date startTime, User user) {
        OrderVo vo = new OrderVo();
        vo.setPrefixId(prefixId);
        vo.setStartTime(startTime);
        vo.setUseInfo(user.getUserName());
        vo.setContact(user.getAccount());
        vo.setEntry(0);
        return lend(vo, user);
    }

}
