package com.club.core.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.sql.SQLException;

/**
 * IdGeneratorMapper的描述
 * @author 阳春白雪 | sample@gmail.com
 * @since 1.0
 * @ignore
 * @date 2022年2月20日
 */
public interface IdGeneratorMapper {

    @Select("select curval from BASE_GENERATOR_ID where name=#{name}")
    Integer getCurVal(@Param("name") String name);

    @Insert("insert into BASE_GENERATOR_ID(name,curval) values(#{name},#{curval})")
    int insertCurVal(@Param("name") String name, @Param("curval") Integer curval) throws SQLException;

    @Update("update BASE_GENERATOR_ID set curval=#{nextval} where name=#{name} and curval=#{curval}")
    int updateVal(@Param("name") String name, @Param("nextval") Integer nextval, @Param("curval") Integer curval);

}
