package ru.itis.application.producers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import ru.itis.application.models.PersonalData;

import java.io.IOException;
import java.util.Date;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

public class Producer {

    private final static String[] EXCHANGES = {
            "dismissal",
            "reduction",
            "academic_leave",
            "enrollment"
    };

    private final static String EXCHANGE_TYPE = "fanout";

    public static void main(String[] args) {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("localhost");

        try {
            Connection connection = connectionFactory.newConnection();
            Channel channel = connection.createChannel();

            for (String exchange : EXCHANGES) {
                channel.exchangeDeclare(exchange, EXCHANGE_TYPE);
            }

            ObjectMapper objectMapper = new ObjectMapper();
            PersonalData personalData = getData();
            byte[] bytes = objectMapper.valueToTree(personalData).asText().getBytes();

            for (String exchange : EXCHANGES) {
                channel.basicPublish(exchange, "",null, bytes);
            }
        } catch (IOException | TimeoutException e) {
            throw new IllegalArgumentException(e);
        }
    }

    private static PersonalData getData() {
        Scanner in = new Scanner(System.in);

        String docType;
        String name;
        String surname;
        String patronymic;
        String age;
        String passportSeries;
        String passportNumber;
        Date issueDate = new Date();
        Date filingDate = new Date();

        System.out.println("Выберите тип заявления:\n" +
                "1 - заявление на увольнение\n" +
                "2 - заявление на отчисление\n" +
                "3 - заявление на получение академического отпуска\n" +
                "4 - заявление на зачисление");
        String type = in.nextLine();
        switch (type) {
            case "1":
                docType = "dismissal";
                break;
            case "2":
                docType = "reduction";
                break;
            case "3":
                docType = "academic_leave";
                break;
            case "4":
                docType = "enrollment";
                break;
            default:
                throw new IllegalArgumentException("Wrong data");
        }
        System.out.println("Введите Ваше имя");
        name = in.nextLine();
        System.out.println("Введите Вашу фамилию");
        surname = in.nextLine();
        System.out.println("Введите Ваше отчество");
        patronymic = in.nextLine();
        System.out.println("Введите Ваш возраст");
        age = in.nextLine();
        System.out.println("Введите серию паспорта");
        passportSeries = in.nextLine();
        System.out.println("Введите номер паспорта");
        passportNumber = in.nextLine();
        System.out.println("Введите дату получения паспорта");
        System.out.println("День");
        issueDate.setDate(in.nextInt());
        System.out.println("Месяц");
        issueDate.setMonth(in.nextInt());
        System.out.println("Год");
        issueDate.setYear(in.nextInt());
        System.out.println("Введите дату подачи заявления");
        System.out.println("День");
        filingDate.setDate(in.nextInt());
        System.out.println("Месяц");
        filingDate.setMonth(in.nextInt());
        System.out.println("Год");
        filingDate.setYear(in.nextInt());

        return PersonalData.builder()
                .documentType(docType)
                .name(name)
                .surname(surname)
                .patronymic(patronymic)
                .age(age)
                .passportSeries(passportSeries)
                .passportNumber(passportNumber)
                .issueDate(issueDate)
                .filingDate(filingDate)
                .build();
    }
}