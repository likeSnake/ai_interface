package com.jcl.aidemo.impl;

import com.jcl.aidemo.bean.AppInfo;
import com.jcl.aidemo.mapper.AppMapper;
import com.jcl.aidemo.service.AppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("AppService")
public class AppServiceImpl implements AppService {

    @Autowired
    AppMapper appMapper;

    @Override
    public AppInfo getAppInfo() {
        return appMapper.getAppInfo();
    }
}
