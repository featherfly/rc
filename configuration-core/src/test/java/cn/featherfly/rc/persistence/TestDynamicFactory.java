
package cn.featherfly.rc.persistence;

import java.util.Set;

import org.springframework.core.type.classreading.MetadataReader;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import cn.featherfly.common.io.ClassPathScanningProvider;
import cn.featherfly.rc.javassist.DynamicConfigurationFacotry;
import cn.featherfly.rc.persistence.configs.WechatConfiguration;

/**
 * <p>
 * TestDynamicFactory
 * </p>
 *
 * @author zhongj
 */
public class TestDynamicFactory {

    private ConfigurationPersistenceFileImpl persistence;

    @BeforeClass
    public void before() {
        Set<MetadataReader> metadataReaders = new ClassPathScanningProvider()
                .findMetadata(new String[] { "cn.featherfly" });
        PropertiesFileConfigurationConfigurator configurator = new PropertiesFileConfigurationConfigurator(
                "featherfly-configuration-dynamic-test", metadataReaders);
        persistence = new ConfigurationPersistenceFileImpl(configurator);
    }

    @Test
    public void testFileStorage() {
        WechatConfiguration wechat = DynamicConfigurationFacotry.getInstance().instance(WechatConfiguration.class,
                persistence);
        System.out.println(wechat.getAllowAutoRegist());
        wechat.setAllowAutoRegist(!wechat.getAllowAutoRegist());
        System.out.println(wechat.getAllowAutoRegist());
    }

}
