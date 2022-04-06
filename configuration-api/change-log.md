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