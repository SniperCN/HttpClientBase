package com.xuehai.test.base;

import com.alibaba.fastjson.*;
import com.xuehai.test.model.Entity;
import com.xuehai.test.model.TestCase;
import com.xuehai.test.utils.FileUtil;
import org.apache.commons.lang3.StringUtils;
import org.testng.ITestContext;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import java.util.*;

/**
 * @ClassName BaseTest
 * @Description: 测试基础类
 * @Author Sniper
 * @Date 2019/3/13 16:55
 */
public class BaseTest {

    private static final String CLASS_NAME = BaseTest.class.getName();
    private static BaseClient baseClient;
    private TestCase testCase;
    private ITestContext iTestContext;

    protected BaseClient getBaseClient() {
        return baseClient;
    }

    protected ITestContext getITestContext() {
        return this.iTestContext;
    }

    /**
     * @description:            初始化TestCase和HttpClient
     * @param iTestContext           ITestContext
     * @return void
     * @author Sniper
     * @date 2019/3/15 17:17
     */
    @BeforeClass
    protected void beforeBaseClass(ITestContext iTestContext) {
        Log.info(CLASS_NAME, "测试类{}开始执行", getClass());
        this.iTestContext = iTestContext;
        Object casePathMap = Configuration.getConfig().get("case-path");
        if (casePathMap != null) {
            String suiteName = iTestContext.getSuite().getName();
            Log.debug(CLASS_NAME, "获取测试用例路径,当前SuiteName:{}", suiteName);
            String casePath = (String) ((Map)casePathMap).get(suiteName);
            testCase = parseTestCase(casePath, getClass().getName());
            if (baseClient == null) {
                baseClient = BaseClient.getInstance();
            }
        } else {
            throw new IllegalArgumentException("config.yaml缺少case-path配置项");
        }

    }

    /**
     * @description:    测试结束打印日志
     * @return void
     * @author Sniper
     * @date 2019/4/19 10:03
     */
    @AfterClass
    protected void afterBaseClass(){
        Log.info(CLASS_NAME, "测试类: {} 执行完毕", getClass());
    }

    /**
     * @description:    获取TestCase实体
     * @param filePath  测试用例文件路径
     * @param className 测试用例对应的测试类类名
     * @return          返回TestCase实体
     * @author Sniper
     * @date 2019/3/13 17:02
     */
    protected TestCase parseTestCase(String filePath, String className) {
        Log.info(CLASS_NAME, "开始加载测试用例,用例文件路径:{},待加载用例ClassName:{}", filePath, className);
        TestCase testCase = null;
        try {
            if (!StringUtils.isEmpty(filePath)) {
                String testCaseJson = FileUtil.read(filePath, "UTF-8");
                String jsonPath = "$[className='" + className + "']";
                Object tempTestCases = JSONPath.read(testCaseJson, jsonPath);
                if (tempTestCases != null) {
                    JSONArray testCases = (JSONArray) JSONPath.read(testCaseJson, jsonPath);
                    if (testCases.size() > 1) {
                        Log.warn(CLASS_NAME, "存在{}条类名为{}的测试用例,默认取最后一条", testCases.size(), className);
                    } else {
                        for (TestCase targetTestCase : testCases.toJavaList(TestCase.class)) {
                            testCase = targetTestCase;
                        }
                    }
                    if (testCase != null) {
                        Log.info(CLASS_NAME, "{}({})测试用例加载成功", testCase.getName(), className);
                    }
                } else {
                    throw new IllegalArgumentException("找不到测试用例数据");
                }
            } else {
                throw new IllegalArgumentException("测试用例加载失败,文件路径为空");
            }
        } catch (JSONException e) {
            Log.error(CLASS_NAME, "测试用例加载失败", e);
        }
        return testCase;
    }


    /**
     * @description:    测试数据初始化
     * @return java.util.Iterator<java.lang.Object[]>
     * @throws
     * @author Sniper
     * @date 2019/4/22 14:45
     */
    protected Iterator<Object[]> initData() {
        List<Object[]> dataList = new ArrayList<>();
        List<Map<String, Entity>> list = testCase.getEntityList();
        list.forEach(map -> dataList.add(new Object[]{map}));
        return dataList.iterator();
    }


}
