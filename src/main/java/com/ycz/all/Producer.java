package com.ycz.all;/*
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


            String exchangeName="direct_message_exchange"; // direct 交换机

            String routeKey="email";

            String exchangeType="direct";

            String message= "hello i am direct Alex Alex";

            //声明交换机，所谓持久化，就是交换机会不会随着服务器重启造成丢失，true代表不丢失，false代表丢失
            channel.exchangeDeclare(exchangeName,exchangeType,true);

            //声明队列                                  是否持久化，是否排他性     ，是否自动删除     是否有参数
            channel.queueDeclare("queue5",true,false,false,null);
            channel.queueDeclare("queue6",true,false,false,null);
            channel.queueDeclare("queue7",true,false,false,null);

            //绑定队列与交换机的关系，并设置其routingkey的关系
            channel.queueBind("queue5",exchangeName,"order");
            channel.queueBind("queue6",exchangeName,"order");
            channel.queueBind("queue7",exchangeName,"course");

                                                    //现在往order中发送消息
            channel.basicPublish(exchangeName,"course",null,message.getBytes());
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
