package org.example;

import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import java.io.File;
import java.io.FileInputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.util.*;

public class Manager {
    final private String userName;
    final public String dateFormat = "dd-MM-yyyy";

    private SecretKey secretKey;

    public Manager (String userName) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException {
        Scanner in = new Scanner(System.in);
        if (FileWR.isUserLogging(userName)){
            System.out.println("Введите ваш пароль пароль ");
            secretKey = FileWR.getSecretKey(in.nextLine(), userName);
            while (!FileWR.tryRead(userName, secretKey)){
                System.out.println("Пароль не верный, попробуйте еще раз ");
                secretKey = FileWR.getSecretKey(in.nextLine(), userName);
            }
        }
        else {
            System.out.println("Придумайте пароль ");
            secretKey = FileWR.createNewUserFile(userName, in.nextLine());
        }
        System.out.println("Добро пожаловать, " + userName +"!");
        this.userName = userName;
    }

    public void saveNewTask(String text) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException {

        Date objDate = new Date();
        SimpleDateFormat objSDF = new SimpleDateFormat(dateFormat);

        FileWR.writeFile(userName, objSDF.format(objDate) + " <=> " + text, secretKey);
    }


    public void printAllTasks(){
        FileInputStream fileInputStream = FileWR.getNewStreamForUser(userName);
        if (fileInputStream == null){
            System.out.println("Записей нет");
        }
        String bf = FileWR.getNextString(fileInputStream, secretKey);
        System.out.println("----------------------------");
        while (bf != null){
            System.out.println(bf);
            bf = FileWR.getNextString(fileInputStream, secretKey);
        }
        System.out.println("----------------------------");
    }
    public boolean findAllOnDate(String val){
        try {

            LocalDate dateMain = LocalDate.parse(val, DateTimeFormatter.ofPattern(dateFormat));

            FileInputStream fileInputStream = FileWR.getNewStreamForUser(userName);

            if (fileInputStream == null){
                System.out.println("Записей нет");
            }

            FileWR.getNextString(fileInputStream, secretKey);

            String bf = FileWR.getNextString(fileInputStream, secretKey);

            System.out.println("----------------------------");
            while (bf != null){
                String[] strings = bf.split(" <=> ");
                LocalDate dateForCompare = LocalDate.parse(strings[0], DateTimeFormatter.ofPattern(dateFormat));

                if (dateForCompare.isAfter(dateMain)){
                    break;
                }

                if (dateForCompare.equals(dateMain)){
                    System.out.println(bf);
                }

                bf = FileWR.getNextString(fileInputStream, secretKey);
            }
            System.out.println("----------------------------");
            return true;
        }
        catch (DateTimeParseException e){
            return false;
        }
    }

    public void getStatistic(){

        FileInputStream fileInputStream = FileWR.getNewStreamForUser(userName);

        FileWR.getNextString(fileInputStream, secretKey);

        int countOfLetters = 0;
        int countOfTasks = 0;
        HashMap<LocalDate, Integer> infoHash = new HashMap<>();


        String bf = FileWR.getNextString(fileInputStream, secretKey);
        while (bf != null){
            String[] strings = bf.split(" <=> ");
            LocalDate dateForCompare = LocalDate.parse(strings[0], DateTimeFormatter.ofPattern(dateFormat));
            countOfLetters += strings[1].length();
            if (!infoHash.containsKey(dateForCompare)){
                infoHash.put(dateForCompare, 1);
            }
            else {
                Integer v = infoHash.get(dateForCompare);
                v += 1;
            }
            countOfTasks++;
            bf = FileWR.getNextString(fileInputStream, secretKey);
        }

        LocalDate maxDate = null;
        if (!infoHash.keySet().isEmpty()){
            Object[] mas = infoHash.keySet().toArray();
            maxDate =(LocalDate) mas[0];

            for (int i = 1; i < mas.length; i++) {
                if (infoHash.get(mas[i]) > infoHash.get(maxDate)){
                    maxDate =(LocalDate) mas[i];
                }

            }
        }


        System.out.println("Количесто записей: " + countOfTasks);
        System.out.println("Количетсво букв в записях: " + countOfLetters);
        System.out.println("Самый активный день: " + maxDate);
    }


}
