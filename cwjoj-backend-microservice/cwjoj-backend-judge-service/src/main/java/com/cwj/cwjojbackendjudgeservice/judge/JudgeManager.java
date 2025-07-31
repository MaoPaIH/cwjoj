package com.cwj.cwjojbackendjudgeservice.judge;

import com.cwj.cwjojbackendjudgeservice.judge.strategy.DefaultJudgeStrategy;
import com.cwj.cwjojbackendjudgeservice.judge.strategy.JavaLanguageJudgeStrategy;
import com.cwj.cwjojbackendjudgeservice.judge.strategy.JudgeContext;
import com.cwj.cwjojbackendjudgeservice.judge.strategy.JudgeStrategy;
import com.cwj.cwjojbackendmodel.model.codesandbox.JudgeInfo;
import com.cwj.cwjojbackendmodel.model.entity.QuestionSubmit;
import org.springframework.stereotype.Service;

/**
 * 判题管理（简化调用）
 */
@Service
public class JudgeManager {

    /**
     * 执行判题
     *
     * @param judgeContext
     * @return
     */
    JudgeInfo doJudge(JudgeContext judgeContext) {
        QuestionSubmit questionSubmit = judgeContext.getQuestionSubmit();
        String language = questionSubmit.getLanguage();
        JudgeStrategy judgeStrategy = new DefaultJudgeStrategy();
        if ("java".equals(language)) {
            judgeStrategy = new JavaLanguageJudgeStrategy();
        }
        return judgeStrategy.doJudge(judgeContext);
    }

}
