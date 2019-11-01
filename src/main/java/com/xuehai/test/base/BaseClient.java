package com.xuehai.test.base;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.xuehai.test.model.*;
import com.xuehai.test.utils.CommonUtil;
import com.xuehai.test.utils.FileUtil;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.*;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.mockserver.client.MockServerClient;
import org.testng.ITestContext;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @ClassName BaseClient
 * @Description:    HttpClient客户端
 * @Author Sniper
 * @Date 2019/3/14 15:01
 */
public class BaseClient {
    private static final String CLASS_NAME = BaseClient.class.getName();
    private static RequestConfig requestConfig;
    private static BaseClient instance;

    public static BaseClient getInstance() {
        if (instance == null) {
            instance = new BaseClient();
            loadHttpClientConfig();
        }
        return instance;
    }

    /**
     * @description: 加载httpClient配置
     * @return void
     * @throws
     * @author Sniper
     * @date 2019/11/1 10:03
     */
    public static void loadHttpClientConfig() {
        Map httpConfig = (Map) Configuration.getConfig().get("http-client");
        RequestConfig.Builder builder = RequestConfig.custom();
        httpConfig.forEach((key, value) -> {
            switch(key.toString()) {
                case "proxy" :
                    Map httpProxy = (Map) value;
                    Object host = httpProxy.get("host");
                    Object port = httpProxy.get("port");
                    if (host != null && port != null) {
                        HttpHost httpHost = new HttpHost((String) httpProxy.get("host"), (Integer) httpProxy.get("port"));
                        builder.setProxy(httpHost);
                    }
                    break;
                case "socket-timeout" :
                    if (value != null) {
                        builder.setSocketTimeout((Integer) value);
                    }
                    break;
                case "connect-timeout" :
                    if (value != null) {
                        builder.setConnectTimeout((Integer) value);
                    }
                    break;
                case "connection-request-timeout" :
                    if (value != null) {
                        builder.setConnectionRequestTimeout((Integer) value);
                    }
                    break;
                default : break;
            }
            requestConfig = builder.build();
        });
    }

    /**
     * @description:                发送http请求
     * @param entity                请求实体
     * @return java.lang.String
     * @throws
     * @author Sniper
     * @date 2019/4/18 10:50
     */
    public Response sendHttpRequest(ITestContext context, Entity entity) {
        Response response;
        MockServerClient mockServerClient = null;
        try {
            String method = entity.getMethod();
            String serverType = entity.getServerType();
            String url = entity.getUrl();
            Map<String, Object> urlParam = entity.getUrlParam();
            Map<String, Object> queryMap = entity.getQueryMap();
            Map<String, String> header = entity.getHeader();
            String requestBody = entity.getRequestBody();
            boolean isSign = entity.isSign();
            boolean isMock = entity.isMock();

            Map serverTypeMap = (Map) Configuration.getConfig().get("server-type");
            if(serverTypeMap != null) {
                String host = (String) serverTypeMap.get(serverType);
                url = host + url;
            } else {
                throw new IllegalArgumentException("config.yaml缺少serverType配置");
            }

            String protocol = (String) Configuration.getConfig().get("protocol");
            if (protocol != null) {
                if ("http".equals(protocol)) {
                    if (!url.startsWith("http://")) {
                        url = "http://" + url;
                    }
                } else if ("https".equals(protocol)) {
                    if (!url.startsWith("https://")) {
                        url = "https://" + url;
                    }
                } else {
                    throw new IllegalArgumentException("错误的协议配置");
                }
            } else {
                throw new IllegalArgumentException("config.yaml缺少protocol配置");
            }

            Pattern pattern = Pattern.compile("\\{.+?}");
            Matcher matcher = pattern.matcher(url);
            while (matcher.find()) {
                String temp = matcher.group();
                String dataKey = temp.replace("{", "").replace("}", "");
                if (urlParam != null && urlParam.size() > 0) {
                    Object value = urlParam.get(dataKey);
                    if (value != null && !"".equals(value)) {
                        url = url.replace(temp, String.valueOf(value));
                    }
                } else {
                    Object obj = context.getAttribute("urlParam");
                    if (obj != null) {
                        url = url.replace(temp, String.valueOf(((Map<String, Object>) obj).get(dataKey)));
                    }
                }
            }

            if (isSign) {
                url = CommonUtil.createRequestSignature(method, url, toQueryString(queryMap), requestBody,
                        entity.getHeader().get("Authorization"));
            } else {
                if (queryMap != null && queryMap.size() > 0) {
                    url = url + "?" + toQueryString(queryMap);
                }
            }

            if (isMock) {
                MockClient mockClient = new MockClient();
                Map mockClientConfig = (Map) Configuration.getConfig().get("mock-client");
                String host = (String) mockClientConfig.get("host");
                int port = (int) mockClientConfig.get("port");
                mockServerClient = mockClient.start(host, port);
                MockDTO mockDTO = entity.getMockDTO();
                mockClient.doMock(mockServerClient, mockDTO.getMockRequest(), mockDTO.getMockResponse(),
                        mockDTO.getMockForward());
            }

            switch (method.toUpperCase()) {
                case "GET":
                    response = sendHttpGet(url, header);
                    break;
                case "POST":
                    response = sendHttpPost(url, requestBody, header);
                    break;
                case "PUT":
                    response = sendHttpPut(url, requestBody, header);
                    break;
                case "DELETE":
                    response = sendHttpDelete(url, requestBody, header);
                    break;
                case "PATCH":
                    response = sendHttpPatch(url, requestBody, header);
                    break;
                default:
                    throw new IllegalArgumentException("未定义的Method类型: " + method);
            }
        } finally {
            if (mockServerClient != null ) {
                mockServerClient.close();
            }
        }
        return response;
    }

