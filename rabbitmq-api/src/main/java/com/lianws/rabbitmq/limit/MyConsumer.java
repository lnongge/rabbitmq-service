package com.lianws.rabbitmq.limit;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

import java.io.IOException;

/**
 * @Description: 限流自定义消费者
 * @Author: lianws
 * @Date: 2019/3/20 13:50
 */
public class MyConsumer extends DefaultConsumer {
    
    private Channel channel;
    
    public MyConsumer(Channel channel) {
        super(channel);
        this.channel=channel;
    }

    @Override
    public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body)
            throws IOException {
        System.out.println("----------consumer message-----");
        System.out.println("consumerTag:"+consumerTag);
        System.out.println("envelope:"+envelope);
        System.out.println("messageProperties:"+properties);
        System.out.println("messageBody:"+new String(body));
        
        /* 参数1 deliveryTag,
         * 参数2:multiple 是否批量签收, 因为我们设置qos的prefetchCount=1,即每次只处理1条,故这个设为false,
         *      若我们在设置qos的prefetchCount>1,这个参数就可以设为true表批量签收
         */
        channel.basicAck(envelope.getDeliveryTag(),false);
    }


}
