package com.xuehai.test.model;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.util.Map;

/**
 * @ClassName Response
 * @Description: TODO
 * @Author Sniper
 * @Date 2019/10/31 13:16
 */
@Data
public class Response {

    private int statusCode;
    private String message;
    private Map<String, String> header;
    @JSONField(name = "isError")
    private boolean isError;
    private ResponseDTO responseDTO;

    public Response(int statusCode, String message, Map<String, String> header, boolean isError, ResponseDTO responseDTO) {
        this.statusCode = statusCode;
        this.message = message;
        this.header = header;
        this.isError = isError;
        this.responseDTO = responseDTO;
    }
}
