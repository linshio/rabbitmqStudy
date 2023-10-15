package cn.linshio.rabbitmq.six;

import cn.linshio.rabbitmq.utils.RabbitMqUtils;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class ReceiveMessage02 {
    public static final String EXCHANGE_NAME = "direct_logs";

    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = RabbitMqUtils.getChannel();
        //此处声明交换机为路由模式
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);
        //创建队列
        channel.queueDeclare("disk",false,false,false,null);
        //与交换机进行绑定
        channel.queueBind("disk",EXCHANGE_NAME,"error");
        System.out.println("02接收方准备好接收消息");
        DeliverCallback deliverCallback = (consumerTag, message) -> {
            System.out.println("已经消费==>"+new String(message.getBody()));
        };
        channel.basicConsume("disk",true,deliverCallback,consumerTag -> {});
    }
}
