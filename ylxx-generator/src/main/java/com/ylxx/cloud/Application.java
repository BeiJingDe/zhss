package com.ylxx.cloud;

import org.yaml.snakeyaml.Yaml;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.TemplateConfig;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.VelocityTemplateEngine;

import cn.hutool.core.util.StrUtil;

/**
 * 
 * @author caixiaopeng
 *
 */
public class Application {
	
//	    protected ConfigBuilder config; // 配置信息
//	    protected InjectionConfig injectionConfig; // 注入配置
//	    private DataSourceConfig dataSource; // 数据库表配置
//	    private StrategyConfig strategy; // 数据库表配置
//	    private PackageConfig packageInfo; // 包 相关配置
//	    private TemplateConfig template; // 模板 相关配置
//	    private GlobalConfig globalConfig; // 全局 相关配置
//	    private AbstractTemplateEngine templateEngine; // 模板引擎

	public static void main(String[] args) {
		final JSONObject config = new Yaml().loadAs(
				Application.class.getClassLoader().getResourceAsStream("application.yml"), JSONObject.class);
		
		DataSourceConfig dataSourceConfig = new DataSourceConfig();
		dataSourceConfig.setDbType(DbType.MYSQL);
		dataSourceConfig.setUrl(config.getJSONObject("dataSourceConfig").getString("url"));
		dataSourceConfig.setDriverName(config.getJSONObject("dataSourceConfig").getString("driverName"));
		dataSourceConfig.setUsername(config.getJSONObject("dataSourceConfig").getString("username"));
		dataSourceConfig.setPassword(config.getJSONObject("dataSourceConfig").getString("password"));
		
		GlobalConfig globalConfig = new GlobalConfig();
		globalConfig.setOutputDir(config.getJSONObject("globalConfig").getString("outputDir"));
		globalConfig.setAuthor(config.getJSONObject("globalConfig").getString("author"));
		globalConfig.setOpen(true);
		globalConfig.setSwagger2(true);
		globalConfig.setFileOverride(true);
		globalConfig.setBaseResultMap(true);
		globalConfig.setBaseColumnList(false);
		globalConfig.setDateType(DateType.ONLY_DATE);
		
		StrategyConfig strategyConfig = new StrategyConfig();
		strategyConfig.setNaming(NamingStrategy.underline_to_camel);
		strategyConfig.setColumnNaming(NamingStrategy.underline_to_camel);
		strategyConfig.setRestControllerStyle(true);
		strategyConfig.setControllerMappingHyphenStyle(true);
		strategyConfig.setEntityLombokModel(true);
		strategyConfig.setSkipView(false);
		strategyConfig.setEntityTableFieldAnnotationEnable(false);
		strategyConfig.setTablePrefix(config.getJSONObject("strategyConfig").getString("tablePrefix").split(","));
		strategyConfig.setInclude(config.getJSONObject("strategyConfig").getString("include").split(","));
		strategyConfig.setSuperEntityClass("");
		strategyConfig.setSuperControllerClass("com.ylxx.cloud.core.controller.BaseController");
		strategyConfig.setSuperServiceClass("");
		strategyConfig.setSuperServiceImplClass("");
		
		PackageConfig packageConfig = new PackageConfig();
		packageConfig.setEntity("model");
		packageConfig.setParent(config.getJSONObject("packageConfig").getString("parent"));
		
		InjectionConfig injectionConfig = new InjectionConfig() {
			@Override
			public void initMap() {
				this.setMap(config.getJSONObject("injectionConfig"));
			}
		};

		new AutoGenerator()
				.setTemplateEngine(new VelocityTemplateEngine())
				.setCfg(injectionConfig)
				.setGlobalConfig(globalConfig.setEntityName("%sDO"))
				.setDataSource(dataSourceConfig)
				.setPackageInfo(packageConfig)
				.setStrategy(strategyConfig)
				.setTemplate(getTemplateConfig("DO"))
				.execute();
		new AutoGenerator()
				.setTemplateEngine(new VelocityTemplateEngine())
				.setCfg(injectionConfig)
				.setGlobalConfig(globalConfig.setEntityName("%sVO"))
				.setDataSource(dataSourceConfig)
				.setPackageInfo(packageConfig)
				.setStrategy(strategyConfig)
				.setTemplate(getTemplateConfig("VO"))
				.execute();
		new AutoGenerator()
				.setTemplateEngine(new VelocityTemplateEngine())
				.setCfg(injectionConfig)
				.setGlobalConfig(globalConfig.setEntityName("%sDTO"))
				.setDataSource(dataSourceConfig)
				.setPackageInfo(packageConfig)
				.setStrategy(strategyConfig)
				.setTemplate(getTemplateConfig("DTO"))
				.execute();
	}
	
	private static TemplateConfig getTemplateConfig(String entityType) {
		if(StrUtil.equals("DO", entityType)) {
			return new TemplateConfig()
					.setEntity("/templates/entity"+entityType+".java");
		}
		return new TemplateConfig()
				.setEntity("/templates/entity"+entityType+".java")
				.setController(null)
				.setMapper(null)
				.setService(null)
				.setServiceImpl(null)
				.setXml(null);
	}

}
