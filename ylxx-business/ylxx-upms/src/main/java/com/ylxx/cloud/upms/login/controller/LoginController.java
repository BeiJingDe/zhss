package com.ylxx.cloud.upms.login.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONArray;
import com.alibaba.fastjson.JSON;
import com.sgcc.isc.core.orm.identity.Department;
import com.sgcc.isc.framework.common.constant.Constants;
import com.sgcc.isc.service.adapter.factory.AdapterFactory;
import com.sgcc.isc.service.adapter.helper.*;
import com.ylxx.cloud.core.annotation.LogAnnotation;
import com.ylxx.cloud.core.controller.BaseController;
import com.ylxx.cloud.core.header.HeaderConsts;
import com.ylxx.cloud.core.rest.ApiResult;
import com.ylxx.cloud.core.rest.ApiResultBuilder;
import com.ylxx.cloud.core.rest.ApiVersionConsts;
import com.ylxx.cloud.core.util.HttpServletUtil;
import com.ylxx.cloud.exception.ext.ActionException;
import com.ylxx.cloud.system.config.consts.ConfigConsts;
import com.ylxx.cloud.system.config.util.ConfigCacheUtil;
import com.ylxx.cloud.system.config.util.ConfigCacheValue;
import com.ylxx.cloud.system.log.enums.LogTypeEnums;
import com.ylxx.cloud.system.log.enums.OperateTypeEnum;
import com.ylxx.cloud.system.redis.util.RedisLockUtil;
import com.ylxx.cloud.system.redis.util.RedisUtil;
import com.ylxx.cloud.upms.login.model.LoginDTO;
import com.ylxx.cloud.upms.login.service.ILoginService;
import com.ylxx.cloud.upms.token.util.TokenCacheUtil;
import com.ylxx.cloud.upms.user.model.TestVO;
import com.ylxx.cloud.upms.user.model.UserVO;
import com.ylxx.cloud.upms.user.service.IUserService;
import com.ylxx.cloud.upms.user.util.UserCacheUtil;
import com.ylxx.cloud.util.JWTUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * 用户登录 前端控制器
 *
 * @author caixiaopeng
 * @since 2020-07-09
 */
@Slf4j
@RestController
@Api(tags = "登录控制：用户登录前端控制器")
@RequestMapping(ApiVersionConsts.V1 + "/upms")
@LogAnnotation(module = "账号登录", logType = LogTypeEnums.SYSTEM)
public class LoginController extends BaseController {

	@Resource
	private ILoginService loginService;
	@Resource
	private IUserService userService;

