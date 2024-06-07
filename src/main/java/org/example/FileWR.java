package org.example;

import javax.crypto.*;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import javax.imageio.IIOException;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Arrays;

public class FileWR {
    static final private String alg = "PBKDF2WithHmacSHA256";
    static SecretKey createNewUserFile(String userName, String password) throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, InvalidKeyException {
        File file = new File("src\\main\\resources\\" + userName + ".txt");
        SecretKey key = FileWR.getSecretKey(password, userName);
        writeFile(userName, userName, key);
        return key;
    }

    static boolean tryRead(String userName, SecretKey secretKey) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException{
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        File file = new File("src\\main\\resources\\" + userName + ".txt");
        if (!file.exists()){
            return false;
        }
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file, StandardCharsets.UTF_8));
            byte[] plainText = cipher.doFinal(reader.readLine().getBytes(StandardCharsets.UTF_8));
            return Arrays.toString(plainText).equals(userName);

        }
        catch (IOException | IllegalBlockSizeException | BadPaddingException e){
            return false;
        }
    }

    static boolean writeFile(String userName, String value, SecretKey secretKey) throws InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("src\\main\\resources\\" + userName + ".txt", false));
            byte[] data = value.getBytes(StandardCharsets.UTF_8);
            String st = Arrays.toString(data);
            writer.newLine();
            if (userName.equals(value)){
                writer.write(Arrays.toString(data));
                return true;
            }
            return true;
        }
        catch (IOException exception){
            return false;
        }
    }


    private static SecretKey getSecretKey(String password, String salt) throws NoSuchAlgorithmException, InvalidKeySpecException {

            SecretKeyFactory factory = SecretKeyFactory.getInstance(alg);
            KeySpec spec = new PBEKeySpec(password.toCharArray(), salt.getBytes(), 65536, 256);
            return new SecretKeySpec(factory.generateSecret(spec).getEncoded(), "AES");
    }
}
