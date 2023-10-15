package cn.linshio.rabbitmq.seven;

import cn.linshio.rabbitmq.utils.RabbitMqUtils;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class ReceiveTopic01 {
    public static final String EXCHANGE_NAME = "topic_logs";

    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = RabbitMqUtils.getChannel();
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.TOPIC);
        String queueName = "Q1";
        channel.queueDeclare(queueName,false,false,false,null);
        //此处的通配符*表示一个单词 #号表示一个或多个单词
        channel.queueBind(queueName,EXCHANGE_NAME,"*.orange.*");
        System.out.println("topic01正在消费");
        DeliverCallback deliverCallback = (consumerTap,message)->{
            System.out.println(queueName+"队列接收,RoutingKey为->"+message.getEnvelope().getRoutingKey());
        };
        channel.basicConsume(queueName,true,deliverCallback,consumerTag -> {});
    }
}
