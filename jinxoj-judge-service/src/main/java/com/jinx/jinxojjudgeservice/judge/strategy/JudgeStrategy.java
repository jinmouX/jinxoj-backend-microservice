package com.jinx.jinxojjudgeservice.judge.strategy;


import com.jinx.jinxojmodel.codosandbox.JudgeInfo;

public interface JudgeStrategy {

    JudgeInfo doJudge(JudgeContext judgeContext);
}
