package com.xh.test.base;

import com.alibaba.fastjson.*;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.xh.test.model.Assertion;
import com.xh.test.model.Entity;
import com.xh.test.model.Response;
import com.xh.test.model.ResponseDTO;
import com.xh.test.utils.AssertionUtil;
import com.xh.test.utils.FileUtil;
import org.apache.commons.lang3.StringUtils;
import org.testng.ITestContext;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeSuite;
import java.lang.reflect.Method;
import java.util.*;
import static io.qameta.allure.Allure.parameter;

/**
 * @ClassName BaseTest
 * @Description: 测试基础类
 * @Author Sniper
 * @Date 2019/3/13 16:55
 */
public class BaseTest {

    private static final String CLASS_NAME = BaseTest.class.getName();
    protected static final Map<String, Entity> ENTITY_MAP = new HashMap<>();
    protected static final Map<String, Object> DATA_MAP = new HashMap<>();
    protected static ITestContext iTestContext;

    @BeforeSuite
    protected void beforeSuite(ITestContext context) {
        String suiteName = context.getSuite().getName();
        iTestContext = context;
        //初始化entity
        Map<String, Object> entitySourcePathMap =  (Map<String, Object>) Configuration.getConfig().get("entity-source-path");
        if (entitySourcePathMap != null) {
            Object entitySourcePath = entitySourcePathMap.get(suiteName);
            if (entitySourcePath != null) {
                List<String> sourcePathList = (List<String>) entitySourcePath;
                loadEntity(sourcePathList);
                Log.info(CLASS_NAME, "Entity加载成功");
            } else {
                Log.error(CLASS_NAME, "TestSuite<{}> Entity配置不允许为空", suiteName);
                throw new NullPointerException("TestSuite<" + suiteName + "> Entity配置不允许为空");
            }
        } else {
            Log.error(CLASS_NAME, "entity-source-path配置不允许为空");
            throw new NullPointerException("entity-source-path配置不允许为空");
        }
        //初始化dataProvider
        Map<String, Object> dataSourcePathMap = (Map<String, Object>) Configuration.getConfig().get("data-source-path");
        if (dataSourcePathMap != null) {
            Object dataSourcePath = dataSourcePathMap.get(suiteName);
            if (dataSourcePath != null) {
                List<String> sourcePathList = (List<String>) dataSourcePath;
                loadData(sourcePathList);
                Log.info(CLASS_NAME, "Data加载成功");
            } else {
                Log.error(CLASS_NAME, "TestSuite<{}> Data配置不允许为空", suiteName);
                throw new NullPointerException("TestSuite<" + suiteName + "> Data配置不允许为空");
            }
        } else {
            Log.error(CLASS_NAME, "data-source-path配置不允许为空");
            throw new NullPointerException("data-source-path配置不允许为空");
        }
        //初始化课堂配置
        Map<String, Object> classroomConfigMap = (Map<String, Object>) Configuration.getConfig().get("classroom-config");
        if (classroomConfigMap != null) {
            classroomConfigMap.forEach(iTestContext::setAttribute);
        }
    }

    /**
     * @description:     测试开始打印日志
     * @return void
     * @author Sniper
     * @date 2019/3/15 17:17
     */
    @BeforeClass
    protected void beforeBaseClass(ITestContext iTestContext) {
        Log.info(CLASS_NAME, "测试类{}开始执行", getClass());
    }

    /**
     * @description:    测试结束打印日志
     * @return void
     * @author Sniper
     * @date 2019/4/19 10:03
     */
    @AfterClass
    protected void afterBaseClass(){
        Log.info(CLASS_NAME, "测试类{}执行完毕", getClass());
    }

    /**
     * @description: 加载测试数据
     * @param   dataSourcePathList  测试数据源路径列表
     * @return void
     * @author Sniper
     * @throws
     * @date 2020/4/27 15:30
     */
    private void loadData(List<String> dataSourcePathList) {
        dataSourcePathList.forEach(dataSourcePath -> {
            Map<String, Object> temp = loadData(dataSourcePath);
            if (temp != null) {
                DATA_MAP.putAll(temp);
            }
        });
    }

    /**
     * @description: 加载测试数据
     * @param  dataSourcePath   测试数据源路径
     * @return Map<String, Object>
     * @author Sniper
     * @throws
     * @date 2020/4/27 15:29
     */
    private Map<String, Object> loadData(String dataSourcePath) {
        try {
            if (!StringUtils.isEmpty(dataSourcePath)) {
                String entitySourceData = FileUtil.read(dataSourcePath, "UTF-8");
                Map<String, Object> dataMap = JSONObject.parseObject(entitySourceData, Map.class);
                Log.info(CLASS_NAME, "{},Data加载成功", dataSourcePath);
                return dataMap;
            } else {
                return null;
            }
        } catch (JSONException e) {
            Log.error(CLASS_NAME, dataSourcePath + ",Data加载失败", e);
            return null;
        }
    }