	@PostMapping("/login")
	@LogAnnotation(value = "用户登录", operateType = OperateTypeEnum.LOGIN)
	@ApiOperation(value = "用户登录")
	public ApiResult login(@RequestBody @Validated LoginDTO loginDto) {
		UserVO userVo = null;
		if(StrUtil.isNotBlank(loginDto.getUsername()) && StrUtil.isNotBlank(loginDto.getMmpd())) {
			// 通过用户名密码获取用户信息
			userVo = loginService.getUserByMmpd(loginDto.getUsername(), loginDto.getMmpd());
		} else if(StrUtil.isNotBlank(loginDto.getTicket())) {
			// 通过isc返回的ticket获取用户信息
			userVo = loginService.getUserByTicket(loginDto.getTicket());
		} else {
			throw new ActionException("登录失败：获取用户信息失败");
		}
		if(ObjectUtil.isNull(userVo)) {
			throw new ActionException("登录失败：获取用户信息失败");
		}

		// 3. 获取登录相关的系统参数
		String tokenTimeout = ConfigCacheValue.SYSTEM_KEY_TOKEN_TIMEOUT();// token有限期默认1天=1440分钟
		String sessionTimeout = ConfigCacheValue.SYSTEM_KEY_SESSION_TIMEOUT();// 默认30分钟
		String sameUserLoginNum = ConfigCacheValue.SYSTEM_KEY_SAME_USER_LOGIN_NUM(); // 默认1个

		// 4. 生成token
		String token = JWTUtil.createToken(userVo.getUsername(), userVo.getMmpd(), Long.parseLong(tokenTimeout)*60*1000);
		// 5. 将token注册到redis
		TokenCacheUtil.registerToken(userVo.getUsername(), token, System.currentTimeMillis() + "", Long.parseLong(sessionTimeout), TimeUnit.MINUTES);

		// 6. 判断相同用户登录的个数，超出登录个数，最早登录的那几个踢掉
		try {
			// 获取分布式锁
			boolean tryLock = RedisLockUtil.tryLock(userVo.getUsername());
			if(!tryLock) {
				throw new ActionException("登录获取锁失败，请重新登录");
			}
			Set<String> keys = TokenCacheUtil.getAllKeys(userVo.getUsername());
			if(ObjectUtil.isNotEmpty(keys)) {
				Map<Long, String> timeMillisMap = CollUtil.newHashMap();
				List<Long> timeMillisList = CollUtil.newArrayList();
				keys.forEach(key -> {
					String value = RedisUtil.get(key);
					try {
						long timeMillis = Long.parseLong(value);
						timeMillisList.add(timeMillis);
						timeMillisMap.put(timeMillis, key);
					} catch (Exception e) {
						// 格式有问题直接删除
						RedisUtil.delete(key);
					}
				});
				// token按时间降序排序
				timeMillisList.sort((o1, o2) -> (int) (o2 - o1));
				// 最末尾的登录最早，踢掉
				for(int i=Integer.parseInt(sameUserLoginNum); i<timeMillisList.size(); i++) {
					RedisUtil.delete(timeMillisMap.get(timeMillisList.get(i)));
				}
			}
		} finally {
			// 释放锁
			RedisLockUtil.unlock(userVo.getUsername());
		}

		// 7.用户信息缓存到redis，用于权限验证
		UserCacheUtil.setUser(userVo.getUsername(), userVo);

		// 8. 将token存入response，避免登录的时候记录日志取不到用户名
		HttpServletResponse response = HttpServletUtil.getResponse();
		response.addHeader(HeaderConsts.ACCESS_TOKEN, token);

		// 9. 更新用户登录时间
		userService.updateLastLoginTime(userVo.getUsername());

		// 10. 返回用户信息和token给前端
		return ApiResultBuilder.success(token);
	}

	/**
	 * 前端页面每次刷新都会请求改方法获取用户信息，并且会将用户信息缓存到redis
	 * @return
	 */
	@PostMapping("/info")
	@LogAnnotation(value = "获取用户信息", operateType = OperateTypeEnum.QUERY)
	@ApiOperation(value = "获取用户信息")
	public ApiResult info() {
		Map<String, Object> result = loginService.getInfo();
		return ApiResultBuilder.success(result);
	}

	@PostMapping("/logout")
	@LogAnnotation(value = "用户登出", operateType = OperateTypeEnum.LOGOUT)
	@ApiOperation(value = "用户登出")
	public ApiResult logout() {
		String token = HttpServletUtil.getToken();
		if(StrUtil.isNotBlank(token)) {
			String username = JWTUtil.getUsername(token);
			if(StrUtil.isNotBlank(username)) {
				try {
					// 获取分布式锁
					boolean tryLock = RedisLockUtil.tryLock(username);
					if(!tryLock) {
						throw new ActionException("登录获取锁失败，请重新登录");
					}
					// 删除注册的token
					TokenCacheUtil.deleteToken(username, token);
				} finally {
					// 释放锁
					RedisLockUtil.unlock(username);
				}
			}
		}
		return ApiResultBuilder.success().setMessage("用户登出成功！");
	}

