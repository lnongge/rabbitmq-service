package com.lianws.spring;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lianws.spring.entity.Order;
import com.lianws.spring.entity.Packaged;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RabbitmqSpringApplicationTests {

	@Test
	public void contextLoads() {
	}
	
	
	@Autowired
    private RabbitAdmin rabbitAdmin;
	/**
	 * @Description rabbitAdmin的使用demo
	 * @Param []
	 * @return void
	 * @Date 16:39 2019/3/25
	 * @Author lianws
	 **/
	@Test
	public void testAdmin() throws Exception{
        /**
         * 方法一:先声明exchange&&queue,再绑定
         */
	    // 声明exchange
	    rabbitAdmin.declareExchange(new DirectExchange("test.direct",false,false));
	    rabbitAdmin.declareExchange(new TopicExchange("test.topic",false,false));
	    rabbitAdmin.declareExchange(new FanoutExchange("test.fanout",false,false));
	    // 声明队列
        rabbitAdmin.declareQueue(new Queue("test.direct.queue",false));
        rabbitAdmin.declareQueue(new Queue("test.topic.queue",false));
        rabbitAdmin.declareQueue(new Queue("test.fanout.queue",false));
        // 绑定
        rabbitAdmin.declareBinding(new Binding("test.direct.queue", Binding.DestinationType.QUEUE,
                "test.direct","direct",new HashMap<>()));
        /**
         * 方法二:绑定的时候才声明:queue和exchange
         */
        rabbitAdmin.declareBinding(BindingBuilder.bind(
                new Queue("test.topic.queue",false)) // 创建queue
                .to(new TopicExchange("test.topic",false,false)) // 创建exchange
                .with("topic.#")); // 指定routingKey
        rabbitAdmin.declareBinding(BindingBuilder.bind(
                new Queue("test.fanout.queue",false)) // queue
                .to(new FanoutExchange("test.fanout",false,false))); //exchange
                // fanoutExchange不走路由键故没有with
        
        //清空队列
        rabbitAdmin.purgeQueue("test.topic.queue",false);
    }
    @Autowired
    private RabbitTemplate rabbitTemplate;
	/**
	 * @Description rabbitTemplate使用示例demo
	 * @Param []
	 * @return void
	 * @Date 16:39 2019/3/25
	 * @Author lianws
	 **/
	@Test
    public void testSendMessage1(){
	    MessageProperties properties=new MessageProperties();
	    properties.getHeaders().put("description","描述信息");
	    properties.getHeaders().put("type","自定义消息类型");
	    String msg="Hello,RabbitTemplate";
	    Message message=new Message(msg.getBytes(),properties);
	    rabbitTemplate.convertAndSend("topic001", "spring.amqp", message, new MessagePostProcessor() {
            @Override
            public Message postProcessMessage(Message message) throws AmqpException {
                message.getMessageProperties().getHeaders().put("description","额外的描述信息");
                message.getMessageProperties().getHeaders().put("attrbution","额外自定义的属性");
                return message;
            }
        });
    }
    
    @Test
    public void testSendMessage2(){
        MessageProperties properties=new MessageProperties();
        properties.setContentType("text/plain");
        Message message=new Message("Hello,RabbitTemplate1".getBytes(),properties);
	    rabbitTemplate.send("topic001","spring.send",message);
	    
	    rabbitTemplate.convertAndSend("topic001","mq.send","Hello,RabbitTemplate2");
	    
	    rabbitTemplate.convertAndSend("topic002","rabbit.send","Hello,RabbitTemplate3");
    }

    @Test
    public void testSendMessage3() {
        MessageProperties properties = new MessageProperties();
        properties.setContentType("text/plain");
        Message message = new Message("Hello,RabbitTemplate1".getBytes(), properties);
        rabbitTemplate.send("topic001", "spring.send", message);
        rabbitTemplate.send("topic002", "rabbit.send", message);
    }
	/**
	 * @Description 各种转换器示例demo
	 * @Param []
	 * @return void
	 * @Date 16:40 2019/3/25
	 * @Author lianws
	 **/
    @Test
    public void testSendJsonMessage() throws  Exception{
        Order order=new Order();
        order.setId("001");
        order.setName("消息订单");
        order.setContent("描述信息");
        ObjectMapper objectMapper=new ObjectMapper();
        String json = objectMapper.writeValueAsString(order);
        System.out.println("order 4 json:"+json);
        //注:contentType一定要设置成application/json
        MessageProperties messageProperties=new MessageProperties();
        messageProperties.setContentType("application/json");
        Message message=new Message(json.getBytes(),messageProperties);
        
        rabbitTemplate.send("topic001","spring.order",message);
    }
    @Test
    public void testSendJavaMessage() throws Exception {
        Order order = new Order();
        order.setId("001");
        order.setName("消息订单");
        order.setContent("描述信息");
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(order);
        System.out.println("order 4 json:" + json);
        MessageProperties messageProperties = new MessageProperties();
        messageProperties.setContentType("application/json");
        // key必须是__TypeId__ (左右各2个下划线)
        messageProperties.getHeaders().put("__TypeId__","com.lianws.spring.entity.Order");
        Message message = new Message(json.getBytes(), messageProperties);

        rabbitTemplate.send("topic001", "spring.order", message);
    }

    @Test
    public void testSendJavaMapperMessage() throws Exception {
        Order order = new Order();
        order.setId("001");
        order.setName("消息订单");
        order.setContent("描述信息");
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(order);
        System.out.println("order 4 json:" + json);
        MessageProperties messageProperties = new MessageProperties();
        messageProperties.setContentType("application/json");
        // 注:这里的key一样是固定,但value 变成了idClassMapping中的key
        messageProperties.getHeaders().put("__TypeId__", "order");
        Message message = new Message(json.getBytes(), messageProperties);

        rabbitTemplate.send("topic001", "spring.order", message);

        Packaged packaged=new Packaged();
        packaged.setId("002");
        packaged.setName("包裹消息");
        packaged.setDescription("包裹描述信息");
        json=objectMapper.writeValueAsString(packaged);
        System.out.println("pack 4 json:"+json);
        messageProperties.getHeaders().put("__TypeId__","packaged");
        Message message2=new Message(json.getBytes(),messageProperties);
        rabbitTemplate.send("topic001", "spring.pack", message2);
        
    }

    @Test
    public void testSendExtMessage() throws Exception {
	    // 发送图片
        byte[] body = Files.readAllBytes(Paths.get("C:/Users/75672/Desktop/mg/dkgj", "1.png"));
        MessageProperties messageProperties=new MessageProperties();
        messageProperties.setContentType("image/png");
        messageProperties.getHeaders().put("extName","png");
        Message message=new Message(body,messageProperties);
        rabbitTemplate.send("","image_queue",message);
        
        // 方式pdf
        body=Files.readAllBytes(Paths.get("G:/test","Hibernate_JPA.pdf"));
        messageProperties.setContentType("application/pdf");
        Message pdfMessage=new Message(body,messageProperties);
        rabbitTemplate.send("","pdf_queue",pdfMessage);
    }
    
}
