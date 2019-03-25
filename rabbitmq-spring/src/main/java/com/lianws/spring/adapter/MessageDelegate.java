package com.lianws.spring.adapter;

import com.lianws.spring.entity.Order;
import com.lianws.spring.entity.Packaged;

import java.io.File;
import java.util.Map;

/**
 * @Description: 自定义消息监听类
 * @Author: lianws
 * @Date: 2019/3/23 7:57
 */
public class MessageDelegate {
    
    public void handleMessage(byte[] messageBody){
        System.out.println("默认方法,消息内容:"+new String(messageBody));
    }

    public void consumeMessage(byte[] messageBody) {
        System.out.println("默认字节数组方法,消息内容:" + new String(messageBody));
    }

    public void consumeMessage(String messageBody) {
        System.out.println("字符串方法,消息内容:" + messageBody);
    }

    public void method1(String messageBody) {
        System.out.println("method1方法,消息内容:" + messageBody);
    }

    public void method2(String messageBody) {
        System.out.println("method2方法,消息内容:" + messageBody);
    }

    public void consumeMessage(Map messageBody) {
        System.out.println("Map方法,消息内容:" + messageBody);
    }


    public void consumeMessage(Order order) {
        System.out.println("Order对象方法,消息内容,id:" + order.getId()
                +",name:"+order.getName()+",content:"+order.getContent());
    }

    public void consumeMessage(Packaged pack) {
        System.out.println("Packaged对象方法,消息内容,id:" + pack.getId()
                +",name:"+pack.getName()+",content:"+pack.getDescription());
    }

    public void consumeMessage(File file) {
        System.out.println("文件对象方法,消息内容:" + file.getName());
    }
    
    
}
