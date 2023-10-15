package cn.linshio.rabbitmq.springbootrabbitmq.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TTLQueueConfig {
    //普通交换机
    public static final String X_EXCHANGE = "xExchange";
    //死信交换机
    public static final String Y_DEAD_LETTER_EXCHANGE = "yExchange";
    //两个普通队列
    public static final String QUEUE_A = "QA";
    public static final String QUEUE_B = "QB";
    //一个死信队列
    public static final String QUEUE_DEAD_LETTER_D = "QD";

    //一个普通可以调整时间的延时队列
    public static final String QUEUE_C = "QC";

    //声明调整时间的延时队列
    @Bean("queueC")
    public Queue queueC(){
        return QueueBuilder
                .durable(QUEUE_C)
                .deadLetterExchange(Y_DEAD_LETTER_EXCHANGE)
                .deadLetterRoutingKey("YD")
                .build();
    }
    //绑定可以调整时间的延时队列
    @Bean
    public Binding queueCBindingX(@Qualifier("queueC") Queue queueC,
                                  @Qualifier("xExchange") DirectExchange xExchange){
        return BindingBuilder.bind(queueC).to(xExchange).with("XC");
    }

    //声明普通交换机Direct类型
    @Bean("xExchange")
    public DirectExchange xExchange(){
        return new DirectExchange(X_EXCHANGE);
    }

    //声明死信交换机Direct类型
    @Bean("yExchange")
    public DirectExchange yExchange(){
        return new DirectExchange(Y_DEAD_LETTER_EXCHANGE);
    }

    //声明普通的队列 设置TTL过期时间10s
    @Bean("queueA")
    public Queue queueA(){
        return QueueBuilder
                .durable(QUEUE_A) // 队列名
                .deadLetterExchange(Y_DEAD_LETTER_EXCHANGE)//设置关联的死信交换机
                .deadLetterRoutingKey("YD")//设置死信队列路由key
                .ttl(10000)//设置ttl过期时间
                .build();
    }
    //声明普通的队列 设置TTL过期时间40s
    @Bean("queueB")
    public Queue queueB(){
        return QueueBuilder
                .durable(QUEUE_B)
                .deadLetterExchange(Y_DEAD_LETTER_EXCHANGE)//设置关联的死信交换机
                .deadLetterRoutingKey("YD")//设置死信队列路由key
                .ttl(40000)//设置ttl过期时间
                .build();
    }

    //声明死信的队列
    @Bean("queueD")
    public Queue queueD(){
        return QueueBuilder
                .durable(QUEUE_DEAD_LETTER_D)
                .build();
    }

    //绑定
    @Bean
    public Binding queueABindingX(@Qualifier("queueA") Queue queueA,
                                  @Qualifier("xExchange") DirectExchange xExchange){
        return BindingBuilder.bind(queueA).to(xExchange).with("XA");
    }
    @Bean
    public Binding queueBBindingX(@Qualifier("queueB") Queue queueB,
                                  @Qualifier("xExchange") DirectExchange xExchange){
        return BindingBuilder.bind(queueB).to(xExchange).with("XB");
    }
    @Bean
    public Binding queueDBindingY(@Qualifier("queueD") Queue queueD,
                                  @Qualifier("yExchange") DirectExchange yExchange){
        return BindingBuilder.bind(queueD).to(yExchange).with("YD");
    }
}
