package com.lianws.rabbitmq.limit;

import com.lianws.util.MqUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

/**
 * @Description: 限流的消费者demo
 * @Author: lianws
 * @Date: 2019/3/20 13:09
 */
public class Consumer {

    public static void main(String[] args) throws Exception {
        
        Connection connection = MqUtils.newConnection();

        Channel channel = connection.createChannel();

        String exchangeName="test_qos_exchange";
        String exchangeType="topic";
        String routingKey="qos.#";
        String queueName="test_qos_queue";
        
        channel.exchangeDeclare(exchangeName, exchangeType,true);
        channel.queueDeclare(queueName,true,false,false,null);
        channel.queueBind(queueName,exchangeName,routingKey);
        /**
         * 使用限流:
         * 第一步: 设置每次推送给消费者的消息条数 basicQos(1)
         * 第二步: autoAck设为false
         * 第三部: 在自定义consumer中做手动签收:channel.basicAck(...)
         */
        channel.basicQos(0,1,false);
        channel.basicConsume(queueName,false,new MyConsumer(channel));
        
    }
}
