package com.ylxx.cloud.core.interceptor;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;

import com.ylxx.cloud.core.header.HeaderConsts;
import com.ylxx.cloud.core.util.HttpServletUtil;

import cn.hutool.core.util.StrUtil;

/**
 * 
 * @ClassName: SecuringRestInterceptor
 * @Description: restTemplate调用传递token
 * @author: caixiaopeng
 * @date: 2020年6月23日 下午3:39:17
 */
@Component
public class SecuringRestInterceptor implements ClientHttpRequestInterceptor {

	@Override
	public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution)
			throws IOException {
		HttpHeaders headers = request.getHeaders();

		HttpServletRequest req = HttpServletUtil.getRequest();
		// 请求头：token
		String token = req.getHeader(HeaderConsts.ACCESS_TOKEN);
		if (StrUtil.isNotBlank(token)) {
			headers.add(HeaderConsts.ACCESS_TOKEN, token);
		}
		// 请求头：menuCode
		String menuCode = req.getHeader(HeaderConsts.MENU_CODE);
		if (StrUtil.isNotBlank(menuCode)) {
			headers.add(HeaderConsts.MENU_CODE, menuCode);
		}
		return execution.execute(request, body);

	}
}
