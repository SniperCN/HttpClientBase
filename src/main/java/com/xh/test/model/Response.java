package com.xh.test.model;

import lombok.Data;
import java.io.Serializable;
import java.util.Map;

/**
 * @ClassName Response
 * @Description: TODO
 * @Author Sniper
 * @Date 2019/10/31 13:16
 */
@Data
public class Response implements Serializable {
    private static final long serialVersionUID = -4708760907007715009L;
    private int statusCode;
    private String message;
    private Map<String, Object> header;
    private boolean isError;
    private ResponseDTO responseDTO;

    public Response(int statusCode, String message, Map<String, Object> header, boolean isError, ResponseDTO responseDTO) {
        this.statusCode = statusCode;
        this.message = message;
        this.header = header;
        this.isError = isError;
        this.responseDTO = responseDTO;
    }
}
