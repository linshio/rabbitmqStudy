package cn.linshio.rabbitmq.one;

import cn.linshio.rabbitmq.utils.RabbitMqUtils;
import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.TimeoutException;

public class Consumer {
    //队列名称
    public static final String QUEUE_NAME = "Hello";
    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = RabbitMqUtils.getChannel();
        //消息接受后进行回调
        DeliverCallback deliverCallback = (consumerTag,deliver)->{
            System.out.println(new String(deliver.getBody()));
        };

        //取消接受消息进行回调
        CancelCallback cancelCallback = consumerTag -> {
            System.out.println("消息消费被中断");
        };
        /*
            1. 消费哪个队列
            2. 消费成功后是否要自动进行应答
            3. 消息接受后进行回调DeliverCallback
            4. 取消接受消息进行回调CancelCallback
         */
        channel.basicConsume(QUEUE_NAME,true,deliverCallback,cancelCallback);
    }
}
