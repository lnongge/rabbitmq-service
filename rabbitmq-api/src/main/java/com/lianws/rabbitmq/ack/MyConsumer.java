package com.lianws.rabbitmq.ack;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

import java.io.IOException;

/**
 * @Description: ack&重回队列自定义消费者
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
        System.out.println("deliveryTag:"+envelope.getDeliveryTag());
        System.out.println("messageBody:"+new String(body));

        try {
            Thread.sleep(2000); //为了便于观察结果
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
//        if((Integer)properties.getHeaders().get("num")==0){
//            // 第一个消息手动Nack并且重回队列(requeue=true)
//            channel.basicNack(envelope.getDeliveryTag(),false,true);
//        }else {
//            // 否则手动ack
//            channel.basicAck(envelope.getDeliveryTag(),false);
//        }

        channel.basicAck(envelope.getDeliveryTag(),false);
    }
}
