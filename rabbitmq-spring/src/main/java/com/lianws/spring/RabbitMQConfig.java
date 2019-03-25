package com.lianws.spring;

import com.lianws.spring.adapter.MessageDelegate;
import com.lianws.spring.convert.ImageMessageConverter;
import com.lianws.spring.convert.PDFMessageConverter;
import com.lianws.spring.convert.TextMessageConverter;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.amqp.support.ConsumerTagStrategy;
import org.springframework.amqp.support.converter.ContentTypeDelegatingMessageConverter;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.UUID;

/**
 * @Description: 配置类
 * @Author: lianws
 * @Date: 2019/3/21 11:38
 */

@Configuration
@ComponentScan({"com.lianws.spring"})
public class RabbitMQConfig {

    // @Bean 如果没有配置属性(默认的),就相当于<bean id="方法名字" class="返回值"/>
    @Bean
    public ConnectionFactory connectionFactory(){
        CachingConnectionFactory connectionFactory=new CachingConnectionFactory();
        connectionFactory.setAddresses("127.0.0.1:5672");
        connectionFactory.setUsername("admin");
        connectionFactory.setPassword("admin@123");
        connectionFactory.setVirtualHost("/");
        return connectionFactory;
    }
    /**
     * 通过RabbitAdmin声明exchagne,queue,binding
     * 1.1.配置连接工厂
     * 2.配置RabbitAdmin(需要一个连接工厂)
     * 注: 参数connectionFactory名称比如和上面方法的名称一致,否则会NPE,因为spring默认是根据方法名来注入的
     */
    @Bean
    public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory){
        RabbitAdmin rabbitAdmin=new RabbitAdmin(connectionFactory);
        rabbitAdmin.setAutoStartup(true);
        return rabbitAdmin;
    }
    /*================================这两种是等价的=============================================*/
    /**
     * 通过springamqp声明式配置exchange,queue,binding
     * 1. 配置exchange,queue
     * 2. 将队列绑定到交换机
     *  FanoutExchange: 将消息分发到所有绑定的队列,无routingKey的概念
     *  HeadersExchange: 通通过添加属性key-value匹配
     *  DirectExchange: 按照routingKey分发到指定队列
     *  TopicExchange: 多关键字匹配
     */
    @Bean
    public TopicExchange exchange001(){
        return new TopicExchange("topic001",true,false);
    }
    @Bean
    public Queue queue001(){
        return new Queue("queue001",true);
    }
    
    @Bean
    public Binding binding001(){
        return BindingBuilder.bind(queue001()).to(exchange001()).with("spring.*");
    }
    
    @Bean
    public TopicExchange exchange002(){
        return new TopicExchange("topic002",true,false);
    }
    @Bean
    public Queue queue002(){
        return  new Queue("queue002",true);
    }
    @Bean
    public Binding binding002(){
        return BindingBuilder.bind(queue002()).to(exchange002()).with("rabbit.*");
    }
    
    @Bean
    public Queue queue003(){return new Queue("queue003",true);}
    @Bean
    public Binding binding003(){return  BindingBuilder.bind(queue003()).to(exchange001()).with("mq.*");}
    @Bean
    public Queue queue_image(){return new Queue("image_queue",true);}
    @Bean
    public Queue queue_pdf(){return new Queue("pdf_queue",true);}

    //rabbitMqTemplate
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory){return  new RabbitTemplate(connectionFactory);}

    //simpleMessageListenerContainer简单消息监听容器
    @Bean
    public SimpleMessageListenerContainer messageContainer(ConnectionFactory connectionFactory){
        SimpleMessageListenerContainer messageContainer=new SimpleMessageListenerContainer(connectionFactory);
        messageContainer.setQueues(queue001(),queue002(),queue003(),queue_image(),queue_pdf());// 监听的队列
        messageContainer.setConcurrentConsumers(1); // 当前消费者个数
        messageContainer.setMaxConcurrentConsumers(5); // 最大消费者个数
        messageContainer.setDefaultRequeueRejected(false); //是否重回队列,一般为false 否
        messageContainer.setAcknowledgeMode(AcknowledgeMode.AUTO); //ack模式
        messageContainer.setExposeListenerChannel(true); // 监听是否外漏
        // 消费端标签策略:在消费端生成标签是可以自定义策略即自定义标签-实际工作中根据具体需求设定
        messageContainer.setConsumerTagStrategy(new ConsumerTagStrategy() {
            @Override
            public String createConsumerTag(String queue) {
                return queue+"_"+ UUID.randomUUID().toString().replace("-","");
            }
        });
        
        /**
         *  消息监听:监听上面设置的队列,onMessage当有消息过来时可以对消息做一些处理
         *  方式一
         */
//        messageContainer.setMessageListener(new ChannelAwareMessageListener() {
//            @Override
//            public void onMessage(Message message, Channel channel) throws Exception {
//                System.out.println("----------收到消息:"+new String( message.getBody()));;
//            }
//        });
        
        /**
         * 方式二:使用监听器适配器
         * 默认消息处理方法是:handleMessage(Byte[] messageBody)
         * 可以自己指定一个方法名: setDefaultListenerMethod("指定的方法的名称")
         * 可以自己指定方法的参数: setMessageConverter("转换器类")添加转换器,从字节数组转换成String
         */
//        MessageListenerAdapter adapter=new MessageListenerAdapter(new MessageDelegate());
//        adapter.setDefaultListenerMethod("consumeMessage");
//        adapter.setMessageConverter(new TextMessageConverter());
//        messageContainer.setMessageListener(adapter);
        
        /**
         * 适配器方式二: 队列名称和方法名称也可以一一匹配
         * 使用setQueueOrTagToMethodName(Map<String,String> map)指定队列和方法,那么就可以把
         * 指定队列中的消息传到指定方法中去处理.
         * 参数map的key是队列名称,value是方法名称
         */
//        MessageListenerAdapter adapter=new MessageListenerAdapter(new MessageDelegate());
//        Map<String,String> queueOrTagToMethodName=new HashMap<>();
//        queueOrTagToMethodName.put("queue001","method1");
//        queueOrTagToMethodName.put("queue002","method2");
//        adapter.setQueueOrTagToMethodName(queueOrTagToMethodName);
//        adapter.setMessageConverter(new TextMessageConverter());// 指定的方法的参数也是String故也需要转换
//        messageContainer.setMessageListener(adapter);

        /**
         * 各种转换器:
         */
        // 1 json转换器
//        MessageListenerAdapter adapter=new MessageListenerAdapter(new MessageDelegate());
//        adapter.setDefaultListenerMethod("consumeMessage");
//        Jackson2JsonMessageConverter jackson2JsonMessageConverter=new Jackson2JsonMessageConverter();
//        adapter.setMessageConverter(jackson2JsonMessageConverter);
//        messageContainer.setMessageListener(adapter);
        
        
        //2 java对象转换器
//        MessageListenerAdapter adapter=new MessageListenerAdapter(new MessageDelegate());
//        adapter.setDefaultListenerMethod("consumeMessage");
//        Jackson2JsonMessageConverter jackson2JsonMessageConverter=new Jackson2JsonMessageConverter();
//        // 比1多了这2句
//        DefaultJackson2JavaTypeMapper javaTypeMapper=new DefaultJackson2JavaTypeMapper();
//        jackson2JsonMessageConverter.setJavaTypeMapper(javaTypeMapper);
//        
//        adapter.setMessageConverter(jackson2JsonMessageConverter);
//        messageContainer.setMessageListener(adapter);
        
        
        // 3. java对象多映射转换
//        MessageListenerAdapter adapter=new MessageListenerAdapter(new MessageDelegate());
//        adapter.setDefaultListenerMethod("consumeMessage");
//        Jackson2JsonMessageConverter jackson2JsonMessageConverter=new Jackson2JsonMessageConverter();
//        DefaultJackson2JavaTypeMapper javaTypeMapper=new DefaultJackson2JavaTypeMapper();
//        //比 2多了setIdClassMapping
//        Map<String,Class<?>> idClassMapper=new HashMap<>(16);
//        idClassMapper.put("order", com.lianws.spring.entity.Order.class);
//        idClassMapper.put("packaged", com.lianws.spring.entity.Packaged.class );
//        javaTypeMapper.setIdClassMapping(idClassMapper);
//         
//        jackson2JsonMessageConverter.setJavaTypeMapper(javaTypeMapper);
//        adapter.setMessageConverter(jackson2JsonMessageConverter);
//        messageContainer.setMessageListener(adapter);

        
        // 4.多转换器
        MessageListenerAdapter adapter=new MessageListenerAdapter(new MessageDelegate());
        adapter.setDefaultListenerMethod("consumeMessage");
        //全局转换器
        ContentTypeDelegatingMessageConverter converter=new ContentTypeDelegatingMessageConverter();

        TextMessageConverter textConverter=new TextMessageConverter();
        converter.addDelegate("text",textConverter);
        converter.addDelegate("html/text",textConverter);
        converter.addDelegate("xml/text",textConverter);
        converter.addDelegate("text/plain",textConverter);

        Jackson2JsonMessageConverter jsonConverter=new Jackson2JsonMessageConverter();
        converter.addDelegate("json",jsonConverter);
        converter.addDelegate("application/json",jsonConverter);

        ImageMessageConverter imageConverter=new ImageMessageConverter();
        converter.addDelegate("image",imageConverter);
        converter.addDelegate("image/png",imageConverter);

        PDFMessageConverter pdfConverter=new PDFMessageConverter();
        converter.addDelegate("pdf",pdfConverter);
        converter.addDelegate("application/pdf",pdfConverter);
        adapter.setMessageConverter(converter);
        messageContainer.setMessageListener(adapter);


        return  messageContainer;
    }
}
