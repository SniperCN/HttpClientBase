package com.xuehai.base;

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
    private static final int MAX_RETRY_COUNT = (int) Configuration.getConfig().get("max-retry-count");
    private int count = 1;


    @Override
    public boolean retry(ITestResult result) {
        if (count < MAX_RETRY_COUNT) {
            Log.info(CLASS_NAME, "{}执行失败重跑,当前重跑次数: {}", result.getMethod().getMethodName(), count);
            count ++;
            return true;
        } else {
            count = 1;
            return false;
        }
    }

}
