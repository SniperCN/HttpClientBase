package com.xuehai.test.model;

import java.util.Objects;

/**
 * @ClassName MockDTO
 * @Description: Mock实体类
 * @Author Sniper
 * @Date 2019/3/12 16:04
 */
public class MockDTO {

    private MockRequest mockRequest;
    private MockResponse mockResponse;
    private MockForward mockForward;

    public MockRequest getMockRequest() {
        return mockRequest;
    }

    public void setMockRequest(MockRequest mockRequest) {
        this.mockRequest = mockRequest;
    }

    public MockResponse getMockResponse() {
        return mockResponse;
    }

    public void setMockResponse(MockResponse mockResponse) {
        this.mockResponse = mockResponse;
    }

    public MockForward getMockForward() {
        return mockForward;
    }

    public void setMockForward(MockForward mockForward) {
        this.mockForward = mockForward;
    }

    @Override
    public String toString() {
        return "MockDTO{" +
                "mockRequest=" + mockRequest +
                ", mockResponse=" + mockResponse +
                ", mockForward=" + mockForward +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MockDTO mockDTO = (MockDTO) o;
        return Objects.equals(mockRequest, mockDTO.mockRequest) &&
                Objects.equals(mockResponse, mockDTO.mockResponse) &&
                Objects.equals(mockForward, mockDTO.mockForward);
    }

    @Override
    public int hashCode() {
        return Objects.hash(mockRequest, mockResponse, mockForward);
    }

}
