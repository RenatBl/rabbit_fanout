package ru.itis.application.consumers;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import ru.itis.application.utils.PdfConverter;

import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

public class Consumer {

    private final static String EXCHANGE_TYPE = "fanout";

    public static void main(String[] args) {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("localhost");
        System.out.println("Выберите необходимый генератор:\n" +
                "1- Заявление на увольнение\n" +
                "2- Заявление на отчисление\n" +
                "3 - Заявление на академический отпуск\n" +
                "4 - Заявление на зачисление");
        String exchangeNum = new Scanner(System.in).nextLine();
        String exchangeName;
        switch (exchangeNum) {
            case "1":
                exchangeName = "dismissal";
                break;
            case "2":
                exchangeName = "reduction";
                break;
            case "3":
                exchangeName = "academic_leave";
                break;
            case "4":
                exchangeName = "enrollment";
                break;
            default:
                throw new IllegalArgumentException("Wrong data");
        }
        try {
            Connection connection = connectionFactory.newConnection();
            Channel channel = connection.createChannel();
            channel.basicQos(3);

            channel.exchangeDeclare(exchangeName, EXCHANGE_TYPE);
            String queue = channel.queueDeclare().getQueue();

            channel.queueBind(queue, exchangeName, "");

            DeliverCallback deliverCallback = (consumerTag, message) -> {
                String data = new String(message.getBody());
                PdfConverter pdfConverter = new PdfConverter("C:\\java2021\\JavaLab\\rabbit_fanout\\files");
                System.out.println("Start creating document");
                try {
                    pdfConverter.createDocument(data);
                    System.out.println("Finish creating document");
                    channel.basicAck(message.getEnvelope().getDeliveryTag(), false);
                } catch (IOException e) {
                    System.err.println("FAILED");
                    channel.basicReject(message.getEnvelope().getDeliveryTag(), false);
                }
            };

            channel.basicConsume(queue, false, deliverCallback, consumerTag -> {
            });
        } catch (IOException | TimeoutException e) {
            throw new IllegalArgumentException(e);
        }
    }
}