    /**
     * @description: 加载请求实体
     * @param  entitySourcePathList 请求实体数据源路径列表
     * @return void
     * @author Sniper
     * @throws
     * @date 2020/4/27 15:28
     */
    private void loadEntity(List<String> entitySourcePathList) {
        entitySourcePathList.forEach(entitySourcePath -> {
            Map<String, Entity> temp = loadEntity(entitySourcePath);
            if (temp != null) {
                ENTITY_MAP.putAll(temp);
            }
        });
    }

    /**
     * @description: 加载请求实体
     * @param  entitySourcePath 请求实体数据源路径
     * @return  Map<String, Entity>
     * @author Sniper
     * @throws
     * @date 2020/4/27 15:27
     */
    private Map<String, Entity> loadEntity(String entitySourcePath) {
        try {
            if (!StringUtils.isEmpty(entitySourcePath)) {
                Map<String, Entity> map = new HashMap<>();
                String entitySourceData = FileUtil.read(entitySourcePath, "UTF-8");
                JSONObject entityJSONObject = JSONObject.parseObject(entitySourceData);
                for (String key : entityJSONObject.keySet()) {
                    map.put(key, JSONObject.toJavaObject(entityJSONObject.getJSONObject(key), Entity.class));
                }
                Log.info(CLASS_NAME, "{},Entity加载成功", entitySourcePath);
                return map;
            } else {
                return null;
            }
        } catch (JSONException e) {
            Log.error(CLASS_NAME, entitySourcePath + ",Entity加载失败", e);
            return null;
        }
    }

    /**
     * @description:    测试数据初始化
     * @return java.util.Iterator<java.lang.Object[]>
     * @throws
     * @author Sniper
     * @date 2019/4/22 14:45
     */
    protected Iterator<Object[]> dataProviderInit(Method method) {
        List<Object[]> dataList = new ArrayList<>();
        Object dataMapObject = DATA_MAP.get(this.getClass().getName());
        if (dataMapObject != null) {
            Map<String, Object> dataMap = (Map<String, Object>) dataMapObject;
            Object data = dataMap.get(method.getName());
            if (data != null) {
                if (data instanceof Map) {
                    dataList.add(new Object[]{data});
                } else if (data instanceof List) {
                    List<Map<String, Object>> list = (List<Map<String, Object>>) data;
                    list.forEach(map -> dataList.add(new Object[]{map}));
                } else {
                    Log.error(CLASS_NAME, "data数据类型不允许为" + data.getClass());
                    throw new IllegalArgumentException("data数据类型不允许为" + data.getClass());
                }
            } else {
                Log.error(CLASS_NAME, "测试方法对应的data为null");
                throw new NullPointerException("测试方法对应的data为null,请检查data数据");
            }
        } else {
            Log.error(CLASS_NAME, "测试类对应的data为null");
            throw new NullPointerException("测试类对应的data为null,请检查data数据");
        }
        return dataList.iterator();
    }

    /**
     * @description: 单条测试数据初始化
     * @param  methodName 测试方法名
     * @return  Map<String, Object>
     * @author Sniper
     * @throws
     * @date 2020/4/27 15:25
     */
    protected Map<String, Object> sigleDataInit(String methodName) {
        Object dataMapObject = DATA_MAP.get(this.getClass().getName());
        if (dataMapObject != null) {
            Map<String, Object> dataMap = (Map<String, Object>) dataMapObject;
            Object data = dataMap.get(methodName);
            if (data != null) {
                if (data instanceof  Map) {
                    return (Map<String, Object>) data;
                } else {
                    Log.error(CLASS_NAME, "当前只允许Map类型数据,{}的数据类型为: {}", methodName, data.getClass());
                    throw new IllegalArgumentException("data数据类型不允许为" + data.getClass());
                }
            } else {
                Log.error(CLASS_NAME, "测试方法对应的data为null");
                throw new NullPointerException("测试方法对应的data为null,请检查data数据");
            }
        } else {
            Log.error(CLASS_NAME, "测试类对应的data为null");
            throw new NullPointerException("测试类对应的data为null,请检查data数据");
        }
    }

