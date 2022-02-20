package com.club.core.mapper;

import com.club.core.model.AuthUser;
import com.club.core.model.LoginLog;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * UserMapper的描述
 *
 * @author 阳春白雪 | sample@gmail.com
 * @ignore
 * @date 2022年2月20日
 * @since 1.0
 */
public interface UserMapper extends Mapper<AuthUser> {

    List<AuthUser> list(@Param("keyword") String keyword, @Param("userIds") List<String> userIds);

    int init(@Param("password") String password,@Param("userIds") List<String> userIds);

    List<LoginLog> listLog(@Param("keyword") String keyword,@Param("admin") boolean admin,@Param("account") String account);

}
