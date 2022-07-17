package com.ycz.routing;/*
 @author ycz
 @date 2022-03-02-9:29
*/

import com.rabbitmq.client.*;

import java.io.IOException;

class ConsumerNew implements Runnable{

    @Override
    public void run() {
        //1.创建连接工程
        ConnectionFactory connectionFactory=new ConnectionFactory();

        connectionFactory.setHost("119.23.241.183");
        connectionFactory.setPort(5672);
        connectionFactory.setUsername("admin");
        connectionFactory.setPassword("admin");
        connectionFactory.setVirtualHost("/");//会指发送到根目录下


        Connection connection = null;
        Channel channel = null;

        try{
            //2.创建连接Connection
            connection=connectionFactory.newConnection("生产者");
            //3.通过连接获取通道Channel
            channel=connection.createChannel();
            String name=Thread.currentThread().getName();
            //4.通过创建交换机，声明队列，绑定关系，路由key，发送消息，和接收消息
            channel.basicConsume(name, true, new DeliverCallback() {
                @Override
                public void handle(String consumerTag, Delivery message) throws IOException {
                    System.out.println(name+"收到消息是：" + new String(message.getBody(),"UTF-8")  );
                }
            }, new CancelCallback() {
                @Override
                public void handle(String s) throws IOException {
                    System.out.println(name+"接收失败了");
                }
            });
            System.out.println(name+"开始接收消息");
            System.in.read();
            //8.关闭通道
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if (channel!=null && channel.isOpen()){
                try {
                    channel.close();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            if (connection!=null && connection.isOpen()){
                try {
                    connection.close();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }

    }
}


public class Consumer {
    public static void main(String[] args) {

        //所有的中间件技术都是基于tcp/ip协议基础上构建新型的协议规范，只不过rabbitmq遵循的是amqp
        ConsumerNew consumerNew = new ConsumerNew();
        new Thread(consumerNew,"queue").start();
        new Thread(consumerNew,"queue2").start();
        new Thread(consumerNew,"queue3").start();


    }
}
