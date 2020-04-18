package com.xh.test.model;

import lombok.Data;
import java.io.Serializable;

/**
 * @ClassName MockDTO
 * @Description: Mock实体类
 * @Author Sniper
 * @Date 2019/3/12 16:04
 */
@Data
public class MockDTO implements Serializable {
    private static final long serialVersionUID = 4087037576433838322L;
    private MockRequest mockRequest;
    private MockResponse mockResponse;
    private MockForward mockForward;

}
