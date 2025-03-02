package com.jcl.aidemo.mapper;

import com.jcl.aidemo.bean.TextTemplate;
import com.jcl.aidemo.bean.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface UserMapper {
    @Select({
            "select",
            "id, userName, password, phone_number, created_at, updated_at, status",
            "from user"
    })
    List<User> selectAllUser();

    //根据id查询用户
    @Select("select * from user where id = #{id}")
    User getUserById(int id);

    //插入用户
    @Insert("insert into user (id, userName, password, phone_number, created_at, updated_at, status) " +
            "values(#{id}, #{userName}, #{password}, #{phone_number}, #{created_at}, #{updated_at}, #{status} )")
    int addUser(User user);

    //更新用户
    @Update("update user set userName=#{userName}, password=#{password}, phone_number=#{phone_number}, created_at=#{created_at}, updated_at=#{updated_at}, status=#{status} where id = #{id}")
    int updateUser(User user);

    //删除用户
    @Delete("delete from user where id = #{id}")
    int deleteUser(int id);

    @Select({
            "select",
            "id, userName, password, phone_number, created_at, updated_at, status",
            "from user",
            "where userName = #{userName} and password = #{password}"
    })
    User loginPassword(User user);

    //根据id查询用户
    @Select("select * from user where userName = #{userName}")
    User getUserByName(String userName);

    @Select("select * from texttemplate")
    List<TextTemplate> getTextTemplates();
}
