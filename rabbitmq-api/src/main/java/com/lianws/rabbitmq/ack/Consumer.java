package com.lianws.rabbitmq.ack;

import com.lianws.util.MqUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

/**
 * @Description: ack&重回队列的消费者demo
 * @Author: lianws
 * @Date: 2019/3/20 13:09
 */
public class Consumer {

    public static void main(String[] args) throws Exception {
        
        Connection connection = MqUtils.newConnection();

        Channel channel = connection.createChannel();

        String exchangeName="test_ack_exchange";
        String exchangeType="topic";
        String routingKey="ack.#";
        String queueName="test_ack_queue";
        
        channel.exchangeDeclare(exchangeName, exchangeType,true);
        channel.queueDeclare(queueName,true,false,false,null);
        channel.queueBind(queueName,exchangeName,routingKey);
        // 手工签收:必须autoAck=false
        channel.basicConsume(queueName,false,new MyConsumer(channel));
        
    }
}
