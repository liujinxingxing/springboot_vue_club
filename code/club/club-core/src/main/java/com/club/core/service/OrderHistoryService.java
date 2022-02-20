package com.club.core.service;

import com.club.core.mapper.OrderHistoryMapper;
import com.club.core.model.OrderHistory;
import com.club.framework.mybatis.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * OrderHistoryService的描述
 *
 * @author 阳春白雪 | sample@gmail.com
 * @ignore
 * @date 2022年2月20日
 * @since 1.0
 */
@Service
public class OrderHistoryService extends AbstractService<OrderHistory> {

    @Autowired
    protected OrderHistoryMapper orderHistoryMapper;
}
