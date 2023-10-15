package cn.linshio.rabbitmq.eight;

import cn.linshio.rabbitmq.utils.RabbitMqUtils;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Producer {

    //普通的交换机
    public static final String NORMAL_EXCHANGE = "normal_exchange";
    public static void main(String[] args) throws IOException, TimeoutException {
        //AMQP.BasicProperties
        Channel channel = RabbitMqUtils.getChannel();
        //expiration表示设置消息的过期时间
//        AMQP.BasicProperties basicProperties = new AMQP.BasicProperties()
//                .builder().expiration("10000").build();
        System.out.println("开始准备发送消息...");
        for (int i = 0; i < 10; i++) {
            String message = "info" + i;
            channel.basicPublish(NORMAL_EXCHANGE,"zhangsan",null,message.getBytes());
            System.out.println("生产者发送消息==>"+message);
        }
    }
}
