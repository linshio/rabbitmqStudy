package cn.linshio.rabbitmq.springbootrabbitmq.consumer;

import cn.linshio.rabbitmq.springbootrabbitmq.config.ConfirmConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class WarningConsumer {

    @RabbitListener(queues = ConfirmConfig.WARNING_QUEUE)
    public void receiveWarningMessage(Message message){
        String msg = new String(message.getBody());
        log.error("报警发现不可路由的消息为{}",msg);
    }
}
