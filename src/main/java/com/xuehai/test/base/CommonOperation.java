package com.xuehai.test.base;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.xuehai.test.model.Entity;
import com.xuehai.test.model.Response;
import com.xuehai.test.model.ResponseDTO;
import com.xuehai.test.utils.AssertionUtil;
import org.apache.commons.lang3.StringUtils;
import org.testng.ITestContext;
import java.util.HashMap;
import java.util.Set;
import static io.qameta.allure.Allure.*;

/**
 * @ClassName CommonOperation
 * @Description: TODO
 * @Author Sniper
 * @Date 2019/10/9 14:05
 */
public class CommonOperation {
    private static final String CLASS_NAME = CommonOperation.class.getName();
    private BaseClient baseClient;

    public CommonOperation(BaseClient baseClient) {
        this.baseClient = baseClient;
    }

    /**
     * @description: 发送http请求,并验证响应数据
     * @param context           testNG ITestContext
     * @param entity            请求实体
     * @param assertionMap      测试类断言Map<String, AssertionHandler>
     * @return void
     * @throws
     * @author Sniper
     * @date 2019/10/9 14:14
     */
    protected void sendHttpRequest(ITestContext context, Entity entity, HashMap<String, AssertionHandler> assertionMap) {
        assertion(sendHttpRequest(context, entity), assertionMap, entity);
    }

    /**
     * @description: 发送http请求
     * @param entity 请求实体
     * @return java.lang.String
     * @throws
     * @author Sniper
     * @date 2019/10/9 14:13
     */
    protected Response sendHttpRequest(ITestContext context, Entity entity) {
        JSONObject contextJson = new JSONObject();
        Set<String> attrName = context.getAttributeNames();
        for (String name : attrName) {
            Object value  = context.getAttribute(name);
            contextJson.put(name, value);
        }
        description(entity.getDescription());
        parameter("ITestContext: ", contextJson.toJSONString());
        parameter("Entity: ", JSON.toJSONString(entity));
        return baseClient.sendHttpRequest(context, entity);
    }

    /**
     * @description: 下载文件,保存本地
     * @param httpUrl   文件url
     * @param savePath  保存路径
     * @return void
     * @throws
     * @author Sniper
     * @date 2019/11/1 10:23
     */
    protected void download(String httpUrl, String savePath) {
        baseClient.download(httpUrl, savePath);
    }

    /**
     * @description: 响应code断言,入库断言
     * @param response            response实体
     * @param assertionMap        断言Map<action名称, AssertionHandler实现类>
     * @param entity              请求实体
     * @return void
     * @throws 
     * @author Sniper
     * @date 2019/10/17 17:01 
     */ 
    protected void assertion(Response response, HashMap<String, AssertionHandler> assertionMap, Entity entity) {
        try {
            if (response != null) {
                Assertion assertion = new Assertion(entity.getAssertion());
                AssertionUtil.assertThat("StatusCode校验", response.getStatusCode(),
                        assertion.statusCode());
                ResponseDTO responseDTO = response.getResponseDTO();
                String action = assertion.action();
                if (!StringUtils.isEmpty(action)) {
                    assertionMap.get(assertion.action()).assertion(responseDTO, entity);
                } else {
                    AssertionUtil.assertThat(responseDTO, assertion);
                }
            } else {
                throw new IllegalArgumentException("用例断言失败,接口响应信息为空");
            }
        } catch (JSONException e) {
            Log.error(CLASS_NAME, "用例断言失败", e);
        }
    }


}
