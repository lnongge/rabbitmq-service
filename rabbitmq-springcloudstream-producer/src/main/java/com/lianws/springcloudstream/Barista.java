package com.lianws.springcloudstream;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.stereotype.Component;

/**
 * @Description: Barista接口是定义作为后面类的参数,这一接口定义了通道类型和名称
 * 通道类型决定了application会使用这一通道进行发送消息or从中接收消息
 * 通道名称是作为配置用的
 * @Author: lianws
 * @Date: 2019/3/28 18:01
 */
@Component
public interface Barista {
    // 1. 定义通道名称
    String OUTPUT_CHANNEL="output_channel";

    /**
     * 2. 定义通道类型
     * @Output 注解声明了它是一个输出类型(输出通道即发送消息)的通道,
     * 表明注入了一个名字为output_channel的输出通道.
     */
    @Output(Barista.OUTPUT_CHANNEL)
    MessageChannel logoutput();
}
