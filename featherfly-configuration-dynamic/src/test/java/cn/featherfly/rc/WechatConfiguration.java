package cn.featherfly.rc;

public interface WechatConfiguration {

    Boolean getAllowAutoRegist();

    WechatConfiguration setAllowAutoRegist(Boolean allowAutoRegist);

    String[] getJsApis();

    WechatConfiguration setJsApis(String[] jsApis);
}
