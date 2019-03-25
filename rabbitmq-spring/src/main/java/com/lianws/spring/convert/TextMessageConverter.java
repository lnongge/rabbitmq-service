package com.lianws.spring.convert;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.support.converter.MessageConversionException;
import org.springframework.amqp.support.converter.MessageConverter;

/**
 * @Description: 自定义转换类
 * @Author: lianws
 * @Date: 2019/3/23 9:58
 */
public class TextMessageConverter implements MessageConverter {

    @Override
    public Message toMessage(Object object, MessageProperties messageProperties) throws MessageConversionException {
        return new Message(object.toString().getBytes(),messageProperties);
    }

    @Override
    public Object fromMessage(Message message) throws MessageConversionException {
        String contentType=message.getMessageProperties().getContentType();
        // 消息类型是文本类型返回string类型,否则直接返回消息体(byte[]类型)
        if(contentType!=null&&contentType.contains("text")){
            return new String(message.getBody());
        }
        return message.getBody();
    }
}
