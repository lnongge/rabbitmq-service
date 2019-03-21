package com.lianws.rabbitmq.returnlistener;

import com.lianws.util.MqUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.QueueingConsumer;

/**
 * @Description: return消息机制的消费者demo
 * @Author: lianws
 * @Date: 2019/3/20 13:09
 */
public class Consumer {

    public static void main(String[] args) throws Exception {
        
        Connection connection = MqUtils.newConnection();

        Channel channel = connection.createChannel();

        String exchangeName="test_return_exchange";
        String exchangeType="topic";
        String routingKey="return.#";
        String queueName="test_return_queue";
        
        channel.exchangeDeclare(exchangeName, exchangeType,true);
        channel.queueDeclare(queueName,true,false,false,null);
        channel.queueBind(queueName,exchangeName,routingKey);

        QueueingConsumer consumer=new QueueingConsumer(channel);
        channel.basicConsume(queueName,true,consumer);
        
        while (true){
            QueueingConsumer.Delivery delivery = consumer.nextDelivery();
            System.out.println("Receive:"+new String(delivery.getBody()));
        }
    }
}
