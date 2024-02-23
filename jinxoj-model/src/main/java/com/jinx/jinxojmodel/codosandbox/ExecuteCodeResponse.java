package com.jinx.jinxojmodel.codosandbox;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExecuteCodeResponse {

    /**
     * 输出
     */
    private List<String> outputList;

    /**
     * 接口信息
     */
    private String message;

    /**
     * 接口状态
     */
    private Integer status;


    /**
     * 程序执行信息
     */
    private JudgeInfo judgeInfo;


}
