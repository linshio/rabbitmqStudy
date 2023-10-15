package cn.linshio.rabbitmq.eight;

import cn.linshio.rabbitmq.utils.RabbitMqUtils;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

/**
 * 死信队列：也就是出现异常不能被正常消费的消息要放入该队列中进行操作
 */
public class Consumer01 {

    //普通的交换机
    public static final String NORMAL_EXCHANGE = "normal_exchange";
    //死信的交换机
    public static final String DEAD_EXCHANGE = "dead_exchange";

    //普通的队列名
    public static final String NORMAL_QUEUE = "normal_queue";
    //死信的队列名
    public static final String DEAD_QUEUE = "dead_queue";

    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = RabbitMqUtils.getChannel();

        //声明普通交换机
        channel.exchangeDeclare(NORMAL_EXCHANGE, BuiltinExchangeType.DIRECT);
        //声明死信交换机
        channel.exchangeDeclare(DEAD_EXCHANGE,BuiltinExchangeType.DIRECT);

        //设置普通交换机的参数 死信消息转到哪里去
        //x-message-ttl
        //x-dead-letter-exchange    => 要转发到的死信交换机
        //x-dead-letter-routing-key => 要转发到的死信交换机的路由key
        Map<String, Object> map = new HashMap<>();
        //设置ttl过期时间
//        map.put("x-message-ttl",100000);
        //设置正常队列长度的限制
        map.put("x-max-length",6);
        map.put("x-dead-letter-exchange",DEAD_EXCHANGE);
        map.put("x-dead-letter-routing-key","lisi");
        //声明普通队列
        channel.queueDeclare(NORMAL_QUEUE,false,false,false,map);
        //声明死信队列
        channel.queueDeclare(DEAD_QUEUE,false,false,false,null);
        //绑定交换机
        channel.queueBind(NORMAL_QUEUE,NORMAL_EXCHANGE,"zhangsan");
        channel.queueBind(DEAD_QUEUE,DEAD_EXCHANGE,"lisi");
        //消费消息
        System.out.println("消费者01准备开始接收消息...");
        DeliverCallback deliverCallback = (consumerTag,message)->{
            String content = new String(message.getBody());
            //如果消息中有info5，我们可以拒绝消息
            if ("info5".equals(content)){
                System.out.println("consumer01 reject the message"+content);
                //传入标签名tag 还有不放回普通队列
                channel.basicReject(message.getEnvelope().getDeliveryTag(),false);
            }else {
                System.out.println("consumer01-->"+content);
            }

        };
        //要开启手动应答
        channel.basicConsume(NORMAL_QUEUE,false,deliverCallback,consumerTag -> {});
    }
}
