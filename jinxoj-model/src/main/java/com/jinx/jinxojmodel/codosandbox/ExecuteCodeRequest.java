package com.jinx.jinxojmodel.codosandbox;


import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
public class ExecuteCodeRequest {

    /**
     * 输入用例
     */
    private List<String> inputList;

    /**
     * 代码
     */
    private String code;
    /**
     * 语言
     */
    private String language;
}
