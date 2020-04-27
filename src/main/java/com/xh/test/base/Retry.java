package com.xh.test.base;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

/**
 * @ClassName Retry
 * @Description:    失败用例重跑
 * @Author Sniper
 * @Date 2019/4/4 16:28
 */
public class Retry implements IRetryAnalyzer {
    private static final String CLASS_NAME = Retry.class.getName();
    private static final int MAX_EXECUTE_COUNT;
    private int count = 1;

    static {
        Object maxExecuteCount = Configuration.getConfig().get("max-execute-count");
        MAX_EXECUTE_COUNT =  maxExecuteCount!= null ? (int) maxExecuteCount : 3;
    }

    @Override
    public boolean retry(ITestResult result) {
        if (count < MAX_EXECUTE_COUNT) {
            Log.info(CLASS_NAME, "{}第{}次执行失败,即将重试第{}次", result.getMethod().getMethodName(), count, count + 1);
            count ++;
            return true;
        } else {
            count = 1;
            return false;
        }
    }

}
