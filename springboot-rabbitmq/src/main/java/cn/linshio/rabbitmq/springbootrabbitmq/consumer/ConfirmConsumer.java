package cn.linshio.rabbitmq.springbootrabbitmq.consumer;

import cn.linshio.rabbitmq.springbootrabbitmq.config.ConfirmConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ConfirmConsumer {

    @RabbitListener(queues = ConfirmConfig.CONFIRM_QUEUE)
    public void receiveMessage(Message message){
        String msg = new String(message.getBody());
        log.info("消费方接收到消息==>{}",msg);
    }
}
