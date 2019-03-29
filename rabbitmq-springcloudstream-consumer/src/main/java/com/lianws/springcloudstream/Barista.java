package com.lianws.springcloudstream;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;
import org.springframework.stereotype.Component;

/**
 * @Description: Barista接口是定义作为后面类的参数,这一接口定义了通道类型和名称
 * 通道类型决定了application会使用这一通道进行发送消息or从中接收消息
 * 通道名称是作为配置用的
 * @Author: lianws
 * @Date: 2019/3/29 11:16
 */
@Component
public interface Barista {
    // 1.声明通道名称
    String INPUT_CHANNEL="input_channel";
    
    // @Input注解声明它是一个输入类型的通道,名字是Barista.INPUT_CHANNEL,也就是input_channel
    @Input(Barista.INPUT_CHANNEL)
    SubscribableChannel loginput();
    
}
