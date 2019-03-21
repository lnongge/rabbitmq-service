package com.lianws.rabbitmq.directexchange;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * @Author: lianws
 * @Date: 2019/3/4 6:34
 */
public class Producer {
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
        String exchangeName="test_direct_exchange";
        String routingKey="test.direct";
        String msg="Hello RabbitMQ for Direct Exchange!";
        channel.basicPublish(exchangeName, routingKey,null,msg.getBytes());
        
        // TODO 5.释放资源
        System.out.println("send success");
    }
}
