package com.jinx.jinxojjudgeservice.judge;


import cn.hutool.json.JSONUtil;
import com.jinx.jinxojcommon.common.ErrorCode;
import com.jinx.jinxojcommon.exception.BusinessException;
import com.jinx.jinxojjudgeservice.judge.codesandbox.CodeSandbox;
import com.jinx.jinxojjudgeservice.judge.codesandbox.CodeSandboxFactory;
import com.jinx.jinxojjudgeservice.judge.codesandbox.CodeSandboxProxy;
import com.jinx.jinxojjudgeservice.judge.strategy.JudgeContext;
import com.jinx.jinxojjudgeservice.judge.strategy.JudgeStrategy;
import com.jinx.jinxojmodel.codosandbox.ExecuteCodeRequest;
import com.jinx.jinxojmodel.codosandbox.ExecuteCodeResponse;
import com.jinx.jinxojmodel.codosandbox.JudgeInfo;
import com.jinx.jinxojmodel.dto.question.JudgeCase;
import com.jinx.jinxojmodel.entity.Question;
import com.jinx.jinxojmodel.entity.QuestionSubmit;
import com.jinx.jinxojmodel.enums.QuestionSubmitStatusEnum;
import com.jinx.jinxojserviceclient.service.QuestionFeignClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class JudgeServiceImpl implements JudgeService{

    @Resource
    private QuestionFeignClient questionFeignClient;

    @Resource
    private JudgeManager judgeManager;

    @Value("${codesandbox.type:example}")
    private String codesandboxType;
    @Override
    public QuestionSubmit doJudge(Long questionSubmitId) {
        QuestionSubmit questionSubmit = questionFeignClient.getQuestionSubmitById(questionSubmitId);

        if(questionSubmit == null){
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR,"题目未提交");
        }
        Long questionId = questionSubmit.getQuestionId();
        Question question = questionFeignClient.getQuestionById(questionId);
        if(question == null){
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR,"题目不存在");
        }
        //查询判题状态避免重复判题
        Integer status = questionSubmit.getStatus();
        if(QuestionSubmitStatusEnum.RUNNING.getValue().equals(status)){
            throw new BusinessException(ErrorCode.OPERATION_ERROR,"题目正在判题中");
        }
        //修改判题状态
        QuestionSubmit updateStatus = new QuestionSubmit();
        updateStatus.setId(questionSubmitId);
        updateStatus.setStatus(QuestionSubmitStatusEnum.RUNNING.getValue());
        boolean b = questionFeignClient.updateQuestionSubmitById(updateStatus);
        if(!b){
            throw new BusinessException(ErrorCode.OPERATION_ERROR,"判题状态修改失败");
        }
        //根据语言获取沙箱
        String language = questionSubmit.getLanguage();
        CodeSandbox codeSandbox = CodeSandboxFactory.newInstance(codesandboxType);
        codeSandbox = new CodeSandboxProxy(codeSandbox);
        //查询题目输入用例
        List<JudgeCase> judgeCaseList = JSONUtil.toList(question.getJudgeCase(), JudgeCase.class);
        List<String> inputList = judgeCaseList.stream().map(JudgeCase::getInput).collect(Collectors.toList());

        //调用沙箱执行程序
        ExecuteCodeRequest executeCodeRequest = new ExecuteCodeRequest();
        executeCodeRequest.setInputList(inputList);
        executeCodeRequest.setCode(questionSubmit.getCode());
        executeCodeRequest.setLanguage(language);
        ExecuteCodeResponse executeCodeResponse = codeSandbox.executeCode(executeCodeRequest);
        if(executeCodeResponse == null){
            throw new BusinessException(ErrorCode.OPERATION_ERROR,"调用沙箱失败");
        }
        //进行判题
        JudgeStrategy judgeStrategy = judgeManager.getJudgeStrategy(language);
        JudgeContext judgeContext = new JudgeContext();
        judgeContext.setJudgeInfo(executeCodeResponse.getJudgeInfo());
        judgeContext.setInputList(inputList);
        judgeContext.setOutputList(executeCodeResponse.getOutputList());
        judgeContext.setJudgeCaseList(judgeCaseList);
        judgeContext.setQuestion(question);
        judgeContext.setQuestionSubmit(questionSubmit);

        JudgeInfo judgeInfo = judgeStrategy.doJudge(judgeContext);
        QuestionSubmit submit = new QuestionSubmit();
        submit.setId(questionSubmitId);
        submit.setStatus(QuestionSubmitStatusEnum.SUCCEED.getValue());
        submit.setJudgeInfo(JSONUtil.toJsonStr(judgeInfo));
        boolean update = questionFeignClient.updateQuestionSubmitById(submit);
        if(!update){
            throw new BusinessException(ErrorCode.OPERATION_ERROR,"更新题目提交状态失败");
        }
        return questionFeignClient.getQuestionSubmitById(questionSubmitId);
    }
}
