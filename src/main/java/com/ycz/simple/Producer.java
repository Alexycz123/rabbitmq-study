package com.ycz.simple;/*
 @author ycz
 @date 2022-03-02-9:29
*/

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class Producer {
    public static void main(String[] args) {

        //所有的中间件技术都是基于tcp/ip协议基础上构建新型的协议规范，只不过rabbitmq遵循的是amqp

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
            String queueName="queue33";
            /**
             * 队列名称，
             * 是否持久化，false就是不持久化。
             * 排他性
             * 是否自动删除（随着最后一个消费者消费完以后是否把队列删除
             * 携带一些附属参数
             */
            channel.queueDeclare(queueName,false,false,false,null);
            //5.准备消息内容
            String message = "阿斯弗萨芬我法大师傅士大夫人发放日格尔";
            //6.发送消息给队列queue
            //                    交换机     ， 队列     ， 消息控制状态， 消息主题
            // 可以存在没有交换机的队列嘛？ 不可能，虽然没有交换机，但一定存在默认交换机
            channel.basicPublish("",queueName,null,message.getBytes());
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
