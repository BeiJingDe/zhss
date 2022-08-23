package com.ylxx.cloud.core.config;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.extension.plugins.pagination.optimize.JsqlParserCountOptimize;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import com.ylxx.cloud.core.system.SystemConsts;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.type.JdbcType;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@EnableTransactionManagement
@Configuration
@MapperScan(basePackages = MybatisPlusConfigPrimary.mapperScan, sqlSessionTemplateRef  = MybatisPlusConfigPrimary.configPrefix + "SqlSessionTemplate")
public class MybatisPlusConfigPrimary {

    public static final String configPrefix = "primary";
    public static final String mapperScan = "com.ylxx.cloud.**.mapper";
    public static final String mapperLocations = "classpath*:com/ylxx/cloud/**/mapper/xml/*.xml";
    public static final String typeAliasesPackage = "com.ylxx.cloud.**.model";

    @Bean(name = configPrefix + "DataSource")
    @Primary   //配置默认数据源
    @ConfigurationProperties(prefix = "spring.datasource.druid." + configPrefix)
    public DataSource dataSource() {
        return DruidDataSourceBuilder.create().build();
    }

    @Bean(configPrefix + "PaginationInterceptor")
    public PaginationInterceptor paginationInterceptor() {
        PaginationInterceptor paginationInterceptor = new PaginationInterceptor();
        // 设置请求的页面大于最大页后操作， true调回到首页，false 继续请求  默认false
        // paginationInterceptor.setOverflow(false);
        // 设置最大单页限制数量，默认 500 条，-1 不受限制
        paginationInterceptor.setLimit(SystemConsts.MAX_PAGE_SIZE);
        // 开启 count 的 join 优化,只针对部分 left join
        paginationInterceptor.setCountSqlParser(new JsqlParserCountOptimize(false));
        paginationInterceptor.setDialectType("mysql");
        return paginationInterceptor;
    }

    @Bean(name = configPrefix + "SqlSessionFactory")
    @Primary
    public SqlSessionFactory sqlSessionFactory(
            @Qualifier(configPrefix + "DataSource") DataSource dataSource,
            @Qualifier(configPrefix + "PaginationInterceptor") PaginationInterceptor paginationInterceptor) throws Exception {
        MybatisSqlSessionFactoryBean  bean = new MybatisSqlSessionFactoryBean();
        bean.setDataSource(dataSource);
        bean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources(mapperLocations));
        bean.setTypeAliasesPackage(typeAliasesPackage);

        MybatisConfiguration configuration = new MybatisConfiguration();
        configuration.setJdbcTypeForNull(JdbcType.NULL);
        configuration.setMapUnderscoreToCamelCase(true);
        configuration.setCacheEnabled(false);
        configuration.setLogImpl(org.apache.ibatis.logging.stdout.StdOutImpl.class);
        bean.setConfiguration(configuration);

        bean.setPlugins(new Interceptor[]{ //PerformanceInterceptor(),OptimisticLockerInterceptor()
                paginationInterceptor //添加分页功能
        });
        return bean.getObject();
    }

    @Bean(name = configPrefix + "TransactionManager")
    @Primary
    public DataSourceTransactionManager transactionManager(@Qualifier(configPrefix + "DataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean(name = configPrefix + "SqlSessionTemplate")
    @Primary
    public SqlSessionTemplate sqlSessionTemplate(@Qualifier(configPrefix + "SqlSessionFactory") SqlSessionFactory sqlSessionFactory)
            throws Exception {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

}