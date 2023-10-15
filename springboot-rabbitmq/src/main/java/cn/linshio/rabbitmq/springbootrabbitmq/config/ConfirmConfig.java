package cn.linshio.rabbitmq.springbootrabbitmq.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//发布确认的高级篇
@Configuration
public class ConfirmConfig {
    public static final String CONFIRM_EXCHANGE = "confirm_exchange";
    public static final String CONFIRM_QUEUE = "confirm_queue";
    public static final String CONFIRM_ROUTING_KEY = "key1";

    public static final String BACKUP_EXCHANGE = "backup_exchange";
    public static final String BACKUP_QUEUE = "backup_queue";
    public static final String WARNING_QUEUE = "warning_queue";

    @Bean("confirmExchange")
    public DirectExchange confirmExchange(){
        //这里要实现 确认交换机 指向备份交换机
        return ExchangeBuilder.directExchange(CONFIRM_EXCHANGE)
                .durable(true)
                .alternate(BACKUP_EXCHANGE)
                .build();
    }

    @Bean("confirmQueue")
    public Queue confirmQueue(){
        return QueueBuilder.durable(CONFIRM_QUEUE).build();
    }

    @Bean
    public Binding queueBindingExchange(@Qualifier("confirmQueue") Queue confirmQueue,
                                        @Qualifier("confirmExchange") DirectExchange confirmExchange){
        return BindingBuilder.bind(confirmQueue).to(confirmExchange).with(CONFIRM_ROUTING_KEY);
    }

    //添加一个备份的交换机与两个队列然后在进行绑定
    @Bean("backupExchange")
    public FanoutExchange backupExchange(){
        return new FanoutExchange(BACKUP_EXCHANGE);
    }
    @Bean("backupQueue")
    public Queue backupQueue(){
        return QueueBuilder.durable(BACKUP_QUEUE).build();
    }
    @Bean ("warningQueue")
    public Queue warningQueue(){
        return QueueBuilder.durable(WARNING_QUEUE).build();
    }
    @Bean
    public Binding backupBindingExchange(@Qualifier("backupQueue") Queue backupQueue,
                                         @Qualifier("backupExchange") FanoutExchange backupExchange){
        return BindingBuilder.bind(backupQueue).to(backupExchange);
    }
    @Bean
    public Binding warningBindingExchange(@Qualifier("warningQueue") Queue warningQueue,
                                         @Qualifier("backupExchange") FanoutExchange backupExchange){
        return BindingBuilder.bind(warningQueue).to(backupExchange);
    }

}
