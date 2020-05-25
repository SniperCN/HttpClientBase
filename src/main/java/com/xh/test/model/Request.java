package com.xh.test.model;

import lombok.Data;
import java.io.Serializable;
import java.util.Map;

/**
 * @ClassName Request
 * @Description: 请求核心数据实体
 * @Author Sniper
 * @Date 2019/10/31 13:32
 */
@Data
public class Request implements Serializable {
    private static final long serialVersionUID = 4969028561102132092L;
    private String url;
    private String method;
    private Map<String, Object> header;
    private Map<String, Object> body;

    public Request(String url, String method, Map<String, Object> header, Map<String, Object> body) {
        this.url = url;
        this.method = method;
        this.header = header;
        this.body = body;
    }

}
