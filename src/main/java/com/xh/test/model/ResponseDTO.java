package com.xh.test.model;

import lombok.Data;
import java.io.Serializable;
import java.util.Map;

/**
 * @ClassName ResponseDTO
 * @Description: TODO
 * @Author Sniper
 * @Date 2019/10/31 11:16
 */
@Data
public class ResponseDTO implements Serializable {
    private static final long serialVersionUID = 8506232713902130149L;
    private int code;
    private String msg;
    private Map<String, Object> data;
    private String desc;

    public ResponseDTO(int code, String msg, Map<String, Object> data, String desc) {
        this.code = code;
        this.msg = msg;
        this.data = data;
        this.desc = desc;
    }

}
