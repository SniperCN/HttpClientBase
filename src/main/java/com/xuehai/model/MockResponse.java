package com.xuehai.model;

import com.alibaba.fastjson.annotation.JSONField;
import java.util.Map;
import java.util.Objects;

/**
 * @ClassName MockResponse
 * @Description:    MockResponse实体类
 * @Author Sniper
 * @Date 2019/4/25 10:17
 */
public class MockResponse {

    @JSONField(name = "headers")
    private Map<String, String> headers;
    @JSONField(name = "statusCode")
    private int statusCode;
    @JSONField(name = "body")
    private String body;
    @JSONField(name = "delaySeconds")
    private int delaySeconds;

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaderList(Map<String, String> headers) {
        this.headers = headers;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public int getDelaySeconds() {
        return delaySeconds;
    }

    public void setDelaySeconds(int delaySeconds) {
        this.delaySeconds = delaySeconds;
    }

    @Override
    public String toString() {
        return "MockResponse{" +
                "headers=" + headers +
                ", statusCode=" + statusCode +
                ", body='" + body + '\'' +
                ", delaySeconds=" + delaySeconds +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MockResponse that = (MockResponse) o;
        return statusCode == that.statusCode &&
                delaySeconds == that.delaySeconds &&
                Objects.equals(headers, that.headers) &&
                Objects.equals(body, that.body);
    }

    @Override
    public int hashCode() {
        return Objects.hash(headers, statusCode, body, delaySeconds);
    }
    
}
