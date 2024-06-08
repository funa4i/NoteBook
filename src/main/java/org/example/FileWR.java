package org.example;

import javax.crypto.*;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FileWR {
    static final private String alg = "PBKDF2WithHmacSHA256";
    static final private String filePath = "src\\main\\resources\\";
    static SecretKey createNewUserFile(String userName, String password) throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, InvalidKeyException {
        File file = new File(filePath + userName + ".bin");
        SecretKey key = FileWR.getSecretKey(password, userName);
        writeFile(userName, userName, key);
        return key;
    }

    public static boolean isUserLogging(String userName){
        File file = new File(filePath + userName + ".bin");
        return file.exists();
    }

    static boolean tryRead(String userName, SecretKey secretKey) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException{
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        File file = new File(filePath + userName + ".bin");
        if (!file.exists()){
            return false;
        }
        try {
            FileInputStream fileInputStream = new FileInputStream(file);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            byte b = (byte)fileInputStream.read();
            if (b == -1){
                return false;
            }
            baos.write(b);


            while (b != " ".getBytes()[0]){
                b = (byte)fileInputStream.read();
                baos.write(b);
            }

            byte[] plainText = fileInputStream.readNBytes( Integer.parseInt(baos.toString().strip()) * 16);
            byte[] data  = cipher.doFinal(plainText);

            return (new String(data)).equals(userName);
        }
        catch (IOException | IllegalBlockSizeException | BadPaddingException e){
            return false;
        }
    }

    static void writeFile(String userName, String value, SecretKey secretKey) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        try {
            FileOutputStream stream = new FileOutputStream(filePath + userName + ".bin", true);
            byte[] data = value.getBytes();
            byte[] plainText = cipher.doFinal(data);

            stream.write(Integer.valueOf(plainText.length / 16).toString().getBytes());
            stream.write(" ".getBytes());
            stream.write(plainText);
            stream.flush();
            stream.close();

        }
        catch (IOException | IllegalBlockSizeException | BadPaddingException ignored){}
    }

    static String getNextString(FileInputStream fileInputStream, SecretKey secretKey){

        try {
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            byte b = (byte)fileInputStream.read();
            if (b == -1){
                return null;
            }
            baos.write(b);

            while (b != " ".getBytes()[0]){
                b = (byte)fileInputStream.read();
                baos.write(b);
            }

            byte[] plainText = fileInputStream.readNBytes(Integer.parseInt(baos.toString().strip()) * 16);
            byte[] data =  cipher.doFinal(plainText);
            return new String(data);
        }
        catch (Exception e){
            return null;
        }
    }
    static public FileInputStream getNewStreamForUser(String userName){
        try {
            return new FileInputStream(filePath + userName + ".bin");
        }
        catch (FileNotFoundException exception){
            return null;
        }
    }

    public static SecretKey getSecretKey(String password, String salt) throws NoSuchAlgorithmException, InvalidKeySpecException {
            SecretKeyFactory factory = SecretKeyFactory.getInstance(alg);
            KeySpec spec = new PBEKeySpec(password.toCharArray(), salt.getBytes(), 65536, 256);
            return new SecretKeySpec(factory.generateSecret(spec).getEncoded(), "AES");
    }
}
