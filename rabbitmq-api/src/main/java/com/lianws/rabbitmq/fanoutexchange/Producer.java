package com.lianws.rabbitmq.fanoutexchange;

import com.lianws.util.MqUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

/**
 * @Author: lianws
 * @Date: 2019/3/18 19:40
 */
public class Producer {
    public static void main(String[] args) throws  Exception {
        // 2.创建connection
        Connection connection = MqUtils.newConnection();
        // 3.创建channel
        Channel channel = connection.createChannel();
        // 4.声明
        String exchangeName="test_fanout_exchange";
        // 5.发送
        String msg="Hello RabbitMq for Topic Exchange Message...";
        channel.basicPublish(exchangeName,"aaaa",null,msg.getBytes());
        
        // 释放资源
        MqUtils.close(channel,connection);
    }
}
