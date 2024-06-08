package org.example;

import javax.crypto.NoSuchPaddingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Scanner;

// Dear programmer:
// When I wrote this code, only god and
// I knew how it worked.
// Now, only god knows it!
//
// Therefore, if you are trying to optimize
// this routine and it fails (most surely),
// please increase this counter as a
// warning for the next person:
//
// total hours wasted here = не многа, или многа, не считал

public class App 
{
    public static void main( String[] args ) throws NoSuchAlgorithmException, InvalidKeySpecException,
            NoSuchPaddingException, InvalidKeyException {
        Scanner in = new Scanner(System.in);

        System.out.println("Записная книжка");
        System.out.print("Пожалуйста, введите ваш логин: ");

        Manager manager = new Manager(in.nextLine());
        String command = "";

        do {
            System.out.println("Введите команду:");
            command = in.nextLine();
            if (command.equals("#read")){

                manager.printAllTasks();

            } else if (command.equals("#find")) {

                System.out.println("Введите дату в формате: " + manager.dateFormat);
                String st = in.nextLine();

                while (!manager.findAllOnDate(st)){
                    System.out.println("Неверный формат даты, попробуйте еще.");
                    st = in.nextLine();
                }

            } else if(command.equals("#write")){

                System.out.println("Введите ваши планы на сегодня:");
                manager.saveNewTask(in.nextLine());

            }
            else if (command.equals("#statistics")){

                System.out.println("Статистика:");
                manager.getStatistic();

            }
            else if (command.equals("#exit")) {}
            else {

                System.out.println("Не известная команда, вот список:");
                System.out.println("#read");
                System.out.println("#statistics");
                System.out.println("#write");
                System.out.println("#find");
                System.out.println("#exit");

            }
        }
        while (!command.equals("#exit"));

    }
}
