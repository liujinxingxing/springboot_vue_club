package com.club.core.mapper;

import com.club.core.model.Notice;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * NoticeMapper的描述
 *
 * @author 阳春白雪 | sample@gmail.com
 * @ignore
 * @date 2022年2月20日
 * @since 1.0
 */
public interface NoticeMapper extends Mapper<Notice> {

    List<Notice> list(@Param("keyword") String keyword,@Param("mode") int mode);

}
