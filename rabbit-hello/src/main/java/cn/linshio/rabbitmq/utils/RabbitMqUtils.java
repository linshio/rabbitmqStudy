package cn.linshio.rabbitmq.utils;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class RabbitMqUtils {
    //创建Connection的工具类
    public static Channel getChannel() throws IOException, TimeoutException {
        //创建连接工厂
        ConnectionFactory factory = new ConnectionFactory();
        //设置连接信息 ip账号与密码
        factory.setHost("60.204.128.243");
        factory.setUsername("guest");
        factory.setPassword("guest");
        //创建连接对象
        Connection connection = factory.newConnection();
        //创建连接信道
        return connection.createChannel();
    }
}
