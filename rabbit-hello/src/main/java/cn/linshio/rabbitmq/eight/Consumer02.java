package cn.linshio.rabbitmq.eight;

import cn.linshio.rabbitmq.utils.RabbitMqUtils;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

import java.util.HashMap;
import java.util.Map;

public class Consumer02 {
    //死信的队列名
    public static final String DEAD_QUEUE = "dead_queue";
    public static void main(String[] args) throws Exception{
        Channel channel = RabbitMqUtils.getChannel();
        //消费消息
        System.out.println("消费者02准备开始接收消息...");
        DeliverCallback deliverCallback = (consumerTag, message)->{
            System.out.println("consumer02-->"+new String(message.getBody()));
        };
        channel.basicConsume(DEAD_QUEUE,true,deliverCallback,consumerTag -> {});
    }
}
