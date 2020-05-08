package com.xh.test.model;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @ClassName Assertion
 * @Description: TODO
 * @Author Sniper
 * @Date 2020/4/19 22:10
 */
@Data
public class Assertion implements Serializable {
    private static final long serialVersionUID = -8101452868161781425L;
    private String action;
    private int statusCode;
    private String message;
    private ResponseDTO responseDTO;
    private List<String> jsonPathList;
    private Map<String, List<String>> includeKeyMap;
    @JSONField(name = "isSort")
    private boolean isSort;
}
