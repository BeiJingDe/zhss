package com.ylxx.cloud.core.util;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.ylxx.cloud.core.header.HeaderConsts;
import com.ylxx.cloud.upms.user.model.UserVO;
import com.ylxx.cloud.upms.user.util.UserCacheUtil;
import com.ylxx.cloud.util.JWTUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName: HttpServletUtil
 * @Description: TODO
 * @author: caixiaopeng
 * @date: 2020年6月11日 下午1:59:13
 */
@Slf4j
public class HttpServletUtil {
	/**
	 * @Title: getRequest
	 * @Description: 获取request
	 * @return
	 */
	public static HttpServletRequest getRequest() {
		ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		HttpServletRequest request = servletRequestAttributes.getRequest();
		return request;
	}
	/**
	 * @Title: getResponse
	 * @Description: 获取response
	 * @return
	 */
	public static HttpServletResponse getResponse() {
		ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		HttpServletResponse response = servletRequestAttributes.getResponse();
		return response;
	}
	
	/**
	 * 通过response返回json格式数据
	 * @param response
	 * @param object
	 */
	public static void responseJSONMessage(HttpServletResponse response, Object object) {
		PrintWriter	out = null;
		try {
			response.setCharacterEncoding("utf-8");
			out = response.getWriter();
			response.setContentType("application/json; charset=utf-8");
			out.print(JSON.toJSONString(object));
			out.flush();
			out.close();
		} catch (IOException e) {
			log.error("通过response返回json格式数据失败");
		} finally {
			IoUtil.close(out);
		}
	}
	
	/**
	 * @Title: getOperateIp
	 * @Description: 获取请求用户IP
	 * @return
	 */
	public static String getOperateIp() {
		HttpServletRequest request = getRequest();
		String ip = null;
		// X-Forwarded-For：Squid 服务代理
		String ipAddresses = request.getHeader("X-Forwarded-For");
		if (ipAddresses == null || ipAddresses.length() == 0 || "unknown".equalsIgnoreCase(ipAddresses)) {
			// Proxy-Client-IP：apache 服务代理
			ipAddresses = request.getHeader("Proxy-Client-IP");
		}
		if (ipAddresses == null || ipAddresses.length() == 0 || "unknown".equalsIgnoreCase(ipAddresses)) {
			// WL-Proxy-Client-IP：weblogic 服务代理
			ipAddresses = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ipAddresses == null || ipAddresses.length() == 0 || "unknown".equalsIgnoreCase(ipAddresses)) {
			// HTTP_CLIENT_IP：有些代理服务器
			ipAddresses = request.getHeader("HTTP_CLIENT_IP");
		}
		if (ipAddresses == null || ipAddresses.length() == 0 || "unknown".equalsIgnoreCase(ipAddresses)) {
			// X-Real-IP：nginx服务代理
			ipAddresses = request.getHeader("X-Real-IP");
		}
		// 有些网络通过多层代理，那么获取到的ip就会有多个，一般都是通过逗号（,）分割开来，并且第一个ip为客户端的真实IP
		if (ipAddresses != null && ipAddresses.length() != 0) {
			ip = ipAddresses.split(",")[0];
		}
		// 还是不能获取到，最后再通过request.getRemoteAddr();获取
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ipAddresses)) {
			ip = request.getRemoteAddr();
		}
		return ip.equals("0:0:0:0:0:0:0:1") ? "127.0.0.1" : ip;
	}
	
	/**
	 * @Title: getParameter
	 * @Description: 获取请求参数Parameter
	 * @param request
	 * @return
	 */
	public static Map<String, String> getParameter(HttpServletRequest request) {
		Map<String, String> params = new HashMap<String, String>();
		Enumeration<String> attrNames = request.getParameterNames();
		while (attrNames.hasMoreElements()) {
			String name = attrNames.nextElement();
			params.put(name, request.getParameter(name));
		}
		return params;
	}
	
	/**
	 * 获取请求token
	 * @return
	 */
	public static String getToken() {
		HttpServletRequest request = getRequest();
		String token = request.getHeader(HeaderConsts.ACCESS_TOKEN);
		// 登录的时候request没有token，所以登录的时候将生成的token存入response
		if(StrUtil.isBlank(token) || StrUtil.isBlankOrUndefined(token) ){
			HttpServletResponse response = HttpServletUtil.getResponse();
			token = response.getHeader(HeaderConsts.ACCESS_TOKEN);
		}
		return token;
	}
	
	/**
	 * @Title: getUsername
	 * @Description: 获取请求的用户名
	 * @return
	 */
	public static String getUsername() {
		String token = getToken();
		if(StrUtil.isNotBlank(token)) {
			return JWTUtil.getUsername(token);
		}
		return null;
	}
	/**
	 * @Title: getUsername
	 * @Description: 获取请求的用户名
	 * @return
	 */
	public static String getUsername(String token) {
		if(StrUtil.isNotBlank(token)) {
			return JWTUtil.getUsername(token);
		}
		return null;
	}

	/**
	 * @Title: getMenuCode
	 * @Description: 获取请求来源菜单编码
	 * @return
	 */
	public static String getMenuCode() {
		HttpServletRequest request = getRequest();
		return request.getHeader(HeaderConsts.MENU_CODE);
	}

	/**
	 * 获取header值
	 * @param headerName
	 * @return
	 */
	public static String getHeader(String headerName) {
		HttpServletRequest request = getRequest();
		return request.getHeader(headerName);
	}

	/**
	 * 获取对应请求的用户信息
	 * @return
	 */
	public static UserVO getUser() {
		String username = getUsername();
		if (username != null) {
			return UserCacheUtil.getUser(username);
		}
		return null;
	}

}
