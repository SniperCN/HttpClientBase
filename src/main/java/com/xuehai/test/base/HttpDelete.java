package com.xuehai.test.base;

import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;

import java.net.URI;

/**
 * @ClassName HttpDelete
 * @Description: 重写HttpDelete方法,支持请求传body
 * @Author Sniper
 * @Date 2019/3/14 14:58
 */
public class HttpDelete extends HttpEntityEnclosingRequestBase {

    public static final String METHOD_NAME = "DELETE";

    @Override
    public String getMethod() {
        return METHOD_NAME;
    }

    public HttpDelete(final String uri) {
        super();
        super.setURI(URI.create(uri));
    }

    public HttpDelete(final URI uri) {
        super();
        super.setURI(uri);
    }

    public HttpDelete() {
        super();
    }
}
