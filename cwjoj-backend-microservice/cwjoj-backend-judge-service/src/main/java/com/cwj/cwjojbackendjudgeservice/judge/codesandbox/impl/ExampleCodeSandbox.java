package com.cwj.cwjojbackendjudgeservice.judge.codesandbox.impl;

import com.cwj.cwjojbackendjudgeservice.judge.codesandbox.CodeSandbox;
import com.cwj.cwjojbackendmodel.model.codesandbox.ExecuteCodeRequest;
import com.cwj.cwjojbackendmodel.model.codesandbox.ExecuteCodeResponse;
import com.cwj.cwjojbackendmodel.model.codesandbox.JudgeInfo;
import com.cwj.cwjojbackendmodel.model.enums.JudgeInfoMessageEnum;
import com.cwj.cwjojbackendmodel.model.enums.QuestionSubmitStatusEnum;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * 示例代码沙箱（仅为了跑通业务流程）
 */
@Slf4j
public class ExampleCodeSandbox implements CodeSandbox {
    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {
        List<String> inputList = executeCodeRequest.getInputList();
        ExecuteCodeResponse executeCodeResponse = new ExecuteCodeResponse();
        executeCodeResponse.setOutputList(inputList);
        executeCodeResponse.setMessage("测试执行成功");
        executeCodeResponse.setStatus(QuestionSubmitStatusEnum.SUCCEED.getValue());
        JudgeInfo judgeInfo = new JudgeInfo();
        judgeInfo.setMessage(JudgeInfoMessageEnum.ACCEPTED.getText());
        judgeInfo.setMemory(100L);
        judgeInfo.setTime(100L);
        executeCodeResponse.setJudgeInfo(judgeInfo);
        return executeCodeResponse;
    }
}
