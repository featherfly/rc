TODO 返回的用于查询的对象值加入类型，目前从数据库查询出来都是字符串

# 1.3.2 2024-05-05
1. 修复ConfigurationSqlDBRepository getConfiguration(String name)方法空指针异常
2. 升级依赖

# 1.3.1 2023-01-12
1. 非兼容性版本升级
2. ConfigurationRepository.get(String,Class)重构为get(Class)

# 1.3.0 2022-10-31
1. 修改 V set(String configName, String name, V value)方法返回值用于修复缓存出错的问题

# 1.2.9 2022-10-8
1. 修复configuration-sqldb的初始化语句错误

# 1.2.8 2022-07-15
1. 修复使用jdk17时,springdev里的restartclassloader加载出错的问题

# 1.2.7 2022-04-06
1. 修复common-core不兼容升级

# 1.2.6 2021-5-11
1.修复springboot使用dev-tool进行热部署时，重新加载生成config报错的问题

# 1.2.5 2021-3-24
1.加入同一个类型(class)多次加入时忽略，而不是抛出异常

# 1.2.4 2021-2-22
1.升级依赖

# 1.2.3 2020-7-14
1.javassit动态创建配置接口实现类加入ClassLoader参数用于解决在springboot下不能正常的问题
    

# 1.2.2 2020-7-14
1.删除未使用的apt定义文件
2.javassit动态创建配置接口实现类加入ClassLoader参数用于解决在springboot下不能正常的问题
3.升级依赖

# 1.2.1 2020-5-21
1.ConfigurationRepository加入List<ConfigurationValue<?>> getConfigurations(String configName)

# 1.2.0 2020-4-11
1.重构项目结构

# 1.1.0 2019-12-03
1.加入ConfigurationPersistenceFileImpl可以使用文件作为配置存储
2.升级依赖