package cn.linshio.rabbitmq.three;

import cn.linshio.rabbitmq.utils.RabbitMqUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Worker03 {
    //队列的名称
    public static final String ATK_QUEUE_NAME = "atk_queue";

    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = RabbitMqUtils.getChannel();
        System.out.println("消息一号处理时间较短");
        DeliverCallback deliverCallback = (consumerTag,message)->{
            //模拟处理业务时间
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println("消息一号进行消费："+new String(message.getBody()));
            /*
             * 1. 第一个参数为消息的id
             * 2. 第二个参数为是否进行批量应答
             */
            channel.basicAck(message.getEnvelope().getDeliveryTag(),false);
        };

        //设置不公平分发
        channel.basicQos(1);
        //autoAck为手动应答
        channel.basicConsume(ATK_QUEUE_NAME,false,deliverCallback,
                consumerTag -> System.out.println("消息中断回调"));
    }

}
