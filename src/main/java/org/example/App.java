package org.example;

import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Scanner;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws NoSuchAlgorithmException, InvalidKeySpecException,
            NoSuchPaddingException, InvalidKeyException {
        Scanner in = new Scanner(System.in);
        System.out.println("Записная книжка");
        System.out.println("Пожалуйста, введите ваш логин: ");
        Manager manager = new Manager(in.nextLine());
        String command = "";
        do {
            System.out.println("Введите команду:");
            command = in.nextLine();
            if (command.equals("#read")){
                manager.printAllTasks();
            }
            else if(command.equals("#write")){
                System.out.println("Введите ваши планы на сегодня:");
                manager.saveNewTask(in.nextLine());
            }
            else if (command.equals("#statistics")){
                System.out.println("Статистика:");
                manager.getStatistic();
            } else if (command.equals("#exit")) {}
            else {
                System.out.println("Не известная команда, вот список:");
                System.out.println("#read");
                System.out.println("#statistics");
                System.out.println("#write");
                System.out.println("#exit");
            }
        }
        while (!command.equals("#exit"));

    }
}
