package com.jcl.aidemo.mapper;

import com.jcl.aidemo.bean.AppInfo;
import com.jcl.aidemo.bean.TextTemplate;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;


@Mapper
public interface AppMapper {
    @Select({
            "SELECT * FROM appinfo ORDER BY versionCode DESC LIMIT 1"
    })
    AppInfo getAppInfo();
}
