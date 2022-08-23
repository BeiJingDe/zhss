package com.ylxx.cloud.core.config;

import com.ylxx.cloud.core.system.SystemConsts;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.extension.plugins.pagination.optimize.JsqlParserCountOptimize;

/**
 * 
 * @ClassName: MybatisPlusConfig 
 * @Description: TODO
 * @author: caixiaopeng
 * @date: 2020年6月11日 下午2:08:29
 */
//@EnableTransactionManagement
//@Configuration
public class MybatisPlusConfig {

    @Bean
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
}