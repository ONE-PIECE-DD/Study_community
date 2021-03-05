package com.onepiece.community.community.mapper;

import com.onepiece.community.community.model.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

//实现与数据库相连接的操作
@Component
@Mapper
public interface UserMapper {
    //该写法来自官网
    @Insert("insert into user (name,account_id,token,gmt_create,gmt_modified,avatar_url) values (#{name},#{accountId},#{token},#{gmtCreate},#{gmtModified},#{avatarUrl})")
    void insert(User user);

    @Select("select * from user where token = #{token}")
    User findByToken(@Param("token") String token);

    //关于#{}写法：如果形参是类，则会从类当中取出对应的变量；如果不是类，则需要通过@Param("")来标注
}
