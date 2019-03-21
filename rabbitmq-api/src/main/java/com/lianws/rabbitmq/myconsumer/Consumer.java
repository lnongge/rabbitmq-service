package com.lianws.rabbitmq.myconsumer;

import com.lianws.util.MqUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

/**
 * @Description: 自定义消费者的消费者demo
 * @Author: lianws
 * @Date: 2019/3/20 13:09
 */
public class Consumer {

    public static void main(String[] args) throws Exception {
        
        Connection connection = MqUtils.newConnection();

        Channel channel = connection.createChannel();

        String exchangeName="test_myconsumer_exchange";
        String exchangeType="topic";
        String routingKey="myconsumer.#";
        String queueName="test_myconsumer_queue";
        
        channel.exchangeDeclare(exchangeName, exchangeType,true);
        channel.queueDeclare(queueName,true,false,false,null);
        channel.queueBind(queueName,exchangeName,routingKey);
       // 使用自定义消费者
        channel.basicConsume(queueName,true,new MyConsumer(channel));
        
    }
}
