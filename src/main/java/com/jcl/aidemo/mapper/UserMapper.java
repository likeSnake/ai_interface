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

    //根据手机号查询用户
    @Select("select * from user where phone_number = #{phoneNumber}")
    User getUserByPhone(String phoneNumber);

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
            "where phone_number = #{phone_number} and password = #{password}"
    })
    User loginPassword(User user);

    @Select({
            "select",
            "id, userName, password, phone_number, created_at, updated_at, status",
            "from user",
            "where phone_number = #{phone} and password = #{pwd}"
    })
    User checkByPwd(String phone,String pwd);

    //根据用户名查询用户
    @Select("select * from user where userName = #{userName}")
    User getUserByName(String userName);

    // 查询使用次数前30位的公开模板
    @Select("SELECT * FROM texttemplate WHERE permissionLevel = 0 ORDER BY useNumber DESC LIMIT 30")
    List<TextTemplate> getTextTemplates();

    // 根据分享者ID查询模板
    @Select("SELECT * FROM texttemplate WHERE sharerId = #{id} ORDER BY useNumber DESC")
    List<TextTemplate> getTextTemplatesByUserId(String id);

    // 添加模板
    @Insert("insert into TextTemplate (id, title, content, prompt, sharerId, useNumber,permissionLevel) " +
            "values(#{id}, #{title}, #{content}, #{prompt}, #{sharerId}, #{useNumber}, #{permissionLevel} )")
    int addTemplate(TextTemplate template);

    // 根据ID查询模板是否已存在
    @Select("SELECT CASE WHEN COUNT(*) > 0 THEN true ELSE false END FROM texttemplate WHERE id = #{id}")
    boolean checkTemplateExist(int id);

    // 更新模板
    @Update("UPDATE TextTemplate SET " +
            "title = #{title}, " +
            "content = #{content}, " +
            "prompt = #{prompt}, " +
            "sharerId = #{sharerId}, " +
            "useNumber = #{useNumber} ," +
            "permissionLevel = #{permissionLevel} " +
            "WHERE id = #{id}")
    int updateTemplateById(TextTemplate template);
}
