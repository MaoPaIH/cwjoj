package com.cwj.cwjoj.judge;

import com.cwj.cwjoj.judge.strategy.DefaultJudgeStrategy;
import com.cwj.cwjoj.judge.strategy.JavaLanguageJudgeStrategy;
import com.cwj.cwjoj.judge.strategy.JudgeContext;
import com.cwj.cwjoj.judge.strategy.JudgeStrategy;
import com.cwj.cwjoj.judge.codesandbox.model.JudgeInfo;
import com.cwj.cwjoj.model.entity.QuestionSubmit;
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
