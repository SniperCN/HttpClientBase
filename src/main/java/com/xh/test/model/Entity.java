package com.xh.test.model;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import java.io.Serializable;
import java.util.Map;

/**
 * @ClassName Entity
 * @Description: 接口实体类
 * @Author Sniper
 * @Date 2019/3/12 16:01
 */
@Data
public class Entity implements Serializable {
    private static final long serialVersionUID = -3510610381656628283L;
    private String description;
    private String serverType;
    private String method;
    private String url;
    private Map<String, Object> urlParamMap;
    private Map<String, Object> header;
    private Map<String, Object> queryMap;
    private Map<String, Object> requestBody;
    private Assertion assertion;
    @JSONField(name = "isSign")
    private boolean isSign;
}
