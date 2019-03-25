package com.lianws.spring.convert;

/**
 * @Description:
 * @Author: lianws
 * @Date: 2019/3/24 17:53
 */
public class ConverterBody {
    private byte[] body;

    public ConverterBody() {
    }

    public ConverterBody(byte[] body) {
        this.body = body;
    }

    public byte[] getBody() {
        return body;
    }

    public void setBody(byte[] body) {
        this.body = body;
    }
}
