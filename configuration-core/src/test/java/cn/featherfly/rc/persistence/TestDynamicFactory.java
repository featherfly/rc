
package cn.featherfly.rc.persistence;

import java.util.Set;

import org.springframework.core.type.classreading.MetadataReader;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import cn.featherfly.common.io.ClassPathScanningProvider;
import cn.featherfly.rc.javassist.DynamicConfigurationFacotry;
import cn.featherfly.rc.persistence.configs.WechatConfiguration;
import cn.featherfly.rc.repository.ConfigurationFileRepository;
import cn.featherfly.rc.repository.PropertiesFileConfigurator;

/**
 * <p>
 * TestDynamicFactory
 * </p>
 *
 * @author zhongj
 */
public class TestDynamicFactory {

    private ConfigurationFileRepository persistence;

    @BeforeClass
    public void before() {
        Set<MetadataReader> metadataReaders = new ClassPathScanningProvider()
                .findMetadata(new String[] { "cn.featherfly" });
        PropertiesFileConfigurator configurator = new PropertiesFileConfigurator(
                "featherfly-configuration-dynamic-test", metadataReaders);
        persistence = new ConfigurationFileRepository(configurator);
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
