package com.lianws.rabbitmq.quickstart;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;

/**
 * @Author: lianws
 * @Date: 2019/3/4 6:37
 */
public class Consumer {
    private static final String QUEUE_NAME="hello";
    
    public static void main(String[] args) throws Exception{
        // 1.创建connnectionFactory
        ConnectionFactory connectionFactory=new ConnectionFactory();
        connectionFactory.setHost("127.0.0.1");
        connectionFactory.setPort(5672);
        connectionFactory.setVirtualHost("/");
        connectionFactory.setUsername("admin");
        connectionFactory.setPassword("admin@123");

        // 2.通过connectionFactory创建connection
        Connection connection = connectionFactory.newConnection();

        // 3.通过connection创建channel
        Channel channel = connection.createChannel();
        // 4.声明(创建)一个队列
        channel.queueDeclare(QUEUE_NAME,false,false,false,null);
        // 5.创建消费者
        QueueingConsumer consumer=new QueueingConsumer(channel);
        channel.basicConsume(QUEUE_NAME,true,consumer);
        // 4.接收消息
        while (true){
            QueueingConsumer.Delivery delivery = consumer.nextDelivery();
            System.out.println("Receive Message :"+new String(delivery.getBody()));
        }
    }
}
