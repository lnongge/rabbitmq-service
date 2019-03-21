package com.lianws.rabbitmq.limit;

import com.lianws.util.MqUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

/**
 * @Description: 限流的生产者demo
 * @Author: lianws
 * @Date: 2019/3/20 13:08
 */
public class Producer {
    public static void main(String[] args) throws  Exception {

        Connection connection = MqUtils.newConnection();

        Channel channel = connection.createChannel();
        
        String exchangeName="test_qos_exchange";
        String routingKey="qos.save";
        
        String msg="Hello RabbitMQ send qos message!!";
        for (int i = 0; i < 5; i++) {
            channel.basicPublish(exchangeName,routingKey,null,msg.getBytes());
        }
        
       
    }
}
