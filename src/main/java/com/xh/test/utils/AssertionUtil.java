package com.xh.test.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPath;
import com.xh.test.model.Assertion;
import org.apache.commons.lang3.StringUtils;
import org.assertj.core.api.Assertions;
import java.util.List;

/**
 * @ClassName AssertionUtil
 * @Description:    断言工具类,基于assertJ
 * @Author Sniper
 * @Date 2019/4/18 17:10
 */
public class AssertionUtil {

    /**
     * @Description:        通用断言方法
     * @param actual        实际对象
     * @param assertion     断言命令行对象
     * @return void
     * @throws
     * @author Sniper
     * @date 2019/5/15 16:28
     */
    public static void assertThat(Object actual, Assertion assertion) {
        Object exceptDTO = assertion.getResponseDTO();
        Object actualDTO = actual;
        boolean isSort = assertion.isSort();
        List<String> jsonPathList = assertion.getJsonPathList();
        if (jsonPathList != null && jsonPathList.size() > 0) {
            for (String jsonPath : jsonPathList) {
                if (!StringUtils.isEmpty(jsonPath)) {
                    exceptDTO = JSONPath.eval(exceptDTO, jsonPath);
                    actualDTO = JSONPath.eval(actualDTO, jsonPath);
                    List<String> keys = assertion.getIncludeKeyMap().get(jsonPath);
                    if (keys != null && keys.size() > 0) {
                        assertThat(actualDTO, exceptDTO, keys, isSort);
                    } else {
                        assertThat(actualDTO, exceptDTO, isSort);
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
     * @param keys      断言字段
     * @param isSort    是否排序
     * @return void
     * @throws
     * @author Sniper
     * @date 2019/5/14 19:28
     */
    public static void assertThat(Object actual, Object except, List<String> keys, boolean isSort) {
        if (actual instanceof JSONObject && except instanceof JSONObject) {
            keys.forEach(key -> assertThat(((JSONObject) actual).get(key), ((JSONObject) except).get(key), isSort));
        }else if (actual instanceof JSONArray && except instanceof JSONArray) {
            JSONArray actualArray = (JSONArray) actual;
            JSONArray exceptArray = (JSONArray) except;
            AssertionUtil.assertThat("数据条数校验", actualArray.size(), exceptArray.size());
            if (isSort) {
                for (int i = 0; i < exceptArray.size(); i++) {
                    assertThat(actualArray.get(i), exceptArray.get(i), keys, true);
                }
            } else {
                actualArray.forEach(obj -> {
                    int index = exceptArray.indexOf(obj);
                    assertThat(obj, exceptArray.get(index), keys, false);
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
