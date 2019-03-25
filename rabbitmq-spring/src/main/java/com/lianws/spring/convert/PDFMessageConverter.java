package com.lianws.spring.convert;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.support.converter.MessageConversionException;
import org.springframework.amqp.support.converter.MessageConverter;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.nio.file.Files;
import java.util.UUID;

/**
 * @Description:
 * @Author: lianws
 * @Date: 2019/3/24 17:54
 */
public class PDFMessageConverter implements MessageConverter {

    @Override
    public Message toMessage(Object object, MessageProperties messageProperties) throws MessageConversionException {
        throw new MessageConversionException("convert pdf to message err!!!");
    }

    @Override
    public Object fromMessage(Message message) throws MessageConversionException {
        System.out.println("----------PdfMessageConverter---------");

        byte[] body=message.getBody();
        String fileName= UUID.randomUUID().toString();
        String path="d:/test/"+fileName+".pdf";
        File file=new File(path);
        try{
            Files.copy(new ByteArrayInputStream(body),file.toPath());
        }catch (Exception e){
            e.printStackTrace();
        }

        return file;
    }
}
