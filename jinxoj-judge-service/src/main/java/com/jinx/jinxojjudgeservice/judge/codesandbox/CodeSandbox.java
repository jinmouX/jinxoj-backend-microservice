package com.jinx.jinxojjudgeservice.judge.codesandbox;


import com.jinx.jinxojmodel.codosandbox.ExecuteCodeRequest;
import com.jinx.jinxojmodel.codosandbox.ExecuteCodeResponse;

/**
 * 代码沙箱接口定义
 */
public interface CodeSandbox {

    /**
     * 执行代码
     *
     * @param executeCodeRequest
     * @return
     */
    ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest);
}
