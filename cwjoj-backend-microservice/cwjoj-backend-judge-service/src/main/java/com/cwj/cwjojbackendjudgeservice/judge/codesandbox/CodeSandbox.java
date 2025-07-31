package com.cwj.cwjojbackendjudgeservice.judge.codesandbox;


import com.cwj.cwjojbackendmodel.model.codesandbox.ExecuteCodeRequest;
import com.cwj.cwjojbackendmodel.model.codesandbox.ExecuteCodeResponse;

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
