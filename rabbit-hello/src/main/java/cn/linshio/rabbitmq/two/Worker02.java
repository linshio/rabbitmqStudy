package cn.linshio.rabbitmq.two;

import cn.linshio.rabbitmq.utils.RabbitMqUtils;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Worker02 {
    public static final String QUEUE_NAME = "Hello";
    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = RabbitMqUtils.getChannel();
        //消息接收后进行回调
        DeliverCallback deliverCallback = (consumerTag,message)->{
            System.out.println("接收到的消息："+new String(message.getBody()));
        };
        //取消消息接收后进行回调
        CancelCallback cancelCallback = consumerTag -> {
            System.out.println("消息消费被中断");
        };
        System.out.println("<<消息接收者二>>");
        channel.basicConsume(QUEUE_NAME,true,deliverCallback,cancelCallback);
    }
}
