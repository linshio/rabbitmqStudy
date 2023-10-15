package cn.linshio.rabbitmq.seven;

import cn.linshio.rabbitmq.utils.RabbitMqUtils;
import com.rabbitmq.client.Channel;

import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

public class SendTopic {
    public static final String EXCHANGE_NAME = "topic_logs";

    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = RabbitMqUtils.getChannel();
        System.out.println("生产者准备生产");
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()){
            String message = scanner.next();
            //此处的routingKey就图方便写的message
            channel.basicPublish(EXCHANGE_NAME,message,null,message.getBytes());
            System.out.println("生产者已经发送->"+message);
        }
    }
}
