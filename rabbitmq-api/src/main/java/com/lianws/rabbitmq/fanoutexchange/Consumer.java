package com.lianws.rabbitmq.fanoutexchange;

import com.lianws.util.MqUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.QueueingConsumer;

/**
 * @Author: lianws
 * @Date: 2019/3/18 19:38
 */
public class Consumer {
    public static void main(String[] args) throws Exception {
        // 2.创建连接
        Connection connection = MqUtils.newConnection();
        // 3.创建channel
        Channel channel = connection.createChannel();
        // 4.声明
        String exchangeName="test_fanout_exchange";
        String exchangeType="fanout";
        String queueName="test_fanout_queue";
        String routingKey="";// 不设置路由键
        // 声明交换机
        channel.exchangeDeclare(exchangeName,exchangeType,true,false,null);
        // 声明队列
        channel.queueDeclare(queueName,false,false,false,null);
        // 建立交换机与队列的绑定关系
        channel.queueBind(queueName,exchangeName,routingKey);

        QueueingConsumer consumer=new QueueingConsumer(channel);
        
        channel.basicConsume(queueName,true,consumer);
        
        while (true){
            QueueingConsumer.Delivery delivery = consumer.nextDelivery();
            String msg=new String(delivery.getBody());
            System.out.println("Receive Msg is:"+msg);
        }
        
                
    }
}
