package com.jinx.jinxojjudgeservice.judge;


import com.jinx.jinxojjudgeservice.config.JudgeTypesConfig;
import com.jinx.jinxojjudgeservice.judge.strategy.impl.JavaLanguageJudgeStrategy;
import com.jinx.jinxojjudgeservice.judge.strategy.JudgeStrategy;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class JudgeManager implements ApplicationContextAware {

    private static final Map<String,JudgeStrategy> judgeStrategies = new ConcurrentHashMap<>();
    @Resource
    private JudgeTypesConfig typesConfig;


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        typesConfig.getTypes().forEach((k,v) -> {
            judgeStrategies.put(k,applicationContext.getBean(v,JudgeStrategy.class));
        });
    }

    public JudgeStrategy getJudgeStrategy(String language) {
        return judgeStrategies.get(language);
    }
}
