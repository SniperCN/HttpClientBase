package com.xuehai.base;

import com.xuehai.model.MockForward;
import com.xuehai.model.MockRequest;
import com.xuehai.model.MockResponse;
import org.apache.commons.lang3.StringUtils;
import org.mockserver.client.MockServerClient;
import org.mockserver.model.*;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName MockClient
 * @Description:    Mock客户端工具类
 * @Author Sniper
 * @Date 2019/4/24 20:47
 */
public class MockClient {

    private MockServerClient mockServerClient;

    /**
     * @description:            执行mock流程
     * @param client            MockServerClient
     * @param mockRequest       MockRequest实体
     * @param mockResponse      MockResponse实体
     * @param mockForward       MockForward实体
     * @return void
     * @throw
     * @author Sniper
     * @date 2019/4/26 15:06
     */
    public void doMock(MockServerClient client, MockRequest mockRequest, MockResponse mockResponse,
                              MockForward mockForward) {
        HttpRequest request = parseRequest(mockRequest);
        HttpResponse response = parseResponse(mockResponse);
        HttpForward forward = parseForward(mockForward);
        if (request != null && response != null && forward == null) {
            response(client, request, response);
        } else if (request != null && forward != null && response == null) {
            forward(client, request, forward);
        } else {
            throw new IllegalArgumentException("有效参数为有且仅有非空的(HttpRequest&&HttpResponse或HttpRequest&&HttpForward)");
        }
    }

    /**
     * @description:        mock响应数据
     * @param client        MockServerClient
     * @param request       HttpRequest
     * @param response      HttpResponse
     * @return void
     * @throw
     * @author Sniper
     * @date 2019/4/26 15:08
     */
    public void response(MockServerClient client, HttpRequest request, HttpResponse response) {
        client.when(request)
                .respond(response);
    }

    /**
     * @description:        mock接口重定向
     * @param client        MockServerClient
     * @param request       HttpRequest
     * @param forward       HttpForward
     * @return void
     * @throw
     * @author Sniper
     * @date 2019/4/26 15:15
     */
    public void forward(MockServerClient client, HttpRequest request, HttpForward forward) {
        client.when(request)
                .forward(forward);
    }

    /**
     * @description:        解析MockRequest实体
     * @param mockRequest
     * @return org.mockserver.model.HttpRequest
     * @throw
     * @author Sniper
     * @date 2019/4/26 15:16
     */
    public HttpRequest parseRequest(MockRequest mockRequest) {
        if (mockRequest != null) {
            HttpRequest httpRequest = new HttpRequest();
            String method = mockRequest.getMethod();
            Map<String, String> headers = mockRequest.getHeaders();
            String path = mockRequest.getPath();
            Map<String, Object> parameters = mockRequest.getParameters();
            String body = mockRequest.getBody();
            boolean isSSL = mockRequest.isSSL();
            if (!StringUtils.isEmpty(method)) {
                httpRequest.withMethod(method);
            }
            if (headers != null) {
                headers.forEach((key, value) -> httpRequest.withHeader(new Header(key, value)));
            }
            if (!StringUtils.isEmpty(path)) {
                httpRequest.withPath(path);
            }
            if (parameters != null) {
                parameters.forEach((key, value) ->
                        httpRequest.withQueryStringParameter(new Parameter(key, String.valueOf(value))));
            }
            if (!StringUtils.isEmpty(body)) {
                httpRequest.withBody(body);
            }
            httpRequest.withSecure(isSSL);
            return httpRequest;
        } else {
            return null;
        }
    }

    /**
     * @description:            解析MockResponse实体
     * @param mockResponse
     * @return org.mockserver.model.HttpResponse
     * @throw
     * @author Sniper
     * @date 2019/4/26 15:17
     */
    public HttpResponse parseResponse(MockResponse mockResponse) {
        if (mockResponse != null) {
            HttpResponse httpResponse = new HttpResponse();
            Map<String, String> headerList = mockResponse.getHeaders();
            int statusCode = mockResponse.getStatusCode();
            String body = mockResponse.getBody();
            int delaySeconds = mockResponse.getDelaySeconds();
            if (headerList != null) {
                headerList.forEach((key, value) -> httpResponse.withHeader(new Header(key, value)));
            }
            if (statusCode > 0) {
                httpResponse.withStatusCode(statusCode);
            }
            if (!StringUtils.isEmpty(body)) {
                httpResponse.withBody(body);
            }
            if (delaySeconds > 0) {
                httpResponse.withDelay(TimeUnit.SECONDS, delaySeconds);
            }
            return httpResponse;
        } else {
            return null;
        }
    }

    /**
     * @description:        解析MockForward实体
     * @param  mockForward
     * @return org.mockserver.model.HttpForward
     * @throw
     * @author Sniper
     * @date 2019/4/26 15:18
     */
    public HttpForward parseForward(MockForward mockForward) {
        if (mockForward != null) {
            HttpForward httpForward = new HttpForward();
            String host = mockForward.getHost();
            int port = mockForward.getPort();
            int delaySeconds = mockForward.getDelaySeconds();
            boolean isSSL = mockForward.isSSL();
            if (!StringUtils.isEmpty(host)) {
                httpForward.withHost(host);
            }
            if (port > 0) {
                httpForward.withPort(port);
            }
            if (delaySeconds > 0) {
                httpForward.withDelay(TimeUnit.SECONDS, delaySeconds);
            }
            if (isSSL) {
                httpForward.withScheme(HttpForward.Scheme.HTTPS);
            } else {
                httpForward.withScheme(HttpForward.Scheme.HTTP);
            }
            return httpForward;
        } else {
            return null;
        }
    }

    /**
     * @description:    实例化MockServerClient
     * @param host
     * @param port
     * @return org.mockserver.client.MockServerClient
     * @throw
     * @author Sniper
     * @date 2019/4/26 15:18
     */
    public MockServerClient start(String host, int port) {
        if (mockServerClient == null || !mockServerClient.isRunning()) {
            mockServerClient = new MockServerClient(host, port);
        }
        return mockServerClient;
    }

    /**
     * @description:     关闭MockServerClient
     * @return void
     * @throw
     * @author Sniper
     * @date 2019/4/26 15:18
     */
    public void close () {
        if (mockServerClient != null && mockServerClient.isRunning()) {
            mockServerClient.close();
            mockServerClient = null;
        }
    }
}
