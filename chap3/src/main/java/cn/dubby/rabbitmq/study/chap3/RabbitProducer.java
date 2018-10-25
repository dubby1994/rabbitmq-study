package cn.dubby.rabbitmq.study.chap3;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

public class RabbitProducer {

    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("127.0.0.1");
        factory.setPort(5672);
        factory.setUsername("dubby");
        factory.setPassword("dubby");
        //创建连接
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        String message = "Hello, world";
        channel.basicPublish("chap3_exchange", "chap3_routing_key", MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes(StandardCharsets.UTF_8));

        //关闭资源
        channel.close();
        connection.close();
    }

}
