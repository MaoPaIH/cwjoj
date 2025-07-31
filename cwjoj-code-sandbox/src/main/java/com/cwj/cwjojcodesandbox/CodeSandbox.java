package com.cwj.cwjojcodesandbox;

import com.cwj.cwjojcodesandbox.model.ExecuteCodeRequest;
import com.cwj.cwjojcodesandbox.model.ExecuteCodeResponse;

/**
 * 代码沙箱接口定义
 */
public interface CodeSandbox {

    /**
     * 执行代码
     *
     * @param executeCodeRequest
     * @return
     */
    ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest);
}
