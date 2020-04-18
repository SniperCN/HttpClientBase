package com.xh.test.model;

import lombok.Data;
import java.io.Serializable;
import java.util.Map;

/**
 * @ClassName MockResponse
 * @Description:    MockResponse实体类
 * @Author Sniper
 * @Date 2019/4/25 10:17
 */
@Data
public class MockResponse implements Serializable {
    private static final long serialVersionUID = -95856121179019268L;
    private Map<String, String> headers;
    private int statusCode;
    private String body;
    private int delaySeconds;
    
}
