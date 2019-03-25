package com.lianws.springboot.producer;

/**
 * @Description: 发送消息工具类
 * @Author: lianws
 * @Date: 2019/3/25 12:56
 */

import com.lianws.springboot.entity.Order;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component 
public class RabbitSender {
    @Autowired
    private RabbitTemplate rabbitTemplate;
    final RabbitTemplate.ConfirmCallback comfirmCallback=new RabbitTemplate.ConfirmCallback() {
        @Override
        public void confirm(CorrelationData correlationData, boolean ack, String cause) {
            System.out.println("correlationData:"+correlationData);
            System.out.println("ack:"+ack);
            System.out.println("cause:"+cause);
            if(!ack){
                System.out.println("异常处理...");
            }
            
        }
    };
    final RabbitTemplate.ReturnCallback returnCallback=new RabbitTemplate.ReturnCallback() {
        @Override
        public void returnedMessage(org.springframework.amqp.core.Message message, 
                                    int replyCode, String replyText, String exchange, String routingKey) {
            System.out.println("replyCode:"+replyCode+",replyText:"+replyText+",exchange:"+exchange+",routingKey:"+routingKey);
        }
    };
    
    public void send(Object message, Map<String,Object> properties)throws Exception{
        // 注:全是spring包中的类,不是rabbitmq包的
        MessageHeaders mhs=new MessageHeaders(properties);
        Message msg= MessageBuilder.createMessage(message,mhs);
        rabbitTemplate.setConfirmCallback(comfirmCallback);
        rabbitTemplate.setReturnCallback(returnCallback);
        CorrelationData correlationData=new CorrelationData();
        correlationData.setId("1234567890"); // 必须是全局唯一,可为:id+timestamp 
        rabbitTemplate.convertAndSend("exchange-1","springboot.hello",msg,correlationData);
    }

   /**
    * @Description 发的消息是java对象类型
    * @Param [order, properties]
    * @return void
    * @Date 20:31 2019/3/25
    * @Author lianws
    **/
    public void sendOrder(Order order) throws Exception {
        rabbitTemplate.setConfirmCallback(comfirmCallback);
        rabbitTemplate.setReturnCallback(returnCallback);
        CorrelationData correlationData = new CorrelationData();
        correlationData.setId("0987654321"); // 必须是全局唯一,可为:id+timestamp 
        rabbitTemplate.convertAndSend("exchange-2", "springboot.javaMessage", order, correlationData);
    }
    
}
