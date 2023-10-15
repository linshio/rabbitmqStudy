package cn.linshio.rabbitmq.one;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Produce {

    //队列名称
    public static final String QUEUE_NAME = "Hello";

    public static void main(String[] args) throws IOException, TimeoutException {

        //创建连接工厂
        ConnectionFactory factory = new ConnectionFactory();
        //设置连接信息 ip账号与密码
        factory.setHost("60.204.128.243");
        factory.setUsername("guest");
        factory.setPassword("guest");
        //创建连接对象
        Connection connection = factory.newConnection();
        //创建连接信道
        Channel channel = connection.createChannel();
        //进行一个队列声明
        /*
            1.队列名称
            2.是否持久化
            3.是否独享
            4.消费端消费完是否释放资源
         */
        channel.queueDeclare(QUEUE_NAME,false,false,false,null);
        //生成一个消息
        String message = "hello world";
        //将消息发送到mq中
        /*
            1.交换机的名称
            2.路由的key值是哪个，这里为队列的名称
            3.其他的参数信息
            4.发送消息的消息体
         */
        channel.basicPublish("",QUEUE_NAME,null,message.getBytes());
        System.out.println("消息发送完毕");
    }
}
