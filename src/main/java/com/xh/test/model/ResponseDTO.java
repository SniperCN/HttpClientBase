package com.xh.test.model;

import lombok.Data;

/**
 * @ClassName ResponseDTO
 * @Description: TODO
 * @Author Sniper
 * @Date 2019/10/31 11:16
 */
@Data
public class ResponseDTO {
    private int code;
    private String msg;
    private String data;
    private String desc;

    public ResponseDTO(int code, String msg, String data, String desc) {
        this.code = code;
        this.msg = msg;
        this.data = data;
        this.desc = desc;
    }

}
