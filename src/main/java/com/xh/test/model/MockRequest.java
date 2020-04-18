package com.xh.test.model;

import lombok.Data;
import java.io.Serializable;
import java.util.Map;

/**
 * @ClassName MockRequest
 * @Description:    MockRequest实体类
 * @Author Sniper
 * @Date 2019/4/25 10:13
 */
@Data
public class MockRequest implements Serializable {
    private static final long serialVersionUID = -8685708636546934529L;
    private String method;
    private Map<String, String> headers;
    private String path;
    private Map<String, Object> parameters;
    private String body;
    private boolean isSSL;

}
