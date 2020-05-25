package com.xh.test.model;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import java.io.Serializable;
import java.util.Map;
import java.util.Set;

/**
 * @ClassName Assertion
 * @Description: 断言实体
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
    private Set<String> jsonPathList;
    private Map<String, Set<String>> includeKeyMap;
    private Map<String, Set<String>> excludeKeyMap;
    @JSONField(name = "isSort")
    private boolean isSort;
}
