package com.xuehai.model;

import com.alibaba.fastjson.annotation.JSONField;
import java.util.Map;
import java.util.Objects;

/**
 * @ClassName Entity
 * @Description: 接口实体类
 * @Author Sniper
 * @Date 2019/3/12 16:01
 */
public class Entity {

    @JSONField(name = "description")
    private String description;
    @JSONField(name = "serverType")
    private String serverType;
    @JSONField(name = "method")
    private String method;
    @JSONField(name = "url")
    private String url;
    @JSONField(name = "urlParam")
    private Map<String, Object> urlParam;
    @JSONField(name = "header")
    private Map<String, String> header;
    @JSONField(name = "queryString")
    private String queryString;
    @JSONField(name = "requestBody")
    private String requestBody;
    @JSONField(name = "accessToken")
    private String accessToken;
    @JSONField(name = "assertion")
    private String assertion;
    @JSONField(name = "isSign")
    private boolean isSign;
    @JSONField(name = "isMock")
    private boolean isMock;
    @JSONField(name = "mockDTO")
    private MockDTO mockDTO;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getServerType() {
        return serverType;
    }

    public void setServerType(String serverType) {
        this.serverType = serverType;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Map<String, Object> getUrlParam() {
        return urlParam;
    }

    public void setUrlParam(Map<String, Object> urlParam) {
        this.urlParam = urlParam;
    }

    public Map<String, String> getHeader() {
        return header;
    }

    public void setHeader(Map<String, String> header) {
        this.header = header;
    }

    public String getQueryString() {
        return queryString;
    }

    public void setQueryString(String queryString) {
        this.queryString = queryString;
    }

    public String getRequestBody() {
        return requestBody;
    }

    public void setRequestBody(String requestBody) {
        this.requestBody = requestBody;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getAssertion() {
        return assertion;
    }

    public void setAssertion(String assertion) {
        this.assertion = assertion;
    }

    public boolean isSign() {
        return isSign;
    }

    public void setSign(boolean isSign) {
        this.isSign = isSign;
    }

    public boolean isMock() {
        return isMock;
    }

    public void setMock(boolean isMock) {
        this.isMock = isMock;
    }

    public MockDTO getMockDTO() {
        return mockDTO;
    }

    public void setMockDTO(MockDTO mockDTO) {
        this.mockDTO = mockDTO;
    }

    @Override
    public String toString() {
        return "Entity{" +
                "description='" + description + '\'' +
                ", serverType='" + serverType + '\'' +
                ", method='" + method + '\'' +
                ", url='" + url + '\'' +
                ", urlParam=" + urlParam +
                ", header=" + header +
                ", queryString='" + queryString + '\'' +
                ", requestBody='" + requestBody + '\'' +
                ", accessToken='" + accessToken + '\'' +
                ", assertion='" + assertion + '\'' +
                ", isSign=" + isSign +
                ", isMock=" + isMock +
                ", mockDTO=" + mockDTO +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Entity entity = (Entity) o;
        return  isSign == entity.isSign &&
                isMock == entity.isMock &&
                Objects.equals(description, entity.description) &&
                Objects.equals(serverType, entity.serverType) &&
                Objects.equals(method, entity.method) &&
                Objects.equals(url, entity.url) &&
                Objects.equals(urlParam, entity.urlParam) &&
                Objects.equals(header, entity.header) &&
                Objects.equals(queryString, entity.queryString) &&
                Objects.equals(requestBody, entity.requestBody) &&
                Objects.equals(accessToken, entity.accessToken) &&
                Objects.equals(assertion, entity.assertion) &&
                Objects.equals(mockDTO, entity.mockDTO);
    }

    @Override
    public int hashCode() {
        return Objects.hash(description, serverType, method, url, urlParam, header, queryString, requestBody, accessToken, assertion, isSign, isMock, mockDTO);
    }

}
