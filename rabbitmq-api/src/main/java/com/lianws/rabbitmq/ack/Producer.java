package com.lianws.rabbitmq.ack;

import com.lianws.util.MqUtils;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description: ack&重回队列的生产者demo
 * @Author: lianws
 * @Date: 2019/3/20 13:08
 */
public class Producer {
    public static void main(String[] args) throws  Exception {

        Connection connection = MqUtils.newConnection();

        Channel channel = connection.createChannel();
        
        String exchangeName="test_ack_exchange";
        String routingKey="ack.save";
        
        channel.confirmSelect();
        
        for (int i = 0; i < 5; i++) {
            String msg="Hello RabbitMQ send ack&reQueue message!!"+i;
            Map<String,Object> map=new HashMap<>();
            map.put("num",i);
            AMQP.BasicProperties properties=new AMQP.BasicProperties().builder()
                    .headers(map)
                    .deliveryMode(2)
                    .contentEncoding("UTF-8")
                    .build();
            
            channel.basicPublish(exchangeName,routingKey,properties,msg.getBytes());
        }
        
        /**
         * 待思考:ack&reQueue如何与confirmListenr结合,这里结合的结果并不如我预想的?
         */
//        channel.addConfirmListener(new ConfirmListener() {
//            @Override
//            public void handleAck(long deliveryTag, boolean multiple) throws IOException {
//                System.out.println("Nack:"+deliveryTag);
//            }
//
//            @Override
//            public void handleNack(long deliveryTag, boolean multiple) throws IOException {
//                System.out.println("ack:"+deliveryTag);
//            }
//        });
       
    }
}
