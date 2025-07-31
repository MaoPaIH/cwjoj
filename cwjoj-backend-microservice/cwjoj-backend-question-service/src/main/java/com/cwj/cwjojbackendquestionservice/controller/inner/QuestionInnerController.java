package com.cwj.cwjojbackendquestionservice.controller.inner;

import com.cwj.cwjojbackendmodel.model.entity.Question;
import com.cwj.cwjojbackendmodel.model.entity.QuestionSubmit;
import com.cwj.cwjojbackendquestionservice.service.QuestionService;
import com.cwj.cwjojbackendquestionservice.service.QuestionSubmitService;
import com.cwj.cwjojbackendserviceclient.service.QuestionFeignClient;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 该服务仅内部调用
 */
@RestController
@RequestMapping("/inner")
public class QuestionInnerController implements QuestionFeignClient {

    @Resource
    private QuestionService questionService;

    @Resource
    private QuestionSubmitService questionSubmitService;

    @Override
    @GetMapping("/get/id")
    public Question getQuestionById(@RequestParam("questionId") long questionId) {
        return questionService.getById(questionId);
    }

    @Override
    @GetMapping("/question_submit/get/id")
    public QuestionSubmit getQuestionSubmitById(@RequestParam("questionSubmitId") long questionSubmitId) {
        return questionSubmitService.getById(questionSubmitId);
    }

    @Override
    @PostMapping("/question_submit/update")
    public boolean updateQuestionSubmitById(@RequestParam("questionSubmit") QuestionSubmit questionSubmit) {
        return questionSubmitService.updateById(questionSubmit);
    }
}
