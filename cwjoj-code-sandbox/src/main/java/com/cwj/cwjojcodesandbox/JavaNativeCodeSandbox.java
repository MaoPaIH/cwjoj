package com.cwj.cwjojcodesandbox;

import com.cwj.cwjojcodesandbox.model.ExecuteCodeRequest;
import com.cwj.cwjojcodesandbox.model.ExecuteCodeResponse;
import org.springframework.stereotype.Component;

/**
 * java原生代码沙箱实现，直接复用模板方法
 */
@Component
public class JavaNativeCodeSandbox extends JavaCodeSandboxTemplate {

    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {
        return super.executeCode(executeCodeRequest);
    }
}
