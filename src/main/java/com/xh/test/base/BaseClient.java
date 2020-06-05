package com.xh.test.base;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.xh.test.model.*;
import com.xh.test.utils.CommonUtil;
import com.xh.test.utils.FileUtil;
import io.qameta.allure.Allure;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.client.config.AuthSchemes;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.*;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.File;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import static io.qameta.allure.Allure.parameter;

/**
 * @ClassName BaseClient
 * @Description:    HttpClient客户端
 * @Author Sniper
 * @Date 2019/3/14 15:01
 */
public class BaseClient {
    private static final String CLASS_NAME = BaseClient.class.getName();
    private static BaseClient instance = new BaseClient();
    private String protocol;

    private BaseClient(){}

    public static BaseClient getInstance() {
        return instance;
    }

    /**
     * @description: 加载httpClient配置
     * @return void
     * @throws
     * @author Sniper
     * @date 2019/11/1 10:03
     */
    private RequestConfig loadHttpClientConfig() {
        final RequestConfig[] requestConfig = new RequestConfig[1];
        Map httpConfig = (Map) Configuration.getConfig().get("http-client");
        if (httpConfig != null) {
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
                requestConfig[0] = builder.build();
            });
        }
        return requestConfig[0];
    }

    /**
     * @description:    发送http请求
     * @param entity    请求实体
     * @return Response
     * @throws
     * @author Sniper
     * @date 2019/4/18 10:50
     */
    public Response sendHttpRequest(Entity entity) {
        String method = entity.getMethod();
        String serverType = entity.getServerType();
        String url = entity.getUrl();
        Map<String, Object> urlParamMap = entity.getUrlParamMap();
        Map<String, Object> queryMap = entity.getQueryMap();
        Map<String, Object> header = entity.getHeader();
        String requestBody = JSON.toJSONString(entity.getRequestBody());
        boolean isSign = entity.isSign();

        Map serverTypeMap = (Map) Configuration.getConfig().get("server-type");
        if(serverTypeMap != null) {
            Map serverAttrMap = (Map) serverTypeMap.get(serverType);
            if (serverAttrMap != null) {
                String host = String.valueOf(serverAttrMap.get("host"));
                protocol = String.valueOf(serverAttrMap.get("protocol"));
                url = host + url;
            }
        } else {
            throw new NullPointerException("config.yaml缺少serverType配置");
        }

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
                throw new IllegalArgumentException("config.yaml错误的协议配置,serverType:" + serverType + ",protocol:" + protocol);
            }
        } else {
            throw new NullPointerException("config.yaml缺少protocol配置");
        }

        Pattern pattern = Pattern.compile("\\{.+?}");
        Matcher matcher = pattern.matcher(url);
        while (matcher.find()) {
            String temp = matcher.group();
            String dataKey = temp.replace("{", "").replace("}", "");
            Object value = urlParamMap.get(dataKey);
            if (value != null) {
                url = url.replace(temp, String.valueOf(value));
            }
        }

        if (isSign) {
            url = CommonUtil.createRequestSignature(method, url, toQueryString(queryMap), requestBody,
                    String.valueOf(entity.getHeader().get("Authorization")));
        } else {
            if (queryMap != null && queryMap.size() > 0) {
                url = url + "?" + toQueryString(queryMap);
            }
        }

        Log.info(CLASS_NAME, "TargetEntity<{}>: {}", entity.getDescription(), JSON.toJSONString(entity));
        Allure.addAttachment("TargetEntity<"+ entity.getDescription() + "> ", JSON.toJSONString(entity));

        switch (method.toUpperCase()) {
            case "GET":
                return sendHttpGet(url, header);
            case "POST":
                return sendHttpPost(url, requestBody, header);
            case "PUT":
                return sendHttpPut(url, requestBody, header);
            case "DELETE":
                return sendHttpDelete(url, requestBody, header);
            case "PATCH":
                return sendHttpPatch(url, requestBody, header);
            default:
                Log.error(CLASS_NAME, "未定义的Method类型: \"{}\"", method);
                throw new IllegalArgumentException("未定义的Method类型: \"" + method + "\"");
        }
    }

    private Response sendHttpGet(String httpUrl) {
        HttpGet httpGet = new HttpGet(httpUrl);
        return sendHttpRequest(httpGet, new HashMap<>());
    }

    private Response sendHttpGet(String httpUrl, Map<String, Object> header) {
        HttpGet httpGet = new HttpGet(httpUrl);
        return sendHttpRequest(httpGet, header);
    }

    private Response sendHttpPost(String httpUrl) {
        HttpPost httpPost = new HttpPost(httpUrl);
        return sendHttpRequest(httpPost, new HashMap<>());
    }

    private Response sendHttpPost(String httpUrl, Map<String, Object> header) {
        HttpPost httpPost = new HttpPost(httpUrl);
        return sendHttpRequest(httpPost, header);
    }

    private Response sendHttpPost(String httpUrl, String body, Map<String, Object> header) {
        HttpPost httpPost = new HttpPost(httpUrl);
        StringEntity stringEntity = new StringEntity(body, "UTF-8");
        httpPost.setEntity(stringEntity);
        return sendHttpRequest(httpPost, header);
    }

    private Response sendHttpPost(String httpUrl, Map<String, File> map, Map<String, Object> header) {
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

    private Response sendHttpDelete(String httpUrl) {
        HttpDelete httpDelete = new HttpDelete(httpUrl);
        return sendHttpRequest(httpDelete, new HashMap<>());
    }

    private Response sendHttpDelete(String httpUrl, Map<String, Object> header) {
        HttpDelete httpDelete = new HttpDelete(httpUrl);
        return sendHttpRequest(httpDelete, header);
    }

    private Response sendHttpDelete(String httpUrl, String body, Map<String, Object> header) {
        HttpDelete httpDelete = new HttpDelete(httpUrl);
        StringEntity stringEntity = new StringEntity(body, "UTF-8");
        httpDelete.setEntity(stringEntity);
        return sendHttpRequest(httpDelete, header);
    }



    private Response sendHttpPut(String httpUrl) {
        HttpPut httpPut = new HttpPut(httpUrl);
        return sendHttpRequest(httpPut, new HashMap<>());
    }

    private Response sendHttpPut(String httpUrl, Map<String, Object> header) {
        HttpPut httpPut = new HttpPut(httpUrl);
        return sendHttpRequest(httpPut, header);
    }

    private Response sendHttpPut(String httpUrl, String body, Map<String, Object> header) {
        HttpPut httpPut = new HttpPut(httpUrl);
        StringEntity stringEntity = new StringEntity(body, "UTF-8");
        httpPut.setEntity(stringEntity);
        return sendHttpRequest(httpPut, header);
    }

    private Response sendHttpPatch(String httpUrl) {
        HttpPatch httpPatch = new HttpPatch(httpUrl);
        return sendHttpRequest(httpPatch, new HashMap<>());
    }

    private Response sendHttpPatch(String httpUrl, Map<String, Object> header) {
        HttpPatch httpPatch = new HttpPatch(httpUrl);
        return sendHttpRequest(httpPatch, header);
    }

    private Response sendHttpPatch(String httpUrl, String body, Map<String, Object> header) {
        HttpPatch httpPatch = new HttpPatch(httpUrl);
        StringEntity stringEntity = new StringEntity(body, "UTF-8");
        httpPatch.setEntity(stringEntity);
        return sendHttpRequest(httpPatch, header);
    }

    private Response sendHttpRequest(HttpRequestBase httpRequest, Map<String, Object> header) {
        CloseableHttpClient httpClient = null;
        CloseableHttpResponse httpResponse = null;
        Response response = null;
        try {
            try {
                httpClient = httpClientInit(httpRequest.getURI().getScheme());
                httpRequest.setConfig(loadHttpClientConfig());
                for (Map.Entry<String, Object> entry : header.entrySet()) {
                    String key = entry.getKey();
                    String value = String.valueOf(entry.getValue());
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
                        parseHeader(httpRequest.getAllHeaders()), JSON.parseObject(bodyInfo, Map.class));
                Log.info(CLASS_NAME, "请求信息: {}", JSON.toJSONString(request, SerializerFeature.WriteMapNullValue));
                Allure.addAttachment("请求信息", JSON.toJSONString(request, SerializerFeature.WriteMapNullValue));
                httpResponse = httpClient.execute(httpRequest);
                String responseData = EntityUtils.toString(httpResponse.getEntity());
                ResponseDTO responseDTO = parseResponse(responseData);
                response = new Response(httpResponse.getStatusLine().getStatusCode(), httpResponse.getStatusLine().getReasonPhrase(),
                        parseHeader(httpResponse.getAllHeaders()), false, responseDTO);
                Log.info(CLASS_NAME, "响应信息: {}", JSON.toJSONString(response, SerializerFeature.WriteMapNullValue));
                Allure.addAttachment("响应信息", JSON.toJSONString(response, SerializerFeature.WriteMapNullValue));
            } finally {
                if (httpResponse != null) {
                    httpResponse.close();
                }
                if (httpClient != null) {
                    httpClient.close();
                }
            }
        } catch (IOException | JSONException e) {
            if (response == null) {
                response = new Response(0, e.getMessage(), null, true, null);
            }
            Log.error(CLASS_NAME, "http请求失败", e);
        }
        return response;
    }

    public void download(String httpUrl, String savePath) {
        CloseableHttpClient httpClient = null;
        CloseableHttpResponse response = null;
        try {
            try {
                httpClient = httpClientInit(httpUrl.startsWith("http") ? "http" : "https");
                HttpGet httpGet = new HttpGet(httpUrl);
                httpGet.setConfig(loadHttpClientConfig());
                Request request = new Request(httpGet.getRequestLine().getUri(), httpGet.getMethod(),
                        parseHeader(httpGet.getAllHeaders()), null);
                Log.info(CLASS_NAME, "请求信息: {}", JSON.toJSONString(request, SerializerFeature.WriteMapNullValue));
                response = httpClient.execute(httpGet);
                HttpEntity httpEntity = response.getEntity();
                if (httpEntity != null) {
                    FileUtil.write(httpEntity.getContent(), savePath);
                }
            } finally {
                if (response != null) {
                    response.close();
                }
                if (httpClient != null) {
                    httpClient.close();
                }
            }
        } catch (IOException e) {
            Log.error(CLASS_NAME, "文件下载失败", e);
        }
    }

    /**
     * @description: HttpClient初始化
     * @return org.apache.http.client.HttpClient
     * @throws
     * @author Sniper
     * @date 2020/3/26 13:01
     */
    private CloseableHttpClient httpClientInit(String protocol) {
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        if ("https".equals(protocol)) {
            return sslClient();
        }
        return httpClient;
    }

    /**
     * @description: 在调用SSL之前需要重写验证方法，取消检测SSL，创建ConnectionManager，添加Connection配置信息，支持https
     * @return org.apache.http.client.HttpClient
     * @throws 
     * @author Sniper
     * @date 2020/3/26 13:03
     */
    private CloseableHttpClient sslClient() {
        CloseableHttpClient closeableHttpClient = null;
        try {
            // 在调用SSL之前需要重写验证方法，取消检测SSL
            X509TrustManager trustManager = new X509TrustManager() {
                @Override public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
                @Override public void checkClientTrusted(X509Certificate[] xcs, String str) {}
                @Override public void checkServerTrusted(X509Certificate[] xcs, String str) {}
            };
            SSLContext ctx = SSLContext.getInstance(SSLConnectionSocketFactory.TLS);
            ctx.init(null, new TrustManager[] { trustManager }, null);
            SSLConnectionSocketFactory socketFactory = new SSLConnectionSocketFactory(ctx, NoopHostnameVerifier.INSTANCE);
            // 创建Registry
            RequestConfig requestConfig = RequestConfig.custom().setCookieSpec(CookieSpecs.STANDARD_STRICT)
                    .setExpectContinueEnabled(Boolean.TRUE).setTargetPreferredAuthSchemes(Arrays.asList(AuthSchemes.NTLM,AuthSchemes.DIGEST))
                    .setProxyPreferredAuthSchemes(Arrays.asList(AuthSchemes.BASIC)).build();
            Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
                    .register("http", PlainConnectionSocketFactory.INSTANCE)
                    .register("https",socketFactory).build();
            // 创建ConnectionManager，添加Connection配置信息
            PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
            closeableHttpClient = HttpClients.custom().setConnectionManager(connectionManager)
                    .setDefaultRequestConfig(requestConfig).build();
        } catch (KeyManagementException | NoSuchAlgorithmException e) {
            Log.error(CLASS_NAME, "初始化SSL Client失败", e);
        }
        return closeableHttpClient;
    }

    /**
     * @description: Header[]转Map
     * @param headers   请求或响应头信息
     * @return java.util.Map<java.lang.String,java.lang.String>
     * @throws
     * @author Sniper
     * @date 2019/11/1 10:16
     */
    private Map<String, Object> parseHeader(Header[] headers) {
        Map<String, Object> headerMap = new HashMap<>();
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

    private boolean isJSONObject (String responseData) {
        try {
            JSONObject.parseObject(responseData);
            return true;
        } catch (JSONException e) {
            return false;
        }
    }

    private boolean isJSONArray (String responseData) {
        try {
            JSONObject.parseArray(responseData);
            return true;
        } catch (JSONException e) {
            return false;
        }
    }

    private ResponseDTO parseResponse(String responseData) {
        if (isJSONObject(responseData)) {
            JSONObject responseJSONObject = JSONObject.parseObject(responseData);
            if (responseJSONObject.keySet().size() == 4 &&
                responseJSONObject.containsKey("code") &&
                responseJSONObject.containsKey("msg") &&
                responseJSONObject.containsKey("desc") &&
                responseJSONObject.containsKey("data")
            ) {
                return JSON.parseObject(responseData, ResponseDTO.class);
            } else {
                return new ResponseDTO(0, "", JSONObject.parseObject(responseData), "");
            }
        } else if (isJSONArray(responseData)) {
            return new ResponseDTO(0, "", JSONArray.parseArray(responseData), "");
        } else {
            return new ResponseDTO(0, "", responseData, "");
        }
    }

}
