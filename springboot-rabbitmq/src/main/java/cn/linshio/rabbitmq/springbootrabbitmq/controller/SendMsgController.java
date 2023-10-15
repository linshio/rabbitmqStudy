package cn.linshio.rabbitmq.springbootrabbitmq.controller;

import cn.linshio.rabbitmq.springbootrabbitmq.config.DelayedQueueConfig;
import cn.linshio.rabbitmq.springbootrabbitmq.config.TTLQueueConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Date;

@RestController
@Slf4j
@RequestMapping("/ttl")
//发送消息的生产者
public class SendMsgController {

    @Resource
    private RabbitTemplate rabbitTemplate;

    @GetMapping("/sendMsg/{message}")
    public void sendNormalMessage(@PathVariable("message") String message){
        log.info("当前时间<{}>,发送TTL给两个队列：{}",new Date(),message);
        rabbitTemplate.convertAndSend(TTLQueueConfig.X_EXCHANGE,"XA","消息来自ttl为10s的队列"+message);
        rabbitTemplate.convertAndSend(TTLQueueConfig.X_EXCHANGE,"XB","消息来自ttl为40s的队列"+message);
    }


    @GetMapping("/sendExpireMsg/{message}/{ttlTime}")
    public void sendTTLMessage(@PathVariable("message") String message,
                               @PathVariable("ttlTime") String ttlTime){
        log.info("当前时间<{}>,发送延时时间为「{}」ms->TTL给队列：{}",new Date(),ttlTime,message);
        rabbitTemplate.convertAndSend(TTLQueueConfig.X_EXCHANGE,"XC",message,msg->{
            msg.getMessageProperties().setExpiration(ttlTime);
            return msg;
        });
    }

    //开始发送消息 基于插件的 延迟时间
    @GetMapping("/sendDelayedMsg/{message}/{delayedTime}")
    public void sendDelayedMessage(@PathVariable("message") String message,
                               @PathVariable("delayedTime") Integer delayedTime){
        log.info("当前时间<{}>,发送延时时间为「{}」ms->延迟消息给队列：{}",new Date(),delayedTime,message);
        rabbitTemplate.convertAndSend(DelayedQueueConfig.DELAYED_EXCHANGE_NAME,
                DelayedQueueConfig.DELAYED_ROUTING_KEY,
                message, msg->{
            msg.getMessageProperties().setDelay(delayedTime);
            return msg;
        });
    }



}
