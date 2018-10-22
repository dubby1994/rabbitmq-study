package cn.dubby.rabbitmq.study.chap2;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.TimeoutException;

public class RabbitTopicProducer {

    private static final String EXCHANGE_NAME = "exchange_topic_demo";

    private static final String ROUTING_KEY = "*.dubby.cn";

    private static final String[] QUEUE_NAME_ARRAY = {"topic_queue_1", "topic_queue_2", "topic_queue_3"};

    private static final String HOST = "127.0.0.1";

    private static final int PORT = 5672;

    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(HOST);
        factory.setPort(PORT);
        factory.setUsername("dubby");
        factory.setPassword("dubby");

        //创建连接
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        //创建一个direct的，持久化的，不自动删除的exchange
        channel.exchangeDeclare(EXCHANGE_NAME, "fanout", true, false, null);

        for (String queueName : QUEUE_NAME_ARRAY) {
            //创建一个持久化的，排他的，不自动删除的queue
            channel.queueDeclare(queueName, true, false, false, null);
            //将exchange和queue通过routingKey绑定
            channel.queueBind(queueName, EXCHANGE_NAME, ROUTING_KEY);
        }


        while (true) {
            String message = "Hello, world " + System.currentTimeMillis();
            channel.basicPublish(EXCHANGE_NAME, System.currentTimeMillis() + ".dubby.cn", MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes(StandardCharsets.UTF_8));
        }

        //关闭资源
        //channel.close();
        //connection.close();
    }

}
