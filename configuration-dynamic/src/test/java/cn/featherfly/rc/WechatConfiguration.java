package cn.featherfly.rc;

import cn.featherfly.rc.annotation.ConfigurationDifinition;

@ConfigurationDifinition(name = "wechat", descp = "描述")
public interface WechatConfiguration {

    Boolean getAllowAutoRegist();

    WechatConfiguration setAllowAutoRegist(Boolean allowAutoRegist);

    String[] getJsApis();

    WechatConfiguration setJsApis(String[] jsApis);
}
