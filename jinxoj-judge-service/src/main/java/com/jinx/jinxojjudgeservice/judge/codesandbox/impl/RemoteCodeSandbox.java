package com.jinx.jinxojjudgeservice.judge.codesandbox.impl;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.jinx.jinxojcommon.common.ErrorCode;
import com.jinx.jinxojcommon.exception.BusinessException;
import com.jinx.jinxojjudgeservice.judge.codesandbox.CodeSandbox;
import com.jinx.jinxojmodel.codosandbox.ExecuteCodeRequest;
import com.jinx.jinxojmodel.codosandbox.ExecuteCodeResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;

/**
 * 远程代码沙箱（实际调用接口的沙箱）
 */
public class RemoteCodeSandbox implements CodeSandbox {

    @Value("${code.sandbox.id}")
    // 定义鉴权请求头和密钥
    private String id ;

    @Value("${code.sandbox.key}")
    private String key ;
    @Value("${code.sandbox.url}")
    private String url ;


    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {
        System.out.println("远程代码沙箱");
        String json = JSONUtil.toJsonStr(executeCodeRequest);
        String responseStr = HttpUtil.createPost(url)
                .header("X-APP-ID",id)
                .header("X-APP-KEY",key)
                .body(json)
                .execute()
                .body();
        if (StringUtils.isBlank(responseStr)) {
            throw new BusinessException(ErrorCode.API_REQUEST_ERROR, "executeCode remoteSandbox error, message = " + responseStr);
        }
        return JSONUtil.toBean(responseStr, ExecuteCodeResponse.class);
    }
}
