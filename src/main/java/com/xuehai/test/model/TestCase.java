package com.xuehai.test.model;

import com.alibaba.fastjson.annotation.JSONField;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @ClassName TestCase
 * @Description: 测试用例实体类
 * @Author Sniper
 * @Date 2019/3/12 16:06
 */
public class TestCase {

    @JSONField(name = "name")
    private String name;
    @JSONField(name = "className")
    private String className;
    @JSONField(name = "entityList")
    private List<Map<String, Entity>> entityList;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public List<Map<String, Entity>> getEntityList() {
        return entityList;
    }

    public void setEntityList(List<Map<String, Entity>> entityList) {
        this.entityList = entityList;
    }

    @Override
    public String toString() {
        return "TestCase{" +
                "name='" + name + '\'' +
                ", className='" + className + '\'' +
                ", entityList=" + entityList +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TestCase testCase = (TestCase) o;
        return Objects.equals(name, testCase.name) &&
                Objects.equals(className, testCase.className) &&
                Objects.equals(entityList, testCase.entityList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, className, entityList);
    }

}
