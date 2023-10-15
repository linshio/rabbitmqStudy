package cn.linshio.rabbitmq.springbootrabbitmq.consumer;

import cn.linshio.rabbitmq.springbootrabbitmq.config.DelayedQueueConfig;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@Slf4j
//消费者 基于延迟插件的
public class DelayedConsumer {

    @RabbitListener(queues = DelayedQueueConfig.DELAYED_QUEUE_NAME)
    public void receiveDelayedQueue(Message message, Channel channel){
        String msg = new String(message.getBody());
        log.info("当前的时间为<{}>,接收到延迟的消息->{}",new Date(),msg);
    }
}
