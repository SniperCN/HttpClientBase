package com.xuehai.base;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName Assertion
 * @Description:    断言解析
 * @Author Sniper
 * @Date 2019/4/18 13:30
 */
public class Assertion {
    private JSONObject assertion;

    public Assertion(String commandJson) throws JSONException {
        assertion = JSONObject.parseObject(commandJson);
    }

    public String action() throws JSONException {
        return assertion.getString("action");
    }

    public int responseCode() throws JSONException {
        return assertion.getIntValue("responseCode");
    }

    public Object response() throws JSONException {
        return assertion.get("response");
    }

    public String jsonPath() throws JSONException {
        return assertion.getString("jsonPath");
    }

    public List<String> assertionKeys() throws JSONException {
        JSONArray assertionKeys = assertion.getJSONArray("assertionKeys");
        if (assertionKeys != null) {
            return assertionKeys.toJavaList(String.class);
        } else {
            return new ArrayList<>();
        }
    }

    public boolean isSort() throws JSONException {
        return assertion.getBoolean("isSort");
    }

}
