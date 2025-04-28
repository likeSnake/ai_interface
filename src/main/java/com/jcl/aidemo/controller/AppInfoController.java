package com.jcl.aidemo.controller;

import com.jcl.aidemo.bean.*;
import com.jcl.aidemo.service.AppService;
import com.jcl.aidemo.impl.AppServiceImpl;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/appInfo")
public class AppInfoController {

    @Resource
    private AppService appService = new AppServiceImpl();

    @RequestMapping(value = "/getAppInfo", method = RequestMethod.GET)
    public BaseEntity<AppInfo> getAppInfo(){
        BaseEntity<AppInfo> baseEntity = new BaseEntity<>();
        AppInfo appInfo = appService.getAppInfo();
        baseEntity.setCode(1);
        baseEntity.setData(appInfo);
        return baseEntity;
    }
}
