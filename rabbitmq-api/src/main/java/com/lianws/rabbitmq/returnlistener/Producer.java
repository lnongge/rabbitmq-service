package com.lianws.rabbitmq.returnlistener;

import com.lianws.util.MqUtils;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ReturnListener;

import java.io.IOException;

/**
 * @Description: return消息机制的生产者demo
 * @Author: lianws
 * @Date: 2019/3/20 13:08
 */
public class Producer {
    public static void main(String[] args) throws  Exception {

        Connection connection = MqUtils.newConnection();

        Channel channel = connection.createChannel();
        
        String exchangeName="test_return_exchange";
        String routingKey="return.save";
        String routingKeyError="abc.save";
        
        String msg="Hello RabbitMQ send return message!!";
        channel.basicPublish(exchangeName,routingKeyError,true,null,msg.getBytes());
        
        // 监听return
        channel.addReturnListener(new ReturnListener() {
            @Override
            public void handleReturn(int replyCode, String replyText, String exchange, String routingKey, AMQP.BasicProperties properties, byte[] body) throws IOException {
                System.out.println("-----------Return Listener------------");
                System.out.println("replyCode:"+replyCode);
                System.out.println("replyText:"+replyText);
                System.out.println("exchange:"+exchange);
                System.out.println("routingKey:"+routingKey);
                System.out.println("messageProperties:"+properties);
                System.out.println("messageBody:"+new String(body));
            }
        });
    }
}
