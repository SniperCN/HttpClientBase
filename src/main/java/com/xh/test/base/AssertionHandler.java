package com.xh.test.base;

import com.xh.test.model.Entity;
import com.xh.test.model.ResponseDTO;

/**
 * @ClassName AssertionHandler
 * @Description:    断言处理器
 * @Author Sniper
 * @Date 2019/4/18 13:48
 */
public interface AssertionHandler {
    
    void assertThat(ResponseDTO responseDTO, Entity entity);

}
