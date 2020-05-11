package com.xh.test.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPath;
import com.xh.test.model.Assertion;
import org.apache.commons.lang3.StringUtils;
import org.assertj.core.api.Assertions;
import java.util.Map;
import java.util.Set;

/**
 * @ClassName AssertionUtil
 * @Description:    断言工具类,基于assertJ
 * @Author Sniper
 * @Date 2019/4/18 17:10
 */
public class AssertionUtil {

    /**
     * @Description:        通用断言方法
     * @param actualDTO     实际对象
     * @param assertion     断言命令行对象
     * @return void
     * @throws
     * @author Sniper
     * @date 2019/5/15 16:28
     */
    public static void assertThat(Object actualDTO, Assertion assertion) {
        Object exceptDTO = assertion.getResponseDTO();
        boolean isSort = assertion.isSort();
        Set<String> jsonPathList = assertion.getJsonPathList();
        if (jsonPathList != null && jsonPathList.size() > 0) {
            for (String jsonPath : jsonPathList) {
                System.out.println(jsonPath);
                if (!StringUtils.isEmpty(jsonPath)) {
                    Object partExceptDTO = JSONPath.eval(exceptDTO, jsonPath);
                    Object partActualDTO = JSONPath.eval(actualDTO, jsonPath);
                    Map<String, Set<String>> includeMap = assertion.getIncludeKeyMap();
                    Map<String, Set<String>> excludeMap = assertion.getExcludeKeyMap();
                    if (includeMap != null && includeMap.size() > 0) {
                        //断言指定字段
                        Set<String> includeKeys = assertion.getIncludeKeyMap().get(jsonPath);
                        if (includeKeys != null && includeKeys.size() > 0) {
                            assertThat(partActualDTO, partExceptDTO, includeKeys, isSort);
                        }
                    } else if (excludeMap != null) {
                        //断言排除指定字段
                        Set<String> excludeKeys = assertion.getExcludeKeyMap().get(jsonPath);
                        if (excludeKeys != null && excludeKeys.size() > 0) {
                            assertThat(partActualDTO, partExceptDTO, isSort, excludeKeys);
                        }
                    } else {
                        assertThat(partActualDTO, partExceptDTO, isSort);
                    }
                }
            }
        } else {
            assertThat(actualDTO, exceptDTO, isSort);
        }
    }

    /**
     * @Description:    通用部分字段断言方法
     * @param actual    实际对象
     * @param except    预期对象
     * @param includeKeys   指定断言字段
     * @param isSort    是否排序
     * @return void
     * @throws
     * @author Sniper
     * @date 2019/5/14 19:28
     */
    public static void assertThat(Object actual, Object except, Set<String> includeKeys, boolean isSort) {
        if (actual instanceof JSONObject && except instanceof JSONObject) {
            AssertionUtil.assertThat("字段个数校验", ((JSONObject) actual).keySet().size(),
                    ((JSONObject) except).keySet().size());
            includeKeys.forEach(key -> assertThat(((JSONObject) actual).get(key),
                    ((JSONObject) except).get(key), isSort));
        }else if (actual instanceof JSONArray && except instanceof JSONArray) {
            JSONArray actualArray = (JSONArray) actual;
            JSONArray exceptArray = (JSONArray) except;
            AssertionUtil.assertThat("数据条数校验", actualArray.size(), exceptArray.size());
            if (isSort) {
                for (int i = 0; i < exceptArray.size(); i++) {
                    assertThat(actualArray.get(i), exceptArray.get(i), includeKeys, true);
                }
            } else {
                actualArray.forEach(obj -> {
                    int index = exceptArray.indexOf(obj);
                    assertThat(obj, exceptArray.get(index), includeKeys, false);
                });
            }
        } else {
            assertThat("基本类型或对象数据断言", actual, except);
        }
    }

    /**
     * @Description:    通用部分字段断言方法
     * @param actual    实际对象
     * @param except    预期对象
     * @param excludeKeys   排除断言字段
     * @param isSort    是否排序
     * @return void
     * @throws
     * @author Sniper
     * @date 2019/5/14 19:28
     */
    public static void assertThat(Object actual, Object except, boolean isSort, Set<String> excludeKeys) {
        if (actual instanceof JSONObject && except instanceof JSONObject) {
            AssertionUtil.assertThat("字段个数校验", ((JSONObject) actual).keySet().size(),
                    ((JSONObject) except).keySet().size());
            Set<String> keys = ((JSONObject) except).keySet();
            keys.forEach(key -> {
                if (!excludeKeys.contains(key)) {
                    assertThat(((JSONObject) actual).get(key), ((JSONObject) except).get(key), isSort);
                }
            });

        }else if (actual instanceof JSONArray && except instanceof JSONArray) {
            JSONArray actualArray = (JSONArray) actual;
            JSONArray exceptArray = (JSONArray) except;
            AssertionUtil.assertThat("数据条数校验", actualArray.size(), exceptArray.size());
            if (isSort) {
                for (int i = 0; i < exceptArray.size(); i++) {
                    assertThat(actualArray.get(i), exceptArray.get(i), true, excludeKeys);
                }
            } else {
                actualArray.forEach(obj -> {
                    int index = exceptArray.indexOf(obj);
                    assertThat(obj, exceptArray.get(index), false, excludeKeys);
                });
            }
        } else {
            assertThat("基本类型或对象数据断言", actual, except);
        }
    }

    /**
     * @Description:    通用全量数据断言
     * @param actual    实际对象
     * @param except    预期对象
     * @param isSort    是否排序
     * @return void
     * @throws
     * @author Sniper
     * @date 2019/5/14 19:28
     */
    public static void assertThat(Object actual, Object except, boolean isSort) {
        if (actual instanceof JSONObject && except instanceof JSONObject) {
            ((JSONObject) actual).forEach((key, value) -> assertThat(value, ((JSONObject) except).get(key), isSort));
        }else if (actual instanceof JSONArray && except instanceof JSONArray) {
            JSONArray actualArray = (JSONArray) actual;
            JSONArray exceptArray = (JSONArray) except;
            assertThat("数据条数校验", actualArray.size(), exceptArray.size());
            if (isSort) {
                for (int i = 0; i < exceptArray.size(); i++) {
                    assertThat(actualArray.get(i), exceptArray.get(i), true);
                }
            } else {
                actualArray.forEach(obj -> {
                    int index = exceptArray.indexOf(obj);
                    assertThat(obj, exceptArray.get(index), false);
                });
            }
        } else {
            assertThat("基本类型或对象数据断言", actual, except);
        }
    }

    /**
     * @Description:    对象equals断言
     * @param description   断言描述
     * @param actual        实际结果
     * @param expect        预期结果
     * @return void
     * @throws
     * @author Sniper
     * @date 2019/4/19 10:12
     */
    public static void assertThat(String description, Object actual, Object expect) {
        Assertions.assertThat(actual)
                .as(description)
                .withFailMessage("Expect:%s, Actual:%s", expect, actual)
                .isEqualTo(expect);
    }

    /**
     * @Description:        条件断言
     * @param description   断言描述
     * @param condition     条件
     * @return void
     * @throws
     * @author Sniper
     * @date 2019/5/14 19:23
     */
    public static void assertThat(String description, boolean condition) {
        Assertions.assertThat(condition)
                .as(description)
                .withFailMessage("False")
                .isTrue();
    }

}
