package cn.linshio.rabbitmq.springbootrabbitmq.consumer;

import cn.linshio.rabbitmq.springbootrabbitmq.config.TTLQueueConfig;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Date;

//消费方  队列ttl的消费者
@Component
@Slf4j
public class DeadLetterConsumer {

    @RabbitListener(queues = TTLQueueConfig.QUEUE_DEAD_LETTER_D)
    public void getMessage(Message message, Channel channel){
        String msg = new String(message.getBody());
        log.info("当前的时间为<{}>,接收到的消息->{}",new Date(),msg);
    }


}
