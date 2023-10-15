package cn.linshio.rabbitmq.five;

import cn.linshio.rabbitmq.utils.RabbitMqUtils;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

//消费端
public class ReceiveMessage01 {
    public static final String EXCHANGE_NAME = "logs";

    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = RabbitMqUtils.getChannel();

        //创建一个交换机 模式为fanout也就是广播模式
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.FANOUT);
        //创建一个临时队列
        String queue = channel.queueDeclare().getQueue();

        //将该临时的队列绑定到我们的交换机上
        channel.queueBind(queue,EXCHANGE_NAME,"");
        System.out.println("接收方准备好接收消息");
        DeliverCallback deliverCallback = (consumerTag, message) -> {
            System.out.println("已经消费==>"+new String(message.getBody()));
        };

        channel.basicConsume(queue,true,deliverCallback,consumerTag -> {});
    }
}
