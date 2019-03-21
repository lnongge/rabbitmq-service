package com.lianws.rabbitmq.dlx;

import com.lianws.util.MqUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description: 死信队列的消费者demo
 * @Author: lianws
 * @Date: 2019/3/20 13:09
 */
public class Consumer {
    public static void main(String[] args) throws Exception {
        
        Connection connection = MqUtils.newConnection();

        Channel channel = connection.createChannel();
        
        // 这只是声明普通交换机,队列,绑定, 不是死信队列
        String exchangeName="test_dlx_exchange";
        String exchangeType="topic";
        String routingKey="dlx.#";
        String queueName="test_dlx_queue";
        
        channel.exchangeDeclare(exchangeName, exchangeType,true);
        /** 死信队列设置2步:
         * 1.设置arguments参数
         * 注:arguments参数要设置到声明的queue上,不要设置到exchange上!!!
         */
        Map<String,Object> arguments=new HashMap<>();
        arguments.put("x-dead-letter-exchange","dlx.exchange"); 
        
        channel.queueDeclare(queueName,true,false,false,arguments);
        channel.queueBind(queueName,exchangeName,routingKey);
        // 2.声明死信队列(同声明普通队列)
        channel.exchangeDeclare("dlx.exchange","topic",true);
        channel.queueDeclare("dlx.queue",true,false,false,null);
        channel.queueBind("dlx.queue","dlx.exchange","#");
        
        channel.basicConsume(queueName,true,new MyConsumer(channel));
    }
}
