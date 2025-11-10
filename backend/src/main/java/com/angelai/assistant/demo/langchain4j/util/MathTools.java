package com.angelai.assistant.demo.langchain4j.util;

import dev.langchain4j.agent.tool.Tool;

/**
 * 给大模型定义的数学计算工具
 */
public class MathTools {
    @Tool("对给定的 2 个数字求和")
    public double sum(double a, double b) {
        return a + b;
    }

    @Tool("返回给定数字的平方根")
    public double squareRoot(double x) {
        return Math.sqrt(x);
    }
}
