package org.example;

import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import java.io.File;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Scanner;

public class Manager {
    final private String userName;

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
            System.out.println("Добро пожаловать, " + userName);
        }
        else {
            System.out.println("Придумайте пароль ");
            secretKey = FileWR.createNewUserFile(userName, in.nextLine());
        }
        this.userName = userName;
    }

    public void saveNewTask(){

    }


}
