package com.xuehai.test.model;

import com.alibaba.fastjson.annotation.JSONField;
import java.util.Objects;

/**
 * @ClassName MockForward
 * @Description:    MockForward实体类
 * @Author Sniper
 * @Date 2019/4/25 10:18
 */
public class MockForward {
    @JSONField(name = "host")
    private String host;
    @JSONField(name = "port")
    private int port;
    @JSONField(name = "isSSL")
    private boolean isSSL;
    @JSONField(name = "delaySeconds")
    private int delaySeconds;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public boolean isSSL() {
        return isSSL;
    }

    public void setSSL(boolean isSSL) {
        this.isSSL = isSSL;
    }

    public int getDelaySeconds() {
        return delaySeconds;
    }

    public void setDelaySeconds(int delaySeconds) {
        this.delaySeconds = delaySeconds;
    }

    @Override
    public String toString() {
        return "MockForward{" +
                "host='" + host + '\'' +
                ", port=" + port +
                ", isSSL=" + isSSL +
                ", delaySeconds=" + delaySeconds +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MockForward that = (MockForward) o;
        return port == that.port &&
                isSSL == that.isSSL &&
                delaySeconds == that.delaySeconds &&
                Objects.equals(host, that.host);
    }

    @Override
    public int hashCode() {
        return Objects.hash(host, port, isSSL, delaySeconds);
    }
}
