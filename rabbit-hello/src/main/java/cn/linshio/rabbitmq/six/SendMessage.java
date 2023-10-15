package cn.linshio.rabbitmq.six;

import cn.linshio.rabbitmq.utils.RabbitMqUtils;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;

import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

//DIRECT模式
public class SendMessage {
    public static final String EXCHANGE_NAME = "direct_logs";

    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = RabbitMqUtils.getChannel();
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);

        System.out.println("准备开始发送消息");
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()){
            String message = scanner.next();
            //往哪边发送消息取决于routingKey
            channel.basicPublish(EXCHANGE_NAME,"error",null,message.getBytes());
            System.out.println("已经发送消息"+message);
        }
    }
}
