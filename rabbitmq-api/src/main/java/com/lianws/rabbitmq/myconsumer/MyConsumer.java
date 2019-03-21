package com.lianws.rabbitmq.myconsumer;

import com.rabbitmq.client.*;

import java.io.IOException;

/**
 * @Description:
 * @Author: lianws
 * @Date: 2019/3/20 13:50
 */
public class MyConsumer extends DefaultConsumer {

    
    public MyConsumer(Channel channel) {
        super(channel);
    }

    @Override
    public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body)
            throws IOException {
        System.out.println("----------consumer message-----");
        System.out.println("consumerTag:"+consumerTag);
        System.out.println("envelope:"+envelope);
        System.out.println("messageProperties:"+properties);
        System.out.println("messageBody:"+new String(body));
        
    }


}
