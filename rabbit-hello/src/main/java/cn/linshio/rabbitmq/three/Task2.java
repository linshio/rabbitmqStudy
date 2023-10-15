package cn.linshio.rabbitmq.three;

import cn.linshio.rabbitmq.utils.RabbitMqUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.MessageProperties;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

//生产者
public class Task2 {
    public static final String ATK_QUEUE_NAME = "atk_queue";

    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = RabbitMqUtils.getChannel();
        //开启发布确认
        channel.confirmSelect();
        //将队列进行持久化durable=true
        channel.queueDeclare(ATK_QUEUE_NAME,true,false,false,null);
        System.out.println("生产者创建成功");
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()){
            String message = scanner.next();
            //将消息的属性进行持久化props=MessageProperties.PERSISTENT_TEXT_PLAIN
            channel.basicPublish("",ATK_QUEUE_NAME, MessageProperties.PERSISTENT_TEXT_PLAIN,message.getBytes(StandardCharsets.UTF_8));
            System.out.println("消息发送成功==>"+message);
        }
    }
}
