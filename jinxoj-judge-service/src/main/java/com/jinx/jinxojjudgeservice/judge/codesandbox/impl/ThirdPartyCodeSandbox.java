package com.jinx.jinxojjudgeservice.judge.codesandbox.impl;


import com.jinx.jinxojjudgeservice.judge.codesandbox.CodeSandbox;
import com.jinx.jinxojmodel.codosandbox.ExecuteCodeRequest;
import com.jinx.jinxojmodel.codosandbox.ExecuteCodeResponse;

/**
 * 第三方代码沙箱（调用网上现成的代码沙箱）
 */
public class ThirdPartyCodeSandbox implements CodeSandbox {
    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {
        System.out.println("第三方代码沙箱");
        return null;
    }
}
