package com.jcl.aidemo.service;

import com.jcl.aidemo.bean.TextTemplate;
import com.jcl.aidemo.bean.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserService {
    public List<User> selectAll();
    public User getUserById(int id);
    public int addUser(User user);
    public int updateUser(User user);
    public int deleteUser(int id);
    public User loginPassword(User user);
    public User checkByPwd(String phone,String pwd);
    public User getUserByName(String userName);
    List<TextTemplate> getAllTemplate();
    List<TextTemplate> getAllAuditTemplate();
    int addTemplate(TextTemplate template);
    List<TextTemplate> getTextTemplatesByUserPhone(String id);
    boolean checkTemplateExist(int id);
    int updateTemplateById(TextTemplate template);
    User getUserByPhone(String phone_number);
    List<TextTemplate> searchTemplates(String title);
    List<TextTemplate> getTemplatesByUserId(@Param("userId") int userId);
    List<User> getUsersByTemplateId(@Param("templateId") int templateId);
    int addFavorite(@Param("userId") int userId, @Param("templateId") int templateId);
    int removeFavorite(@Param("userId") int userId, @Param("templateId") int templateId);
    int isTemplateFavorited(@Param("userId") int userId, @Param("templateId") int templateId);
    TextTemplate getTextTemplatesById(int id);
    List<TextTemplate> getTemplatesByManagerId(@Param("userId") int userId);
    int addManager(@Param("userId") int userId, @Param("templateId") int templateId);
    List<User> getAdministratorByTemplateId(@Param("templateId") int templateId);
}
