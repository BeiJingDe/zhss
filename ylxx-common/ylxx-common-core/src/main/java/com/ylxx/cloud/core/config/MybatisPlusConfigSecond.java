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
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

//@EnableTransactionManagement
//@Configuration
//@MapperScan(basePackages = MybatisPlusConfigSecond.mapperScan, sqlSessionTemplateRef  = MybatisPlusConfigSecond.configPrefix + "SqlSessionTemplate")
public class MybatisPlusConfigSecond {

    public static final String configPrefix = "second";
    public static final String mapperScan = "com.ylxx.second.**.mapper";
    public static final String mapperLocations = "classpath*:com/ylxx/second/**/mapper/xml/*.xml";
    public static final String typeAliasesPackage = "com.ylxx.second.**.model";

    @Bean(name = configPrefix + "DataSource")
    @ConfigurationProperties(prefix = "spring.datasource.druid." + configPrefix)
    public DataSource dataSource() {
        return DruidDataSourceBuilder.create().build();
    }

    @Bean(configPrefix + "PaginationInterceptor")
    public PaginationInterceptor paginationInterceptor() {
        PaginationInterceptor paginationInterceptor = new PaginationInterceptor();
        // ???????????????????????????????????????????????? true??????????????????false ????????????  ??????false
        // paginationInterceptor.setOverflow(false);
        // ??????????????????????????????????????? 500 ??????-1 ????????????
        paginationInterceptor.setLimit(SystemConsts.MAX_PAGE_SIZE);
        // ?????? count ??? join ??????,??????????????? left join
        paginationInterceptor.setCountSqlParser(new JsqlParserCountOptimize(false));
        paginationInterceptor.setDialectType("mysql");
        return paginationInterceptor;
    }

    @Bean(name = configPrefix + "SqlSessionFactory")
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
                paginationInterceptor //??????????????????
        });
        return bean.getObject();
    }

    @Bean(name = configPrefix + "TransactionManager")
    public DataSourceTransactionManager transactionManager(@Qualifier(configPrefix + "DataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean(name = configPrefix + "SqlSessionTemplate")
    public SqlSessionTemplate sqlSessionTemplate(@Qualifier(configPrefix + "SqlSessionFactory") SqlSessionFactory sqlSessionFactory)
            throws Exception {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

}