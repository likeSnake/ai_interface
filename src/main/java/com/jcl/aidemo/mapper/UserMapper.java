package com.jcl.aidemo.mapper;

import com.jcl.aidemo.bean.TextTemplate;
import com.jcl.aidemo.bean.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface UserMapper {
    @Select({
            "select * from user"
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
            "select * from user",
            "where phone_number = #{phone_number} and password = #{password}"
    })
    User loginPassword(User user);

    @Select({
            "select * from user",
            "where phone_number = #{phone} and password = #{pwd}"
    })
    User checkByPwd(String phone,String pwd);

    //根据用户名查询用户
    @Select("select * from user where userName = #{userName}")
    User getUserByName(String userName);

    // 查询使用次数前30位的公开模板
    @Select("SELECT * FROM texttemplate WHERE permissionLevel = 0 ORDER BY useNumber DESC LIMIT 30")
    List<TextTemplate> getTextTemplates();
    // 查询审核中的模板
    @Select("SELECT * FROM texttemplate WHERE permissionLevel = 2 ORDER BY useNumber DESC")
    List<TextTemplate> getAllAuditTemplate();

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
    // 根据模板ID查询模板
    @Select("SELECT * FROM texttemplate WHERE id = #{id}")
    TextTemplate getTextTemplatesById(int id);

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

    // 根据title模糊查询
    @Select("SELECT * FROM texttemplate WHERE title LIKE CONCAT('%', #{title}, '%') AND permissionLevel = 0 ORDER BY useNumber DESC")
    List<TextTemplate> searchTemplates(String title);

    /**************收藏模板的增删改查操作***************/
    // 查询：用户收藏的模板列表
    @Select("SELECT t.* FROM texttemplate t " +
            "JOIN user_template_favorite f ON t.id = f.template_id " +
            "WHERE f.user_id = #{userId}")
    List<TextTemplate> getTemplatesByUserId(@Param("userId") int userId);

    // 查询：某模板被哪些用户收藏
    @Select("SELECT u.* FROM user u " +
            "JOIN user_template_favorite f ON u.id = f.user_id " +
            "WHERE f.template_id = #{templateId}")
    List<User> getUsersByTemplateId(@Param("templateId") int templateId);

    // 插入：用户收藏模板
    @Insert("INSERT INTO user_template_favorite (user_id, template_id) VALUES (#{userId}, #{templateId})")
    int addFavorite(@Param("userId") int userId, @Param("templateId") int templateId);

    // 删除：用户取消收藏模板
    @Delete("DELETE FROM user_template_favorite WHERE user_id = #{userId} AND template_id = #{templateId}")
    int removeFavorite(@Param("userId") int userId, @Param("templateId") int templateId);

    // 查询是否收藏：返回数量
    @Select("SELECT COUNT(*) FROM user_template_favorite WHERE user_id = #{userId} AND template_id = #{templateId}")
    int isTemplateFavorited(@Param("userId") int userId, @Param("templateId") int templateId);

    /***********************操作结束*******************************/

    /**************管理员对模板表的操作***************/

    // 查询：对应管理员id审核的模板
    @Select("SELECT t.* FROM texttemplate t " +
            "JOIN user_template_manager f ON t.id = f.template_id " +
            "WHERE f.user_id = #{userId}")
    List<TextTemplate> getTemplatesByManagerId(@Param("userId") int userId);

    // 插入：管理员审核记录
    @Insert("INSERT INTO user_template_manager (user_id, template_id) VALUES (#{userId}, #{templateId})")
    int addManager(@Param("userId") int userId, @Param("templateId") int templateId);

    // 查询：某模板被哪些管理员操作过
    @Select("SELECT u.* FROM user u " +
            "JOIN user_template_manager f ON u.id = f.user_id " +
            "WHERE f.template_id = #{templateId}")
    List<User> getAdministratorByTemplateId(@Param("templateId") int templateId);
    /***********************操作结束*******************************/
}
