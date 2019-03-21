package com.lianws.rabbitmq.confirmlistener;

import com.lianws.util.MqUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmListener;
import com.rabbitmq.client.Connection;

import java.io.IOException;

/**
 * @Author: lianws
 * @Date: 2019/3/20 10:17
 */
public class Producer {
    public static void main(String[] args) throws Exception{
        // 2.创建连接
        Connection connection = MqUtils.newConnection();
        // 3.创建channel
        Channel channel = connection.createChannel();
        // 4.声明
        String exchangeName="test_confirm_exchange";
        String exchangeType="topic";
        String queueName="test_confirm_queue";
        String routingKey="confirmlistener.#";

        channel.exchangeDeclare(exchangeName,exchangeType,true,false,false,null);
        channel.queueDeclare(queueName,true,false,false,null);
        channel.queueBind(queueName,exchangeName,routingKey);
        
        // 开启消息确认模式
        channel.confirmSelect();
        
        // 5.发送消息
        String msg="Hello RabbitMQ send confirmlistener message!!";
        for (int i = 0; i < 5; i++) {
            channel.basicPublish(exchangeName,routingKey,null,msg.getBytes());
        }
        
        // 6.添加确认监听
        channel.addConfirmListener(new ConfirmListener() {
            @Override
            public void handleAck(long deliveryTag, boolean multiple) throws IOException {
                System.out.println("----------Ack!--------------");
            }

            @Override
            public void handleNack(long deliveryTag, boolean multiple) throws IOException {
                System.out.println("----------No Ack!--------------");
            }
        });
        
    }
}
