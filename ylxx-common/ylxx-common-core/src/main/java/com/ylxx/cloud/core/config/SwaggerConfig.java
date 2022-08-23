package com.ylxx.cloud.core.config;

import cn.hutool.core.util.ObjectUtil;
import cn.weiguangfu.swagger2.plus.annotation.EnableSwagger2Plus;
import com.ylxx.cloud.core.header.HeaderConsts;
import com.ylxx.cloud.core.system.SystemConsts;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @ClassName: SwaggerConfig
 * @Description: TODO
 * @author: caixiaopeng
 * @date: 2020年6月11日 下午2:08:34
 */
@Configuration
@EnableSwagger2Plus
public class SwaggerConfig {

	@Bean
    public Docket createRestApi() {
		ParameterBuilder parameterBuilder = new ParameterBuilder();
		Parameter build = parameterBuilder.name(HeaderConsts.ACCESS_TOKEN)
				.description("用户登录返回的token")
				.parameterType("header")
				.required(false)
				.modelRef(new ModelRef("string"))
				.build();
		List<Parameter> parameters = new ArrayList<>();
		parameters.add(build);
		return new Docket(DocumentationType.SWAGGER_2)
	            .apiInfo(apiInfo())
				.globalOperationParameters(parameters)
				.select()
				.apis(
						RequestHandlerSelectors.any() // 所有都暴露
//            			RequestHandlerSelectors.basePackage("com.ylxx.*") // 指定包位置
	            )
				.paths(path -> !ObjectUtil.equal(path, "/error") && !path.startsWith("/actuator"))
	            .paths(PathSelectors.any())
	            .build()
				.groupName(SystemConsts.SWAGGER_GROUP_NAME);
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
	            .title("spring boot示例接口API")
	            .description("spring boot示例接口API")
	            .version("1.0.0")
	            .build();
    }

}
