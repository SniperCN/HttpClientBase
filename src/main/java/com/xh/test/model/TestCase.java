package com.xh.test.model;

import lombok.Data;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @ClassName TestCase
 * @Description: 测试用例实体类
 * @Author Sniper
 * @Date 2019/3/12 16:06
 */
@Data
public class TestCase implements Serializable {
    private static final long serialVersionUID = 7625328979939407351L;
    private String name;
    private String className;
    private List<Map<String, Entity>> entityList;

}
