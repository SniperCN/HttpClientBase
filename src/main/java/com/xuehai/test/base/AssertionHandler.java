package com.xuehai.test.base;

import com.xuehai.test.model.Entity;

/**
 * @ClassName AssertionHandler
 * @Description:    断言处理器
 * @Author Sniper
 * @Date 2019/4/18 13:48
 */
public abstract class AssertionHandler {

    public abstract void assertion(Object response, Entity entity);

}
