package com.xuehai.test.model;

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
    private Map<String, Object> urlParam;
    private Map<String, String> header;
    private Map<String, Object> queryMap;
    private String requestBody;
    private String assertion;
    private boolean isSign;
    private boolean isMock;
    private MockDTO mockDTO;

}
