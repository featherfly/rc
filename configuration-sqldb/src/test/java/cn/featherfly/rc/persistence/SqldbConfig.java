
package cn.featherfly.rc.persistence;

import java.util.HashSet;
import java.util.Set;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.log4j.xml.DOMConfigurator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import cn.featherfly.common.db.metadata.DatabaseMetadata;
import cn.featherfly.common.db.metadata.DatabaseMetadataManager;
import cn.featherfly.common.lang.ClassLoaderUtils;
import cn.featherfly.constant.ConstantConfigurator;
import cn.featherfly.hammer.Hammer;
import cn.featherfly.hammer.sqldb.SqldbHammerImpl;
import cn.featherfly.hammer.sqldb.jdbc.Jdbc;
import cn.featherfly.hammer.sqldb.jdbc.SpringJdbcTemplateImpl;
import cn.featherfly.hammer.sqldb.jdbc.mapping.JdbcMappingFactory;
import cn.featherfly.hammer.sqldb.sql.dialect.Dialects;
import cn.featherfly.hammer.tpl.TplConfigFactory;
import cn.featherfly.hammer.tpl.TplConfigFactoryImpl;
import cn.featherfly.rc.persistence.ConfigurationPersistenceSqlDBImpl;
import cn.featherfly.rc.spring.DynamicConfigurationScanSpringRegist;
import cn.featherfly.rc.spring.DynamicConfigurationSpringRegist;

/**
 * <p>
 * SqldbConfig
 * </p>
 *
 * @author zhongj
 */
@Configuration
public class SqldbConfig {

    @Bean
    public DynamicConfigurationSpringRegist dynamicConfigurationSpringRegist() {
        Set<String> packages = new HashSet<>();
        packages.add("cn.featherfly");
        DynamicConfigurationScanSpringRegist registor = new DynamicConfigurationScanSpringRegist(packages,
                "configurationPersistenceSqlDBImpl");
        return registor;
    }

    @Bean
    public ConfigurationPersistenceSqlDBImpl configurationPersistenceSqlDBImpl(Hammer hammer) {
        ConfigurationPersistenceSqlDBImpl p = new ConfigurationPersistenceSqlDBImpl();
        p.setHammer(hammer);
        return p;
    }

    @Bean
    public DataSource dataSource() {
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setUrl("jdbc:mysql://127.0.0.1:3306/rc_persitence");
        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
        dataSource.setUsername("root");
        dataSource.setPassword("123456");
        return dataSource;
    }

    @Bean
    public SqldbHammerImpl hammer(DataSource dataSource) {
        DOMConfigurator.configure(ClassLoaderUtils.getResource("log4j.xml", this.getClass()));
        ConstantConfigurator.config();

        Jdbc jdbc = new SpringJdbcTemplateImpl(dataSource, Dialects.MYSQL);
        DatabaseMetadata metadata = DatabaseMetadataManager.getDefaultManager().create(dataSource);

        JdbcMappingFactory mappingFactory = new JdbcMappingFactory(metadata, Dialects.MYSQL);

        TplConfigFactory configFactory = new TplConfigFactoryImpl("tpl/");

        SqldbHammerImpl hammer = new SqldbHammerImpl(jdbc, mappingFactory, configFactory);
        return hammer;
    }
}
