package cn.linshio.rabbitmq.three;

import cn.linshio.rabbitmq.utils.RabbitMqUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Worker04 {
    public static final String ATK_QUEUE_NAME = "atk_queue";

    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = RabbitMqUtils.getChannel();
        System.out.println("消息二号处理时间较长");
        DeliverCallback deliverCallback = (consumerTag,message)->{

            //模拟处理业务时间
            try {
                Thread.sleep(30000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println("消息二号进行消费："+new String(message.getBody()));
            /*
             * 1. 第一个参数为消息的id
             * 2. 第二个参数为是否进行批量应答
             */
            channel.basicAck(message.getEnvelope().getDeliveryTag(),false);
        };
        //设置不公平分发=1 如果值超过1就是设定欲取值：就是有点像缓冲区的最大值，会有几条数据过来
        channel.basicQos(1);
        //autoAck:false 为手动进行应答
        channel.basicConsume(ATK_QUEUE_NAME,false,deliverCallback,
                consumerTag -> System.out.println("消息中断回调"));
    }

}
