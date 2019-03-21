package com.lianws.rabbitmq.dlx;

import com.lianws.util.MqUtils;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

/**
 * @Description: 死信队列的生产者demo
 * @Author: lianws
 * @Date: 2019/3/20 13:08
 */
public class Producer {
    public static void main(String[] args) throws  Exception {

        Connection connection = MqUtils.newConnection();

        Channel channel = connection.createChannel();
        
        String exchangeName="test_dlx_exchange";
        String routingKey="dlx.save";
        
        String msg="Hello RabbitMQ send DLX message!!";
        
        // 制造死信: 设置消息过期时间,让它过期就变成死信了
        AMQP.BasicProperties properties=new AMQP.BasicProperties().builder()
                .deliveryMode(2)
                .contentEncoding("UTF-8")
                .expiration("10000")
                .build();
        
        channel.basicPublish(exchangeName,routingKey,properties,msg.getBytes());
    }
}