	@PostMapping(value = "/isc-test")
	@LogAnnotation(value="ISC测试", operateType = OperateTypeEnum.QUERY)
	public ApiResult iscTest() {
		List<Object> arr = CollUtil.newArrayList();
		try {
			IDomainService domainService = AdapterFactory.getDomainService();
			IIdentityService identityService = AdapterFactory.getIdentityService();
			IOrganizationService organizationService = AdapterFactory.getOrganizationService();
			IResourceService resourceService = AdapterFactory.getResourceService();
			IRoleService roleService = AdapterFactory.getRoleService();

			System.out.println("########");

			String userId = "210000000419056";

			arr.add(


			// 基准组织
					identityService.getDepartmentById("4d56b75bc1de4f94ba626992aee286ad")
			// 业务组织单元信息
//					organizationService.getBusiOrgByUserId(userId, iscAppId)
			// 业务组织角色
//					roleService.getOrgRolesByUserId(userId, iscAppId, null)
			// 业务角色
//					roleService.getRoleByRoleId("ff8080816ecb14e6016ed07eca7d0241")
//					organizationService.getBusiOrgsByOrgName(iscAppId, null)
//					organizationService.getBusiOrgsByIds(new String[] {"ff808081706d68050170a44c76690a4b"})
//					roleService.getRoleGroup(iscAppId, "")
//					resourceService.getFuncTree("ff8080816e649f18016e655984a00079", iscAppId, "001")
//					this.buildMenuTree(resourceService.getFuncTree("ff8080816e649f18016e655984a00079", iscAppId, "002"), "pc")
			);

			arr.add(identityService.getDepartmentById("210000000030727"));
// 用户信息
			arr.add(identityService.getUserByIds(new String[] { "4d56b75bc1de4f94ba626992aee286ad" }));

			System.out.println("########");

		} catch (Exception e) {
			e.printStackTrace();
		}
		return ApiResultBuilder.success(arr);
	}

    @PostMapping(value = "/isc-test1")
    @LogAnnotation(value="ISC测试组织信息", operateType = OperateTypeEnum.QUERY)
    public ApiResult getSubDepartment(/*@RequestBody @Validated LoginDTO loginDto*/) {
		String userId =  "4d56b75bc1de4f94ba626992aee286ad";
       String deptId =  "210000000030727";
        Department de = loginService.getSubDepartment(userId,deptId);
        return ApiResultBuilder.success(de);
    }

	@PostMapping(value = "/isc-test5")
	@LogAnnotation(value="ISC测试", operateType = OperateTypeEnum.QUERY)
	public ApiResult iscTest5(@RequestBody TestVO testVO) {
		List<Object> arr = CollUtil.newArrayList();
		try {
			IIdentityService identityService = AdapterFactory.getIdentityService();
/*
   System.out.println("########");

   String userId = "210000000419056";*/

			arr.add(
					// 用户信息
//     identityService.getUserByIds(new String[] { userId })
					// 基准组织
				//identityService.getQuoteDepartmentsByConditionAndOrderBy(testVO.getTest1(),testVO.getTest2())
					identityService.getSubDepartment(testVO.getTest1(), Constants.DEPARTMENT_PROPERTY_DEPARTMENT)
			);

			System.out.println("########");

		} catch (Exception e) {
			e.printStackTrace();
		}
		return ApiResultBuilder.success(arr);
	}

	@PostMapping(value = "/isc-test6")
	@LogAnnotation(value="ISC测试", operateType = OperateTypeEnum.QUERY)
	public ApiResult iscTest6(@RequestBody TestVO testVO) {
		List<Object> arr = CollUtil.newArrayList();
		try {
			IIdentityService identityService = AdapterFactory.getIdentityService();
/*
   System.out.println("########");

   String userId = "210000000419056";*/

			arr.add(
					// 用户信息
//     identityService.getUserByIds(new String[] { userId })
					// 基准组织
					identityService.getUsersByOrg(testVO.getTest1(),testVO.getTest2(),testVO.getTest3(),testVO.getTest4())
			);

			System.out.println("########");

		} catch (Exception e) {
			e.printStackTrace();
		}
		return ApiResultBuilder.success(arr);
	}

	@PostMapping(value = "/isc-org-update")
	@LogAnnotation(value="ISC更新基准组织id", operateType = OperateTypeEnum.QUERY)
	public ApiResult iscOrgUpdate() {
		return loginService.iscOrgUpdate();
	}

}

