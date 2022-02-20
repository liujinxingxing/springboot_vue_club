package com.club.core.mapper;

import com.club.core.model.Discuss;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * DiscussMapper的描述
 *
 * @author 阳春白雪 | sample@gmail.com
 * @ignore
 * @date 2022年2月20日
 * @since 1.0
 */
public interface DiscussMapper extends Mapper<Discuss> {

    List<Discuss> list(@Param("keyword") String keyword);

}
