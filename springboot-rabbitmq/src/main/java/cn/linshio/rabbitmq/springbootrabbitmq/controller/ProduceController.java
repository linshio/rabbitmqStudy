package cn.linshio.rabbitmq.springbootrabbitmq.controller;

import cn.linshio.rabbitmq.springbootrabbitmq.config.ConfirmConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.ReturnedMessage;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;

@RestController
@Slf4j
@RequestMapping("/confirm")
public class ProduceController implements RabbitTemplate.ConfirmCallback,RabbitTemplate.ReturnsCallback{

    @Resource
    private RabbitTemplate rabbitTemplate;

    @GetMapping("/sendMessage/{message}")
    public void confirmMessage(@PathVariable("message") String message){
        //携带CorrelationData 参数给 交换机确认回调方法
        CorrelationData correlationData = new CorrelationData("001");
        rabbitTemplate.convertAndSend(ConfirmConfig.CONFIRM_EXCHANGE,
                ConfirmConfig.CONFIRM_ROUTING_KEY,message.getBytes(StandardCharsets.UTF_8),
                correlationData);
        log.info("发送方发送消息==>{}",message+"key1");

        CorrelationData correlationData2 = new CorrelationData("002");
        rabbitTemplate.convertAndSend(ConfirmConfig.CONFIRM_EXCHANGE,
                ConfirmConfig.CONFIRM_ROUTING_KEY+"2",message.getBytes(StandardCharsets.UTF_8),
                correlationData2);
        log.info("发送方发送消息==>{}",message+"key12");
    }


    //我们要将当前类注入到RabbitTemplate中，因为我们实现的是RabbitTemplate内部的接口
    @PostConstruct
    public void init(){
        rabbitTemplate.setConfirmCallback(this);
        rabbitTemplate.setReturnsCallback(this);
    }

    /**
     * 交换机确认回调方法
     * 1. 发消息 交换机接收到了进行回调
     * @param correlationData 保存回调消息的ID以及相关信息
     * @param ack 交换机收到消息是否应答
     * @param cause 失败的原因，如果成功则为null
     */
    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        String id = correlationData.getId() != null ? correlationData.getId() : "";
        if (ack){
            log.info("交换机已经收到id==>{}的消息",id);
        }else {
            log.info("交换机未收到id==>{}的消息,原因为「{}」",id,cause);
        }
    }


    //可以在消息传递过程中不可到达目的地的时候将消息返回给消费者
    @Override
    public void returnedMessage(ReturnedMessage returned) {
        log.error("消息{},被交换机{}退回,退回的原因为:{},路由key:{}",
                new String(returned.getMessage().getBody()),
                returned.getExchange(),
                returned.getReplyText(),
                returned.getRoutingKey());
    }
}