    /**
     * @description: 请求实体合并
     * @param  iTestContext testNG iTestContext
     * @param  entity       请求实体
     * @param  dataMap      测试数据
     * @return
     * @author Sniper
     * @throws
     * @date 2020/4/27 15:17
     */
    protected Entity entityMerge(ITestContext iTestContext, Entity entity, Map<String, Object> dataMap) {
        JSONObject contextJson = new JSONObject();
        Set<String> attrName = iTestContext.getAttributeNames();
        for (String name : attrName) {
            Object value  = iTestContext.getAttribute(name);
            contextJson.put(name, value);
        }
        Log.info(CLASS_NAME, "ITestContextAttribute: {}", contextJson.toJSONString());
        Log.info(CLASS_NAME, "Entity: {}", JSON.toJSONString(entity));
        Log.info(CLASS_NAME, "dataMap: {}", JSON.toJSONString(dataMap));
        parameter("ITestContextAttribute: ", contextJson.toJSONString());
        parameter("Entity: ", JSON.toJSONString(entity));
        parameter("Data: ", JSON.toJSONString(dataMap));

        JSONObject entityJSONObject = JSONObject.parseObject(JSONObject.toJSONString(entity,
                SerializerFeature.WriteMapNullValue));
        JSONObject dataJSONObject = dataMap == null ?
                null : JSONObject.parseObject(JSONObject.toJSONString(dataMap));
        if (dataJSONObject != null) {
            Set<String> keySet = entityJSONObject.keySet();
            for (String key : keySet) {
                //根据userId获取user对应的header
                if ("header".equals(key)) {
                    JSONObject header = entityJSONObject.getJSONObject(key);
                    Object headerKey = dataJSONObject.get("headerKey");
                    if (headerKey != null) {
                        Object userHeader = iTestContext.getAttribute(String.valueOf(headerKey));
                        if (userHeader != null) {
                            header.putAll((Map<String, Object>) userHeader);
                        }
                        Object dataHeader = dataJSONObject.get(key);
                        if (dataHeader != null) {
                            header.putAll((Map<String, Object>) dataHeader);
                        }
                    }
                } else if ("urlParamMap".equals(key) || "queryMap".equals(key) || "requestBody".equals(key) ||
                        "assertion".equals(key)) {
                    JSONObject partEntity = entityJSONObject.getJSONObject(key);
                    JSONObject partData = dataJSONObject.getJSONObject(key);
                    if (partEntity != null) {
                        Set<String> partEntityKeySet = partEntity.keySet();
                        for (String partEntityKey : partEntityKeySet) {
                            entityMerge(iTestContext, partEntity, partData, partEntityKey);
                        }
                    }
                } else {
                    entityMerge(iTestContext, entityJSONObject, dataJSONObject, key);
                }
            }
        }

        return JSON.toJavaObject(entityJSONObject, Entity.class);
    }

    /**
     * @description: 实体字段值合并,优先数据,其次testNG iTestContext
     * @param  iTestContext testNG iTestContext
     * @param  entity   局部请求实体
     * @param  data     局部测试数据
     * @param  key      字段
     * @return
     * @author Sniper
     * @throws
     * @date 2020/4/27 15:13
     */
    private void entityMerge(ITestContext iTestContext, JSONObject entity, JSONObject data, String key) {
        //合并iTestContext的entity字段值
        Object iTestContextValue = iTestContext.getAttribute(key);
        if (iTestContextValue != null) {
            entity.put(key, iTestContextValue);
        }
        //合并dataMap的entity字段值
        if (data !=null && data.containsKey(key)) {
            Object dataValue = data.get(key);
            entity.put(key, dataValue);
        }
    }

    /**
     * @description: 响应数据断言
     * @param response            response实体
     * @param entity              请求实体
     * @param assertionHandlerMap 断言Map
     * @return void
     * @throws
     * @author Sniper
     * @date 2019/10/17 17:01
     */
    protected void assertThat(Response response, Entity entity, HashMap<String, AssertionHandler> assertionHandlerMap) {
        Assertion assertion = entity.getAssertion();
        AssertionUtil.assertThat("StatusCode校验", response.getStatusCode(),
                assertion.getStatusCode());
        AssertionUtil.assertThat("Message校验", response.getMessage(),
                assertion.getMessage());
        ResponseDTO responseDTO = response.getResponseDTO();
        String action = assertion.getAction();
        if (!StringUtils.isEmpty(action)) {
            assertionHandlerMap.get(action).assertThat(responseDTO, entity);
        } else {
            AssertionUtil.assertThat(responseDTO, assertion);
        }
    }

}
