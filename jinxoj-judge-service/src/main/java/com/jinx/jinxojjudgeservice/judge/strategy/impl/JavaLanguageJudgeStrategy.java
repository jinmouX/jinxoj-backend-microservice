package com.jinx.jinxojjudgeservice.judge.strategy.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.jinx.jinxojjudgeservice.judge.strategy.JudgeContext;
import com.jinx.jinxojjudgeservice.judge.strategy.JudgeStrategy;
import com.jinx.jinxojmodel.codosandbox.JudgeInfo;
import com.jinx.jinxojmodel.dto.question.JudgeCase;
import com.jinx.jinxojmodel.dto.question.JudgeConfig;
import com.jinx.jinxojmodel.entity.Question;
import com.jinx.jinxojmodel.enums.JudgeInfoMessageEnum;


import java.util.List;
import java.util.Optional;

public class JavaLanguageJudgeStrategy implements JudgeStrategy {
    @Override
    public JudgeInfo doJudge(JudgeContext judgeContext) {
        JudgeInfo judgeInfo = judgeContext.getJudgeInfo();
        Long memory = Optional.ofNullable(judgeInfo.getMemory()).orElse(0L);
        Long time = Optional.ofNullable(judgeInfo.getTime()).orElse(0L);
        Long stack = Optional.ofNullable(judgeInfo.getStack()).orElse(0L);
        List<String> inputList = judgeContext.getInputList();
        List<String> outputList = judgeContext.getOutputList();
        List<JudgeCase> judgeCaseList = judgeContext.getJudgeCaseList();

        Question question = judgeContext.getQuestion();
        JudgeInfo returnInfo = new JudgeInfo();
        returnInfo.setMemory(memory);
        returnInfo.setTime(time);
        returnInfo.setStack(stack);

        //判断答案是否一致
        //数量
        if(inputList.size() != outputList.size()){
            JudgeInfoMessageEnum wrongAnswer = JudgeInfoMessageEnum.WRONG_ANSWER;
            returnInfo.setMessage(wrongAnswer.getValue());
            return returnInfo;
        }
        //结果
        for(int i =0;i<outputList.size();i++){
            if(judgeCaseList.get(i).equals(outputList.get(i))){
                JudgeInfoMessageEnum wrongAnswer = JudgeInfoMessageEnum.WRONG_ANSWER;
                returnInfo.setMessage(wrongAnswer.getValue());
                return returnInfo;
            }
        }
        //判断限制是否合格
        JudgeConfig judgeConfig = JSONUtil.toBean(question.getJudgeConfig(), JudgeConfig.class);
        Long timeLimit = judgeConfig.getTimeLimit();
        Long memoryLimit = judgeConfig.getMemoryLimit();
        Long stackLimit = judgeConfig.getStackLimit();
        String msg = "";
        if(time > timeLimit){
            msg += JudgeInfoMessageEnum.TIME_LIMIT_EXCEEDED.getValue() + "\n";
        }
        if(memory > memoryLimit){
            msg += JudgeInfoMessageEnum.MEMORY_LIMIT_EXCEEDED.getValue() + "\n";
        }
        if(stack > stackLimit){
            msg += JudgeInfoMessageEnum.STACK_LIMIT_EXCEEDED.getValue() + "\n";
        }
        if(StrUtil.isEmpty(msg)){
            msg = JudgeInfoMessageEnum.ACCEPTED.getValue();
        }
        returnInfo.setMessage(msg);
        return returnInfo;
    }
}
