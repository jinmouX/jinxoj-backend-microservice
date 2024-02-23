package com.jinx.jinxojjudgeservice.judge;


import com.jinx.jinxojmodel.entity.QuestionSubmit;

public interface JudgeService {

    QuestionSubmit doJudge(Long questionSubmitId);
}