    public Response sendHttpGet(String httpUrl) {
        HttpGet httpGet = new HttpGet(httpUrl);
        return sendHttpRequest(httpGet, new HashMap<>());
    }

    public Response sendHttpGet(String httpUrl, Map<String, String> header) {
        HttpGet httpGet = new HttpGet(httpUrl);
        return sendHttpRequest(httpGet, header);
    }

    public Response sendHttpPost(String httpUrl) {
        HttpPost httpPost = new HttpPost(httpUrl);
        return sendHttpRequest(httpPost, new HashMap<>());
    }

    public Response sendHttpPost(String httpUrl, Map<String, String> header) {
        HttpPost httpPost = new HttpPost(httpUrl);
        return sendHttpRequest(httpPost, header);
    }

    public Response sendHttpPost(String httpUrl, String body, Map<String, String> header) {
        HttpPost httpPost = new HttpPost(httpUrl);
        StringEntity stringEntity = new StringEntity(body, "UTF-8");
        httpPost.setEntity(stringEntity);
        return sendHttpRequest(httpPost, header);
    }

    public Response sendHttpPost(String httpUrl, Map<String, File> map, Map<String, String> header) {
        HttpPost httpPost = new HttpPost(httpUrl);
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        for(Map.Entry<String, File> entry : map.entrySet()){
            String key = entry.getKey();
            File file = entry.getValue();
            FileBody fileBody = new FileBody(file);
            builder.addPart(key, fileBody);
        }
        HttpEntity reqEntity = builder.build();
        httpPost.setEntity(reqEntity);
        return sendHttpRequest(httpPost, header);
    }

    public Response sendHttpDelete(String httpUrl) {
        HttpDelete httpDelete = new HttpDelete(httpUrl);
        return sendHttpRequest(httpDelete, new HashMap<>());
    }

    public Response sendHttpDelete(String httpUrl, Map<String, String> header) {
        HttpDelete httpDelete = new HttpDelete(httpUrl);
        return sendHttpRequest(httpDelete, header);
    }

    public Response sendHttpDelete(String httpUrl, String body, Map<String, String> header) {
        HttpDelete httpDelete = new HttpDelete(httpUrl);
        StringEntity stringEntity = new StringEntity(body, "UTF-8");
        httpDelete.setEntity(stringEntity);
        return sendHttpRequest(httpDelete, header);
    }



    public Response sendHttpPut(String httpUrl) {
        HttpPut httpPut = new HttpPut(httpUrl);
        return sendHttpRequest(httpPut, new HashMap<>());
    }

    public Response sendHttpPut(String httpUrl, Map<String, String> header) {
        HttpPut httpPut = new HttpPut(httpUrl);
        return sendHttpRequest(httpPut, header);
    }

    public Response sendHttpPut(String httpUrl, String body, Map<String, String> header) {
        HttpPut httpPut = new HttpPut(httpUrl);
        StringEntity stringEntity = new StringEntity(body, "UTF-8");
        httpPut.setEntity(stringEntity);
        return sendHttpRequest(httpPut, header);
    }

    public Response sendHttpPatch(String httpUrl) {
        HttpPatch httpPatch = new HttpPatch(httpUrl);
        return sendHttpRequest(httpPatch, new HashMap<>());
    }

    public Response sendHttpPatch(String httpUrl, Map<String, String> header) {
        HttpPatch httpPatch = new HttpPatch(httpUrl);
        return sendHttpRequest(httpPatch, header);
    }

    public Response sendHttpPatch(String httpUrl, String body, Map<String, String> header) {
        HttpPatch httpPatch = new HttpPatch(httpUrl);
        StringEntity stringEntity = new StringEntity(body, "UTF-8");
        httpPatch.setEntity(stringEntity);
        return sendHttpRequest(httpPatch, header);
    }

