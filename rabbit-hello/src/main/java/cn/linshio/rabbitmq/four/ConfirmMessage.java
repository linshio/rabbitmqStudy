package cn.linshio.rabbitmq.four;

import cn.linshio.rabbitmq.utils.RabbitMqUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmCallback;


import java.io.IOException;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.TimeoutException;

public class ConfirmMessage {

    //发送的消息条数
    public static final int MESSAGE_COUNT = 1000;

    public static void main(String[] args) throws Exception {
//        testSingle();
//        testBrash();
        testAsynchronize();
    }

    //异步进行确认 在发送消息之前进行监听 总共耗时31ms
    public static void  testAsynchronize() throws Exception{
        Channel channel = RabbitMqUtils.getChannel();
        //开启发布确认
        channel.confirmSelect();


        channel.queueDeclare("Hello",false,false,false,null);

        //开一个并发记录map
        ConcurrentSkipListMap<Long,String> concurrentSkipListMap =
                new ConcurrentSkipListMap<>();

        //成功确认的回调函数
        ConfirmCallback ackCallback = (deliveryTag,multiple)->{
            //这里删除了成功的条数，剩下的就是失败的
            //如果是批量的，我们就批量删除
            if (multiple){
                ConcurrentNavigableMap<Long, String> map = concurrentSkipListMap.headMap(deliveryTag);
                map.clear();
            }else {
                //如果不是我们就删除该数据
                concurrentSkipListMap.remove(deliveryTag);
            }
        };
        //未成功确认的回调函数
        /**
         *  1. 消息的id
         *  2. 是否为批量
         */
        ConfirmCallback nackCallback = (deliveryTag,multiple)->{
            String failMessage = concurrentSkipListMap.get(deliveryTag);
            System.out.println("发送失败的消息==>"+failMessage);
            System.out.println("消息确认失败，id："+deliveryTag);
        };

        //开启函数进行监听
        channel.addConfirmListener(ackCallback,nackCallback);

        //开始时间
        long begin = System.currentTimeMillis();
        for (int i = 0; i < MESSAGE_COUNT; i++) {
            String message = i + "";
            //将每一条消息记录装载到map中
            concurrentSkipListMap.put(channel.getNextPublishSeqNo(),message);
            channel.basicPublish("","Hello",null,message.getBytes());
        }
        //结束时间
        long end = System.currentTimeMillis();
        //消耗时间
        System.out.println("总共耗时"+(end-begin)+"ms");
    }

    //批量进行确认 比如这里是每一百条数据进行一次确认 总共耗时260ms
    public static void  testBrash() throws Exception{
        Channel channel = RabbitMqUtils.getChannel();
        //开启发布确认
        channel.confirmSelect();
        //开始时间
        long begin = System.currentTimeMillis();
        channel.queueDeclare("Hello",false,false,false,null);
        for (int i = 0; i < MESSAGE_COUNT; i++) {
            String message = i + "";
            channel.basicPublish("","Hello",null,message.getBytes());
            //每一百条数据就确认一次
            if (i%100==0){
                channel.waitForConfirms();
                System.out.println(i);
            }
        }
        //结束时间
        long end = System.currentTimeMillis();
        //消耗时间
        System.out.println("总共耗时"+(end-begin)+"ms");
    }

    //单个确认发布 总共耗时18043ms
    public static void testSingle()throws IOException, TimeoutException, InterruptedException{
        Channel channel = RabbitMqUtils.getChannel();
        //开启发布确认
        channel.confirmSelect();
        //开始时间
        long begin = System.currentTimeMillis();
        channel.queueDeclare("Hello",false,false,false,null);
        for (int i = 0; i < MESSAGE_COUNT; i++) {
            String message = i + "";
            channel.basicPublish("","Hello",null,message.getBytes());
            //单个消息已发布就马上进行确认
            boolean flag = channel.waitForConfirms();
//            if (flag){
//                System.out.println("消息发送成功");
//            }
        }
        //结束时间
        long end = System.currentTimeMillis();
        //消耗时间
        System.out.println("总共耗时"+(end-begin)+"ms");
    }





}
