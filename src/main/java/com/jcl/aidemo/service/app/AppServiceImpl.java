package com.jcl.aidemo.service.app;

import com.jcl.aidemo.bean.AppInfo;
import com.jcl.aidemo.mapper.AppMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("AppService")
public class AppServiceImpl implements AppService{

    @Autowired
    AppMapper appMapper;

    @Override
    public AppInfo getAppInfo() {
        return appMapper.getAppInfo();
    }
}
