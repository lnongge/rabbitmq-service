package com.lianws.springboot.consumer;

import com.lianws.springboot.entity.Order;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @Description: 接收类
 * @Author: lianws
 * @Date: 2019/3/25 16:47
 */

@Component
public class RabbitReceiver {
    
    
    @RabbitListener(
            bindings = @QueueBinding(
            value=@Queue(value = "queue-1",durable = "true"),
            exchange = @Exchange(value = "exchange-1",type = "topic",durable = "true",ignoreDeclarationExceptions = "true"),
            key = "springboot.*")
    )
    @RabbitHandler
    public void onMessage(Message message,Channel channel)throws Exception{
        System.out.println("------------springboot consumer------------");
        System.out.println("消费端Payload:"+message.getPayload());
        Long deliveryTag = (Long) message.getHeaders().get(AmqpHeaders.DELIVERY_TAG);
        //手工签收
        channel.basicAck(deliveryTag,false);

    }
    /**
     * @Description 接收的消息是java对象
     * @Param [order, headers, channel]
     * @return void
     * @Date 20:21 2019/3/25
     * @Author lianws
     **/
    @RabbitListener(
            bindings = @QueueBinding(
                    value=@Queue(value = "${spring.rabbitmq.listener.order.queue.name}",
                            durable = "${spring.rabbitmq.listener.order.queue.durable}"),
                    exchange = @Exchange(value = "${spring.rabbitmq.listener.order.exchange.name}",
                            type = "${spring.rabbitmq.listener.order.exchange.type}",
                            durable = "${spring.rabbitmq.listener.order.exchange.durable}",
                            ignoreDeclarationExceptions = "${spring.rabbitmq.listener.order.ignoreDeclarationException}"),
                    key = "${spring.rabbitmq.listener.order.key}")
    )
    @RabbitHandler
    public void onOrderMessage(@Payload Order order, @Headers Map<String,Object> headers,
                               Channel channel) throws Exception {
        System.out.println("------------springboot consumer------------");
        System.out.println("消费端order:" + order.getId()+",orderName:"+order.getName());
        Long deliveryTag = (Long) headers.get(AmqpHeaders.DELIVERY_TAG); 
        //手工签收
        channel.basicAck(deliveryTag, false);

    }
        
}
