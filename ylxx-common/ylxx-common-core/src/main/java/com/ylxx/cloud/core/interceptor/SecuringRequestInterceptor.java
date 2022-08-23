package com.ylxx.cloud.core.interceptor;

import cn.hutool.core.util.StrUtil;
import com.ylxx.cloud.core.header.HeaderConsts;
import com.ylxx.cloud.core.util.HttpServletUtil;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * 
* @ClassName: SecuringRequestInterceptor
* @Description: feign调用传递token
* @author: caixiaopeng
* @date: 2020年6月23日 下午2:48:30
 */
@Component
public class SecuringRequestInterceptor implements RequestInterceptor {
	
	@Override
	public void apply(RequestTemplate requestTemplate) {
		HttpServletRequest request = HttpServletUtil.getRequest();
		// 请求头：token
		String token = request.getHeader(HeaderConsts.ACCESS_TOKEN);
		if(StrUtil.isNotBlank(token)) {
			requestTemplate.header(HeaderConsts.ACCESS_TOKEN, token);
		}
		// 请求头：menuCode
		String menuCode = request.getHeader(HeaderConsts.MENU_CODE);
		if(StrUtil.isNotBlank(menuCode)) {
			requestTemplate.header(HeaderConsts.MENU_CODE, menuCode);
		}
	}
	
}
