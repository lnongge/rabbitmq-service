package com.lianws.rabbitmq.confirmlistener;

import com.lianws.util.MqUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.QueueingConsumer;

/**
 * @Author: lianws
 * @Date: 2019/3/20 10:17
 */
public class Consumer {

    public static void main(String[] args) throws Exception {
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
        // 5.创建消费者
        QueueingConsumer consumer=new QueueingConsumer(channel);
        channel.basicConsume(queueName,true,consumer);
        // 6.消费
        while (true){
            QueueingConsumer.Delivery delivery = consumer.nextDelivery();
            System.out.println("收到:"+new String(delivery.getBody()));
        }
                
                
                
    }
}
