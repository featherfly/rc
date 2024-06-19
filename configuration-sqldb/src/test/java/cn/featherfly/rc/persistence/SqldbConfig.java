
package cn.featherfly.rc.persistence;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.log4j.xml.DOMConfigurator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import cn.featherfly.common.bean.AsmPropertyAccessorFactory;
import cn.featherfly.common.bean.PropertyAccessorFactory;
import cn.featherfly.common.db.dialect.Dialect;
import cn.featherfly.common.db.dialect.Dialects;
import cn.featherfly.common.db.mapping.JdbcMappingFactory;
import cn.featherfly.common.db.mapping.JdbcMappingFactoryImpl;
import cn.featherfly.common.db.mapping.SqlTypeMappingManager;
import cn.featherfly.common.db.metadata.DatabaseMetadata;
import cn.featherfly.common.db.metadata.DatabaseMetadataManager;
import cn.featherfly.common.lang.ClassLoaderUtils;
import cn.featherfly.common.repository.id.IdGeneratorManager;
import cn.featherfly.conversion.string.ToStringConversionPolicys;
import cn.featherfly.conversion.string.ToStringTypeConversion;
import cn.featherfly.hammer.Hammer;
import cn.featherfly.hammer.config.HammerConfigImpl;
import cn.featherfly.hammer.sqldb.SqldbHammerImpl;
import cn.featherfly.hammer.sqldb.jdbc.Jdbc;
import cn.featherfly.hammer.sqldb.jdbc.JdbcImpl;
import cn.featherfly.hammer.tpl.TplConfigFactory;
import cn.featherfly.hammer.tpl.TplConfigFactoryImpl;
import cn.featherfly.rc.repository.ConfigurationSqlDBRepository;
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
    public ConfigurationSqlDBRepository configurationPersistenceSqlDBImpl(Hammer hammer) {
        ConfigurationSqlDBRepository p = new ConfigurationSqlDBRepository();
        p.setHammer(hammer);
        p.setConversion(new ToStringTypeConversion(ToStringConversionPolicys.getBasicConversionPolicy()));
        return p;
    }

    @Bean
    public DataSource dataSource() {
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setUrl(
            "jdbc:mysql://127.0.0.1:3306/rc_persitence?serverTimezone=CTT&characterEncoding=utf8&useUnicode=true&useSSL=false");
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setUsername("root");
        dataSource.setPassword("123456");
        return dataSource;
    }

    @Bean
    public SqldbHammerImpl hammer(DataSource dataSource) throws SQLException {
        DOMConfigurator.configure(ClassLoaderUtils.getResource("log4j.xml", this.getClass()));
        //ConstantConfigurator.config();

        Dialect dialect = Dialects.mysql();
        DatabaseMetadata metadata = DatabaseMetadataManager.getDefaultManager().create(dataSource);

        SqlTypeMappingManager mappingManager = new SqlTypeMappingManager();

        PropertyAccessorFactory propertyAccessorFactory = new AsmPropertyAccessorFactory(
            Thread.currentThread().getContextClassLoader());

        HammerConfigImpl hammerConfig = new HammerConfigImpl();

        TplConfigFactory configFactory = TplConfigFactoryImpl.builder() //
            .prefixes("tpl/") //
            .config(hammerConfig.getTemplateConfig()) //
            .devMode(true) //
            .build();

        JdbcMappingFactory mappingFactory = new JdbcMappingFactoryImpl(metadata, dialect, mappingManager,
            new IdGeneratorManager(), propertyAccessorFactory);

        Connection conn = dataSource.getConnection();
        Jdbc jdbc = new JdbcImpl(conn, dialect, metadata, mappingManager, propertyAccessorFactory);

        SqldbHammerImpl hammer = new SqldbHammerImpl(jdbc, mappingFactory, configFactory, propertyAccessorFactory,
            hammerConfig);
        return hammer;
    }
}
