package cn.dubby.rabbitmq.study.chap2;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeoutException;

public class RabbitTopicConsumer {

    private static final String[] QUEUE_NAME_ARRAY = {"topic_queue_1", "topic_queue_2", "topic_queue_3"};

    private static final String HOST = "127.0.0.1";

    private static final int PORT = 5672;

    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
        Address[] addresses = new Address[]{
                new Address(HOST, PORT)
        };

        ConnectionFactory factory = new ConnectionFactory();
        factory.setUsername("dubby");
        factory.setPassword("dubby");

        Connection connection = factory.newConnection(addresses);
        Channel channel = connection.createChannel();
        channel.basicQos(64);

        for (String queueName : QUEUE_NAME_ARRAY) {
            Consumer consumer = new DefaultConsumer(channel) {
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                    System.out.println(queueName + " received message:\t" + new String(body, StandardCharsets.UTF_8));
                    channel.basicAck(envelope.getDeliveryTag(), false);
                }
            };

            channel.basicConsume(queueName, consumer);
        }


        new CountDownLatch(1).await();
    }

}
