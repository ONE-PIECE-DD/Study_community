package com.onepiece.community.community.mapper;

import com.onepiece.community.community.model.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

//实现与数据库相连接的操作
@Component
@Mapper
public interface UserMapper {
    //该写法来自官网
    @Insert("insert into user (name,account_id,token,gmt_create,gmt_modified) values (#{name},#{accountId},#{token},#{gmtCreate},#{gmtModified})")
    void insert(User user);
}
