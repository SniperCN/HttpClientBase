package com.xuehai.test.base;

import com.xuehai.test.model.Entity;
import org.testng.ITestContext;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * @ClassName OperationInvoke
 * @Description: TODO
 * @Author Sniper
 * @Date 2019/10/21 17:07
 */
public class OperationInvoke {
    private static final String CLASS_NAME = OperationInvoke.class.getName();

    public static List<String> operationIterator(String className, String methodName, BaseClient baseClient, ITestContext iTestContext,
                                          Map<String, List<Entity>> map, HashMap<String, AssertionHandler> assertionMap) {
        List<String> list = new ArrayList<>();
        try {
            Class clazz = Class.forName(className);
            Constructor constructor = clazz.getDeclaredConstructor(BaseClient.class);
            Object object = constructor.newInstance(baseClient);
            Method method = clazz.getMethod(methodName, ITestContext.class, HashMap.class, Entity.class);
            List<Entity> entityList = map.get(methodName);
            if (entityList != null) {
                Iterator<Entity> iterator = entityList.iterator();
                while (iterator.hasNext()) {
                    Entity entity = iterator.next();
                    Object o = method.invoke(object, iTestContext, assertionMap, entity);
                    list.add(o.toString());
                }
            } else {
                throw new IllegalArgumentException("缺少" + className + "方法的DataProvider");
            }
        } catch (ClassNotFoundException | NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            Log.error(CLASS_NAME, "接口业务操作迭代失败", e);
        }
        return list;
    }

    public static List<String> operationIterator(String className, String methodName, BaseClient baseClient, ITestContext iTestContext,
                                                 Map<String, List<Entity>> map) {
        List<String> list = new ArrayList<>();
        try {
            Class clazz = Class.forName(className);
            Constructor constructor = clazz.getDeclaredConstructor(BaseClient.class);
            Object object = constructor.newInstance(baseClient);
            Method method = clazz.getMethod(methodName, ITestContext.class, Entity.class);
            List<Entity> entityList = map.get(methodName);
            if (entityList != null) {
                Iterator<Entity> iterator = entityList.iterator();
                while (iterator.hasNext()) {
                    Entity entity = iterator.next();
                    Object o = method.invoke(object, iTestContext, entity);
                    list.add(o.toString());
                }
            } else {
                throw new IllegalArgumentException("缺少" + className + "方法的DataProvider");
            }
        } catch (ClassNotFoundException | NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            Log.error(CLASS_NAME, "接口业务操作迭代失败", e);
        }
        return list;
    }

}
