package com.club.core.mapper;

import com.club.core.model.Order;
import com.club.core.model.OrderHistory;
import com.club.core.vo.OrderResult;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.Date;
import java.util.List;

/**
 * OrderMapper的描述
 *
 * @author 阳春白雪 | sample@gmail.com
 * @ignore
 * @date 2022年2月20日
 * @since 1.0
 */
public interface OrderMapper extends Mapper<Order> {

//    int active(@Param("uuid") String uuid,@Param("account") String account,@Param("userName") String userName,@Param("now") Date now);

    List<OrderResult> list(@Param("uuid") String uuid,@Param("contact")  String contact,@Param("account")  String account,@Param("admin") boolean admin,@Param("entry")Integer entry);

    List<OrderHistory> listHis(@Param("uuid") String uuid,@Param("contact")  String contact,@Param("account")  String account,@Param("admin") boolean admin);

    List<OrderHistory> listHisSingle(@Param("keyword") String keyword);

    List<OrderResult> listSingle(@Param("keyword") String keyword);

    int deleteHis(@Param("uuids") List<String> uuids);

    List<Order> getSubscribe();
}