    private Response sendHttpRequest(HttpRequestBase httpRequest, Map<String, String> header) {
        CloseableHttpClient httpClient = null;
        CloseableHttpResponse httpResponse = null;
        Response response;
        try {
            httpClient = HttpClients.createDefault();
            httpRequest.setConfig(requestConfig);
            for(Map.Entry<String, String> entry: header.entrySet()){
                String key=entry.getKey();
                String value=entry.getValue();
                httpRequest.setHeader(key, value);
            }
            String bodyInfo = null;
            HttpEntity httpEntity = null;
            if (httpRequest instanceof HttpPost) {
                httpEntity = ((HttpPost) httpRequest).getEntity();
            }
            if (httpRequest instanceof HttpDelete) {
                httpEntity = ((HttpDelete) httpRequest).getEntity();
            }
            if (httpRequest instanceof HttpPut) {
                httpEntity = ((HttpPut) httpRequest).getEntity();
            }
            if (httpEntity != null) {
                bodyInfo = EntityUtils.toString(httpEntity);
            }
            Request request = new Request(httpRequest.getRequestLine().getUri(), httpRequest.getMethod(),
                    parseHeader(httpRequest.getAllHeaders()), bodyInfo);
            Log.info(CLASS_NAME, "请求信息: {}", JSON.toJSONString(request, SerializerFeature.WriteMapNullValue));
            httpResponse = httpClient.execute(httpRequest);
            ResponseDTO responseDTO = JSON.toJavaObject(JSON.parseObject(EntityUtils.toString(httpResponse.getEntity(), "UTF-8")),
                    ResponseDTO.class);
            response = new Response(httpResponse.getStatusLine().getStatusCode(), httpResponse.getStatusLine().getReasonPhrase(),
                    parseHeader(httpResponse.getAllHeaders()), false, responseDTO);
            Log.info(CLASS_NAME, "响应信息: {}", JSON.toJSONString(response, SerializerFeature.WriteMapNullValue));
        } catch (IOException e) {
            response = new Response(0, e.getMessage(), null, true, null);
            Log.error(CLASS_NAME, "请求失败", e);

        } finally {
            try {
                if (httpResponse != null) {
                    httpResponse.close();
                }
                if (httpClient != null) {
                    httpClient.close();
                }
            }catch (IOException e) {
                Log.error(CLASS_NAME, "httpClient连接关闭失败", e);
            }
        }
        return response;
    }

    public void download(String httpUrl, String savePath) {
        CloseableHttpClient httpClient = null;
        CloseableHttpResponse response = null;
        try {
            httpClient = HttpClients.createDefault();
            HttpGet httpGet = new HttpGet(httpUrl);
            httpGet.setConfig(requestConfig);
            Request request = new Request(httpGet.getRequestLine().getUri(), httpGet.getMethod(),
                    parseHeader(httpGet.getAllHeaders()), null);
            Log.info(CLASS_NAME, "请求信息: {}", JSON.toJSONString(request, SerializerFeature.WriteMapNullValue));
            response = httpClient.execute(httpGet);
            HttpEntity httpEntity = response.getEntity();
            if (httpEntity != null) {
                FileUtil.write(httpEntity.getContent(), savePath);
            }
        } catch (IOException e) {
            Log.error(CLASS_NAME, "下载失败", e);
        } finally {
            try {
                if (response != null) {
                    response.close();
                }
                if (httpClient != null) {
                    httpClient.close();
                }
            } catch (IOException e) {
                Log.error(CLASS_NAME, "httpClient连接关闭失败", e);
            }
        }
    }

    /**
     * @description: Header[]转Map
     * @param headers   请求或响应头信息
     * @return java.util.Map<java.lang.String,java.lang.String>
     * @throws
     * @author Sniper
     * @date 2019/11/1 10:16
     */
    private static Map<String, String> parseHeader(Header[] headers) {
        Map<String, String> headerMap = new HashMap<>();
        for (Header header : headers) {
            headerMap.put(header.getName(), header.getValue());
        }
        return headerMap;
    }

    /**
     * @description: queryMap转queryString
     * @param map   Map
     * @return java.lang.String
     * @throws
     * @author Sniper
     * @date 2019/11/1 10:17
     */
    private String toQueryString(Map<String, Object> map) {
        StringBuilder stringBuilder = new StringBuilder();
        map.forEach((key, value) -> stringBuilder.append(key).append("=").append(value).append("&"));
        int lastIndex = stringBuilder.lastIndexOf("&");
        stringBuilder.replace(lastIndex, lastIndex + 1, "");
        return stringBuilder.toString();
    }

}
