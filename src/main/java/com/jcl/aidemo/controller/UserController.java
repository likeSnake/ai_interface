package com.jcl.aidemo.controller;


import com.jcl.aidemo.Util.MyUtil;
import com.jcl.aidemo.Util.RandomStringGenerator;
import com.jcl.aidemo.bean.*;
import com.jcl.aidemo.service.UserService;
import com.jcl.aidemo.impl.UserServiceImpl;
import io.jsonwebtoken.Claims;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService = new UserServiceImpl();

    @RequestMapping(value = "/selectAll", method = RequestMethod.GET)
    public List<User> selectAll() {
        List<User> list = userService.selectAll();
        return list;
    }

    @RequestMapping(value = "/getUserById", method = RequestMethod.POST)
    public User getUserById(int id){
        User userById = userService.getUserById(id);
        return userById;
    }

    @RequestMapping(value = "/addUser", method = RequestMethod.POST)
    public BaseEntity<Integer> addUser(@RequestBody User user){
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();//获得当前时间
        String birthday = df.format(date);
        BaseEntity<Integer> baseEntity = new BaseEntity<>();

        user.setCreated_at(birthday);
        user.setUpdated_at(birthday);
        user.setStatus(0);

        User userByPhone = userService.getUserByPhone(user.getPhone_number());
        if (userByPhone == null){
            int code = userService.addUser(user);
            if (code == 1){
                baseEntity.setCode(1);
                baseEntity.setMsg("注册成功");
            }else {
                baseEntity.setCode(-1);
                baseEntity.setMsg("注册失败");
            }
        }else {
            baseEntity.setCode(-1);
            baseEntity.setMsg("该手机号已被注册");
        }

        return baseEntity;
    }

    @RequestMapping(value = "/updateUser", method = RequestMethod.POST)
    public int updateUser(@RequestBody User user){
        int code = userService.updateUser(user);
        return code;
    }

    @RequestMapping(value = "/updatePwd", method = RequestMethod.POST)
    public BaseEntity<Integer> updatePwd(@RequestBody SignInInfo signInInfo){
        signInInfo.getCheckType();
        BaseEntity<Integer> baseEntity = new BaseEntity<>();

        User user = userService.checkByPwd(signInInfo.getPhone(), signInInfo.getPassword());
        if (user != null){
            user.setPassword(signInInfo.getNewPassword());
            int code = userService.updateUser(user);
            baseEntity.setCode(1);
            baseEntity.setMsg("密码修改成功");
        }else {
            // 原密码验证失败
            baseEntity.setCode(2);
            baseEntity.setMsg("原密码错误，请重新输入");
        }
        return baseEntity;
    }

    @RequestMapping(value = "/deleteUser", method = RequestMethod.POST)
    public int deleteUser(int id){
        int code = userService.deleteUser(id);
        return code;
    }

    @RequestMapping(value = "/loginPassword", method = RequestMethod.POST)
    public BaseEntity<MyToken> loginPassword(@RequestBody User user){
        BaseEntity<MyToken> baseEntity = new BaseEntity<>();
        // 先检查用户是否存在
        User userByPhone = userService.getUserByPhone(user.getPhone_number());
        if (userByPhone == null){
            baseEntity.setCode(2);
            baseEntity.setMsg("用户不存在");
            return baseEntity;
        }

        User loginUser = userService.loginPassword(user);
        if (loginUser != null){
            // 成功登录
            MyToken token = new MyToken();
            String accessToken = RandomStringGenerator.generateAccessToken(loginUser);
            String refreshToken = RandomStringGenerator.generateRefreshToken(loginUser);
            token.setAccessToken(accessToken);
            token.setRefreshToken(refreshToken);

            baseEntity.setData(token);
            baseEntity.setCode(1);
            baseEntity.setMsg("登录成功");

        }else {
            // 登录失败
            baseEntity.setCode(2);
            baseEntity.setMsg("用户名或密码错误");
        }

        return baseEntity;
    }

    @RequestMapping(value = "/getUserInfo", method = RequestMethod.GET)
    public BaseEntity<User> getUserInfo(@RequestHeader(value = "Authorization", required = false) String authorizationHeader){

        // 提取Bearer令牌
        MyUtil util = new MyUtil();
        Claims parseToken = util.checkToken(authorizationHeader);
        BaseEntity<User> baseEntity = new BaseEntity<>();
        if (parseToken != null) {
            String userName = parseToken.getSubject();  // 获取用户名
            User user = userService.getUserByName(userName);
            if (user != null){
                baseEntity.setData(user);
                baseEntity.setCode(1);
                baseEntity.setMsg("登录成功");
            }else {
                baseEntity.setCode(-1);
                baseEntity.setMsg("用户读取错误");
            }
        } else {
            baseEntity.setCode(-3);
            baseEntity.setMsg("令牌已过期");
        }

        return baseEntity;
    }

    @RequestMapping(value = "/refreshAccessToken", method = RequestMethod.POST)
    public BaseEntity<MyToken> refreshAccessToken(@RequestBody MyToken myToken){
        BaseEntity<MyToken> baseEntity = new BaseEntity<>();
        String refreshToken = myToken.getRefreshToken();
        if (refreshToken==null){
            baseEntity.setCode(-1);
            baseEntity.setMsg("参数错误");
            return baseEntity;
        }
        String token = refreshToken.replace("Bearer ", "");
        Claims parseToken = RandomStringGenerator.parseRefreshToken(token);
        /*MyUtil util = new MyUtil();
        Claims parseToken = util.checkToken(refreshToken);*/
        if (parseToken != null){
            String accessToken = RandomStringGenerator.refreshAccessToken(refreshToken, userService);
            MyToken token1 = new MyToken();
            token1.setRefreshToken(refreshToken);
            token1.setAccessToken(accessToken);
            baseEntity.setCode(1);
            baseEntity.setData(token1);
        }else {
            baseEntity.setCode(-3);
            baseEntity.setMsg("令牌已过期");
        }
        return baseEntity;
    }

    // 获取所有公开的模板
    @RequestMapping(value = "/getAllTemplate", method = RequestMethod.GET)
    public BaseListEntity<TextTemplate> getAllTemplate(@RequestHeader(value = "Authorization", required = false) String authorizationHeader){

        // 提取Bearer令牌
        MyUtil util = new MyUtil();
        Claims parseToken = util.checkToken(authorizationHeader);
        BaseListEntity<TextTemplate> baseEntity = new BaseListEntity<>();
        if (parseToken != null) {
            String userName = parseToken.getSubject();  // 获取用户名
            User user = userService.getUserByName(userName);
            if (user != null){
                baseEntity.setCode(1);
                baseEntity.setData(userService.getAllTemplate());
            }else {
                baseEntity.setCode(-1);
                baseEntity.setMsg("请先登录");
            }
        } else {
            baseEntity.setCode(-3);
            baseEntity.setMsg("请先登录");
        }

        return baseEntity;
    }

    // 获取指定用户的模板
    @RequestMapping(value = "/getTemplateByUserId", method = RequestMethod.GET)
    public BaseListEntity<TextTemplate> getTemplateByUserId(@RequestHeader(value = "Authorization", required = false) String authorizationHeader){

        // 提取Bearer令牌
        MyUtil util = new MyUtil();
        Claims parseToken = util.checkToken(authorizationHeader);
        BaseListEntity<TextTemplate> baseEntity = new BaseListEntity<>();
        if (parseToken != null) {
            String userName = parseToken.getSubject();  // 获取用户名
            User user = userService.getUserByName(userName);
            if (user != null){
                baseEntity.setCode(1);
                baseEntity.setData(userService.getTextTemplatesByUserId(user.getUserName()));
            }else {
                baseEntity.setCode(-1);
                baseEntity.setMsg("请先登录");
            }
        } else {
            baseEntity.setCode(-3);
            baseEntity.setMsg("请先登录");
        }

        return baseEntity;
    }

    @RequestMapping(value = "/createTemplate", method = RequestMethod.POST)
    public BaseEntity<Integer> createTemplate(@RequestBody TextTemplate template, @RequestHeader(value = "Authorization", required = false) String authorizationHeader){
        MyUtil util = new MyUtil();
        Claims parseToken = util.checkToken(authorizationHeader);
        BaseEntity<Integer> baseEntity = new BaseEntity<>();
        if (parseToken == null){
            baseEntity.setCode(-3);
            baseEntity.setMsg("请先登录");
        }else {
            String userName = parseToken.getSubject();  // 获取用户名
            User user = userService.getUserByName(userName);
            if (user != null){
                if (template.getId() != 0){
                    boolean ii = userService.checkTemplateExist(template.getId());
                    if (ii){
                        // 如果已存在就更新
                        userService.updateTemplateById(template);
                    }
                }else {
                    // 新增
                    template.setSharerId(user.getUserName());
                    template.setUseNumber(0);
                    userService.addTemplate(template);
                }
                baseEntity.setCode(1);
                baseEntity.setData(1);
            }else {
                baseEntity.setCode(-1);
                baseEntity.setMsg("请先登录");
            }
        }
        return baseEntity;
    }
}
