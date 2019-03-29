package com.lianws.springcloudstream;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;

/**
 * @Description:
 * @Author: lianws
 * @Date: 2019/3/29 11:36
 */
@EnableBinding(Barista.class)
@Service
public class RabbitmqReceiver {
    
    @StreamListener(Barista.INPUT_CHANNEL)
    public void receiver(Message message) throws Exception{
        Channel channel = (Channel)message.getHeaders().get(AmqpHeaders.CHANNEL);
        Long deliveryTag = (Long) message.getHeaders().get(AmqpHeaders.DELIVERY_TAG);
        System.out.println("Input Stream 1接收消息:"+message);
        System.out.println("消费完毕--------------");
        channel.basicAck(deliveryTag,false);
    }
        
}
