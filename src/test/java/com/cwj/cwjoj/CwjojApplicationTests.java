package com.cwj.cwjoj;

import com.cwj.cwjoj.judge.codesandbox.CodeSandbox;
import com.cwj.cwjoj.judge.codesandbox.CodeSandboxFactory;
import com.cwj.cwjoj.judge.codesandbox.CodeSandboxProxy;
import com.cwj.cwjoj.judge.codesandbox.model.ExecuteCodeRequest;
import com.cwj.cwjoj.judge.codesandbox.model.ExecuteCodeResponse;
import com.cwj.cwjoj.model.enums.QuestionSubmitLanguageEnum;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

@SpringBootTest
class CwjojApplicationTests {

    @Value("${codesandbox.type}")
    private String type;

    @Test
    void contextLoads() {
    }

    @Test
    void executeCodeByProxy() {
        CodeSandbox codeSandbox = CodeSandboxFactory.newInstance(type);
        codeSandbox = new CodeSandboxProxy(codeSandbox);
        String code = "public class Main {\n" +
                "\n" +
                "    public static void main(String[] args) {\n" +
                "        int a = Integer.parseInt(args[0]);\n" +
                "        int b = Integer.parseInt(args[1]);\n" +
                "        System.out.println((a + b));\n" +
                "    }\n" +
                "}";
        String language = QuestionSubmitLanguageEnum.JAVA.getValue();
        List<String> inputList = Arrays.asList("1 2", "3 4");
        ExecuteCodeRequest executeCodeRequest = ExecuteCodeRequest.builder()
                .code(code)
                .language(language)
                .inputList(inputList)
                .build();
        ExecuteCodeResponse executeCodeResponse = codeSandbox.executeCode(executeCodeRequest);
        System.out.println(executeCodeResponse);
    }

}
