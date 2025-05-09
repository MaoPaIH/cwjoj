package com.cwj.cwjoj.judge.codesandbox.impl;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.cwj.cwjoj.common.ErrorCode;
import com.cwj.cwjoj.exception.BusinessException;
import com.cwj.cwjoj.judge.codesandbox.CodeSandbox;
import com.cwj.cwjoj.judge.codesandbox.model.ExecuteCodeRequest;
import com.cwj.cwjoj.judge.codesandbox.model.ExecuteCodeResponse;
import org.apache.commons.lang3.StringUtils;

/**
 * 远程代码沙箱（实际调用接口的沙箱）
 */
public class RemoteCodeSandbox implements CodeSandbox {

    // 定义鉴权请求头和密钥
    public static final String AUTH_REQUEST_HEADER = "auth";

    public static final String AUTH_REQUEST_SECRET = "secretKey";

    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {
        System.out.println("远程代码沙箱");
        String url = "http://192.168.62.130:8080/executeCode";
        String json = JSONUtil.toJsonStr(executeCodeRequest);
        String requestStr = HttpUtil.createPost(url)
                .header(AUTH_REQUEST_HEADER, AUTH_REQUEST_SECRET)
                .body(json)
                .execute()
                .body();
        if (StringUtils.isBlank(requestStr)) {
            throw new BusinessException(ErrorCode.API_REQUEST_ERROR, "executeCode remoteSandbox error, message = " + requestStr);
        }
        return JSONUtil.toBean(requestStr, ExecuteCodeResponse.class);
    }
}
