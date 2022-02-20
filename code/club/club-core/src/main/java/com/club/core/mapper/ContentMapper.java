package com.club.core.mapper;

import com.club.core.model.Content;
import com.club.core.vo.ContentVo;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * ContentMapper的描述
 *
 * @author 阳春白雪 | sample@gmail.com
 * @ignore
 * @date 2022年2月20日
 * @since 1.0
 */
public interface ContentMapper extends Mapper<Content> {

    List<ContentVo> list(@Param("keyword") String keyword, @Param("mode") int mode, @Param("flag") boolean flag, @Param("codes") List<String> codes);

}
