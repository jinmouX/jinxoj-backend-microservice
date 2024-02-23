package com.jinx.jinxojjudgeservice.judge.strategy;


import com.jinx.jinxojmodel.codosandbox.JudgeInfo;
import com.jinx.jinxojmodel.dto.question.JudgeCase;
import com.jinx.jinxojmodel.entity.Question;
import com.jinx.jinxojmodel.entity.QuestionSubmit;
import lombok.Data;

import java.util.List;

/**
 * 上下文
 */
@Data
public class JudgeContext {

    private JudgeInfo judgeInfo;

    private List<String> inputList;

    private List<String> outputList;

    private List<JudgeCase> judgeCaseList;

    private Question question;

    private QuestionSubmit questionSubmit;
}
