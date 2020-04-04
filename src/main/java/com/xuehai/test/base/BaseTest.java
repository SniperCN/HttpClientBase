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
            if (((Map) casePathMap).get(suiteName) != null) {
                List<String> casePathList = (List<String>) ((Map)casePathMap).get(suiteName);
                testCase = loadTestCase(casePathList, getClass().getName());
            } else {
                throw new IllegalArgumentException("TestSuite<" + suiteName + ">用例配置不正确");
            }
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

    protected TestCase loadTestCase(List<String> casePathList, String className) {
        Log.info(CLASS_NAME, "开始加载用例数据,用例文件路径:{},待加载用例ClassName:{}",
                JSON.toJSONString(casePathList), className);
        TestCase testCase = null;
        try {
            for (String casePath : casePathList) {
                testCase = loadTestCase(casePath, className);
                if (testCase != null) {
                    Log.info(CLASS_NAME, "{}({})用例数据加载成功", testCase.getName(), className);
                    break;
                }
            }
        } catch (JSONException e) {
            Log.error(CLASS_NAME, "用例数据加载失败", e);
        } finally {
            if (testCase == null) {
                Log.info(CLASS_NAME, "{}({})用例数据加载失败", testCase.getName(), className);
            }
        }
        return testCase;
    }

    /**
     * @description:    获取TestCase实体
     * @param filePath  测试用例文件路径
     * @param className 测试用例对应的测试类类名
     * @return          返回TestCase实体
     * @author Sniper
     * @date 2019/3/13 17:02
     */
    protected TestCase loadTestCase(String filePath, String className) {
        Log.info(CLASS_NAME, "加载用例数据,用例文件路径:{},待加载用例ClassName:{}", filePath, className);
        TestCase testCase = null;
        if (!StringUtils.isEmpty(filePath)) {
            String testCaseJson = FileUtil.read(filePath, "UTF-8");
            String jsonPath = "$[className='" + className + "']";
            JSONArray testCases = (JSONArray) JSONPath.read(testCaseJson, jsonPath);
            if (testCases.size() > 1) {
                Log.warn(CLASS_NAME, "存在{}条类名为{}的测试用例,默认取最后一条", testCases.size(), className);
            }
            for (TestCase targetTestCase : testCases.toJavaList(TestCase.class)) {
                testCase = targetTestCase;
            }
        } else {
            Log.error(CLASS_NAME, "用例数据加载失败,文件路径为空字符串");
            throw new IllegalArgumentException("用例数据加载失败,文件路径不允许为空字符串");
        }
        if (testCase != null) {
            Log.info(CLASS_NAME, "用例ClassName:{}数据加载成功,当前路径:{}", className, filePath);
        } else {
            Log.info(CLASS_NAME, "{}中未包含用例ClassName:{}数据", filePath, className);
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
