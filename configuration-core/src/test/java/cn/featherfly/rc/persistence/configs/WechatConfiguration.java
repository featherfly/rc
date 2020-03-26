package cn.featherfly.rc.persistence.configs;

import cn.featherfly.rc.annotation.Configuration;
import cn.featherfly.rc.annotation.Configurations;

@Configurations(name = "wechat", descp = "描述")
public interface WechatConfiguration {

    @Configuration(descp = "允许注册")
    Boolean getAllowAutoRegist();

    WechatConfiguration setAllowAutoRegist(Boolean allowAutoRegist);

    String[] getJsApis();

    WechatConfiguration setJsApis(String[] jsApis);
}