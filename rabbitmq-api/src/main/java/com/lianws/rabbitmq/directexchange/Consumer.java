package com.lianws.rabbitmq.directexchange;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;

/**
 * @Author: lianws
 * @Date: 2019/3/4 6:37
 */
public class Consumer {
    
    public static void main(String[] args) throws Exception{
        // 1.创建connnectionFactory
        ConnectionFactory connectionFactory=new ConnectionFactory();
        connectionFactory.setHost("127.0.0.1");
        connectionFactory.setPort(5672);
        connectionFactory.setVirtualHost("/");
        connectionFactory.setUsername("admin");
        connectionFactory.setPassword("admin@123");
        connectionFactory.setAutomaticRecoveryEnabled(true);
        connectionFactory.setNetworkRecoveryInterval(3000);

        // 2.通过connectionFactory创建connection
        Connection connection = connectionFactory.newConnection();

        // 3.通过connection创建channel
        Channel channel = connection.createChannel();
        // 4.声明
        String exchangeName="test_direct_exchange";
        String exchangeType="direct";
        String queueName="test_direct_queue";
        String routingKey="test.direct";
        // 声明交换机
        channel.exchangeDeclare(exchangeName, exchangeType,true,false,false,null);
        // 声明队列
        channel.queueDeclare(queueName,false,false,false,null);
        // 奖励交换机与queue的绑定关系
        channel.queueBind(queueName,exchangeName,routingKey);
        
        //5.收消息
        QueueingConsumer consumer=new QueueingConsumer(channel);
        // channel.basicQos(1)指该消费者在接收到队列里的消息但没有返回确认结果之前,它不会将新的消息分发给它。
        channel.basicQos(1);
        channel.basicConsume(queueName,true,consumer);
        while (true) {
            QueueingConsumer.Delivery delivery = consumer.nextDelivery();
            String msg=new String(delivery.getBody());
            System.out.println("Receive Msg is:"+msg);
        }
        
    }
}
