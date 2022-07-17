package com.ycz.work.lunxun;/*
 @author ycz
 @date 2022-03-02-9:29
*/

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class Producer {
    public static void main(String[] args) {

        //所有的中间件技术都是基于tcp/ip协议基础上构建新型的协议规范，只不过rabbitmq遵循的是amqp

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
            //4.通过创建交换机，声明队列，绑定关系，路由key，发送消息，和接收消息


            String exchangeName="";

            String routeKey="queue";//应该满足1，3，4

            String type="";




            for (int i = 0; i < 20; i++) {
                String message= "hello i am :::::" +i;
                channel.basicPublish(exchangeName,routeKey,null,message.getBytes());
                Thread.sleep(2000);
            }


            //7.关闭连接

            System.out.println("消息发送成功");
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
