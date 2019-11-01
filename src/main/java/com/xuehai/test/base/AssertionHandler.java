package com.xuehai.test.base;

import com.xuehai.test.model.Entity;
import com.xuehai.test.model.ResponseDTO;

/**
 * @ClassName AssertionHandler
 * @Description:    断言处理器
 * @Author Sniper
 * @Date 2019/4/18 13:48
 */
public interface AssertionHandler {
    
    void assertion(ResponseDTO responseDTO, Entity entity);

}
