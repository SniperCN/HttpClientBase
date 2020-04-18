package com.xh.test.model;

import lombok.Data;

import java.util.Map;

/**
 * @ClassName Request
 * @Description: TODO
 * @Author Sniper
 * @Date 2019/10/31 13:32
 */
@Data
public class Request {
    private String url;
    private String method;
    private Map<String, Object> header;
    private String body;

    public Request(String url, String method, Map<String, Object> header, String body) {
        this.url = url;
        this.method = method;
        this.header = header;
        this.body = body;
    }

}
