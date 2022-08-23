package com.ylxx.cloud.upms.login.service;

import com.sgcc.isc.core.orm.complex.FunctionNode;
import com.sgcc.isc.core.orm.identity.Department;
import com.ylxx.cloud.core.rest.ApiResult;
import com.ylxx.cloud.upms.login.model.IscRule;
import com.ylxx.cloud.upms.login.model.UserIscInfo;
import com.ylxx.cloud.upms.user.model.UserVO;

import java.util.List;
import java.util.Map;

public interface ILoginService {

    UserVO getUserByMmpd(String username, String mmpd);

    UserVO getUserByTicket(String ticket);

    Map<String, Object> getInfo();

    Department getSubDepartment(String deptId, String userId);

    UserIscInfo getMenuByUserId(String userId);

    void getFuncNode(List<FunctionNode> funcNode, IscRule iscRule);

    ApiResult iscOrgUpdate();

}
