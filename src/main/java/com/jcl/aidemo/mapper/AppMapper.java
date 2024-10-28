package com.jcl.aidemo.mapper;

import com.jcl.aidemo.bean.AppInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;


@Mapper
public interface AppMapper {
    @Select({
            "select",
            "version, updateInfo, versionName, versionCode, versionUrl, versionContent",
            "from appinfo"
    })
    AppInfo getAppInfo();
}
