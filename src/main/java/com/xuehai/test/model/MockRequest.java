package com.xuehai.test.model;

import com.alibaba.fastjson.annotation.JSONField;
import java.util.Map;
import java.util.Objects;

/**
 * @ClassName MockRequest
 * @Description:    MockRequest实体类
 * @Author Sniper
 * @Date 2019/4/25 10:13
 */
public class MockRequest {

    @JSONField(name = "method")
    private String method;
    @JSONField(name = "headers")
    private Map<String, String> headers;
    @JSONField(name = "path")
    private String path;
    @JSONField(name = "parameters")
    private Map<String, Object> parameters;
    @JSONField(name = "body")
    private String body;
    @JSONField(name = "isSSL")
    private boolean isSSL;

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaderList(Map<String, String> headers) {
        this.headers = headers;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Map<String, Object> getParameters() {
        return parameters;
    }

    public void setParameterList(Map<String, Object> parameters) {
        this.parameters = parameters;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public boolean isSSL() {
        return isSSL;
    }

    public void setSSL(boolean isSSL) {
        this.isSSL = isSSL;
    }

    @Override
    public String toString() {
        return "MockRequest{" +
                "method='" + method + '\'' +
                ", headers=" + headers +
                ", path='" + path + '\'' +
                ", parameters=" + parameters +
                ", body='" + body + '\'' +
                ", isSSL=" + isSSL +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MockRequest that = (MockRequest) o;
        return isSSL == that.isSSL &&
                Objects.equals(method, that.method) &&
                Objects.equals(headers, that.headers) &&
                Objects.equals(path, that.path) &&
                Objects.equals(parameters, that.parameters) &&
                Objects.equals(body, that.body);
    }

    @Override
    public int hashCode() {
        return Objects.hash(method, headers, path, parameters, body, isSSL);
    }

}
