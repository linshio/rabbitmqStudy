package cn.linshio.rabbitmq.springbootrabbitmq.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.HashMap;
import java.util.Map;


@Configuration
public class DelayedQueueConfig {

    //定义一个交换机
    public static final String DELAYED_EXCHANGE_NAME = "delayed.exchange";
    //定义一个队列
    public static final String DELAYED_QUEUE_NAME = "delayed.queue";
    //定义一个routingKay
    public static final String DELAYED_ROUTING_KEY = "delayed.exchange";

    //自定义交换机
    //x-delayed-type direct
    @Bean
    public CustomExchange delayedExchange(){
        Map<String, Object> arguments = new HashMap<>();
        //指定交换机的内部类型为direct
        arguments.put("x-delayed-type","direct");
        /**
         * 1. 交换机的名称
         * 2. 交换机的类型 这里是自定义的
         * 3. 是否持久化
         * 4. 是否消费完自动删除
         * 5. 交换机配置参数
         */
        return new CustomExchange(DELAYED_EXCHANGE_NAME,
                "x-delayed-message",true,false,arguments);
    }

    //创建队列
    @Bean
    public Queue delayedQueue(){
        return QueueBuilder
                .durable(DELAYED_QUEUE_NAME)
                .build();
    }

    //绑定关系
    @Bean("delayedQueueBindingExchange")
    public Binding queueBindingExchange(@Qualifier("delayedQueue") Queue delayedQueue,
                                        @Qualifier("delayedExchange") CustomExchange delayedExchange){
        return BindingBuilder.bind(delayedQueue).to(delayedExchange).with(DELAYED_ROUTING_KEY).noargs();
    }
}
