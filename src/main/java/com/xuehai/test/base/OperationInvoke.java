package com.xuehai.test.base;

import com.xuehai.test.model.Entity;
import com.xuehai.test.model.Response;
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

    /**
     * @description: 操作方法迭代器,支持断言
     * @param className         类名
     * @param methodName        方法名
     * @param baseClient        BaseClient
     * @param iTestContext      ITestContext
     * @param map               dataProvider迭代后的数据类型
     * @param assertionMap      断言Map
     * @return java.util.List<com.xuehai.test.model.Response>
     * @throws 
     * @author Sniper
     * @date 2019/11/1 13:40 
     */
    public static List<Response> operationIterator(String className, String methodName, BaseClient baseClient, ITestContext iTestContext,
                                                   Map<String, List<Entity>> map, HashMap<String, AssertionHandler> assertionMap)
            throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        List<Response> list = new ArrayList<>();
        Class clazz = Class.forName(className);
        Constructor constructor = clazz.getDeclaredConstructor(BaseClient.class);
        Object object = constructor.newInstance(baseClient);
        Method method = clazz.getMethod(methodName, ITestContext.class, Entity.class, HashMap.class);
        List<Entity> entityList = map.get(methodName);
        if (entityList != null) {
            Iterator<Entity> iterator = entityList.iterator();
            while (iterator.hasNext()) {
                Entity entity = iterator.next();
                Object o = method.invoke(object, iTestContext, entity, assertionMap);
                list.add((Response) o);
            }
        } else {
            throw new IllegalArgumentException("缺少" + className + "方法的DataProvider");
        }
        return list;
    }

    /**
     * @description: 操作方法迭代器
     * @param className         类名
     * @param methodName        方法名
     * @param baseClient        BaseClient
     * @param iTestContext      ITestContext
     * @param map               dataProvider迭代后的数据类型
     * @return java.util.List<com.xuehai.test.model.Response>
     * @throws
     * @author Sniper
     * @date 2019/11/1 10:22
     */
    public static List<Response> operationIterator(String className, String methodName, BaseClient baseClient, ITestContext iTestContext,
                                                 Map<String, List<Entity>> map)
            throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        List<Response> list = new ArrayList<>();
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
                list.add((Response) o);
            }
        } else {
            throw new IllegalArgumentException("缺少" + className + "方法的DataProvider");
        }
        return list;
    }

}
