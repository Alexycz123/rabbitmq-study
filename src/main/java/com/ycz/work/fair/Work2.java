package com.ycz.work.fair;/*
 @author ycz
 @date 2022-03-02-18:19
*/

import com.rabbitmq.client.*;

import java.io.IOException;

public class Work2 {
    public static void main(String[] args) {

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
            String name="queue";
            //4.通过创建交换机，声明队列，绑定关系，路由key，发送消息，和接收消息
            Channel finalChannel = channel;
                                        //手动应答
            channel.basicQos(1);
            channel.basicConsume(name, false, new DeliverCallback() {
                @Override
                public void handle(String s, Delivery delivery) throws IOException {
                    System.out.println(name+"收到消息是：" + new String(delivery.getBody(),"UTF-8")  );
                    try {
                        Thread.sleep(1000);
                        //公平
                        finalChannel.basicAck(delivery.getEnvelope().getDeliveryTag(),false);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
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
