package cn.dubby.rabbitmq.study.chap3;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeoutException;

public class RabbitConsumer {

    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
        Address[] addresses = new Address[]{
                new Address("127.0.0.1", 5672)
        };

        ConnectionFactory factory = new ConnectionFactory();
        factory.setUsername("dubby");
        factory.setPassword("dubby");

        Connection connection = factory.newConnection(addresses);
        Channel channel = connection.createChannel();
        channel.basicQos(64);

        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                System.out.println("received message:\t" + new String(body, StandardCharsets.UTF_8));
                channel.basicAck(envelope.getDeliveryTag(), false);
            }
        };

        channel.basicConsume("chap3_queue", consumer);
        new CountDownLatch(1).await();
    }

}
