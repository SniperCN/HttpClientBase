package com.xh.test.model;

import lombok.Data;
import java.io.Serializable;

/**
 * @ClassName MockForward
 * @Description:    MockForward实体类
 * @Author Sniper
 * @Date 2019/4/25 10:18
 */
@Data
public class MockForward implements Serializable {
    private static final long serialVersionUID = -7658385389256247520L;
    private String host;
    private int port;
    private boolean isSSL;
    private int delaySeconds;

}
