package com.lianws.rabbitmq.quickstart;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * @Author: lianws
 * @Date: 2019/3/4 6:34
 */
public class Producer {
    private static final String QUEUE_NAME="hello";
    public static void main(String[] args) throws Exception {
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
        
        
        // 4.通过channel发送消息
        channel.queueDeclare(QUEUE_NAME,false,false,false,null);
        // 4.通过channel发送消息
        String msg="Hello,RabbitMq!!!";
        for (int i = 0; i <5 ; i++) {
            channel.basicPublish("",QUEUE_NAME,null,msg.getBytes());
        }

        // 5.释放资源
        channel.close();
        connection.close();
        System.out.println("send success");
    }
}
