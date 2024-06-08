package org.example;

import javax.crypto.*;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import javax.imageio.IIOException;
import java.io.*;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Arrays;
import java.util.*;

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
            byte[] data = fileInputStream.readNBytes(16);
            byte[] plainText = cipher.doFinal(data);
            return (new String(plainText)).equals(userName);
        }
        catch (IOException | IllegalBlockSizeException | BadPaddingException e){
            return false;
        }
    }

    static boolean writeFile(String userName, String value, SecretKey secretKey) throws InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        try {
            FileOutputStream stream = new FileOutputStream(filePath + userName + ".bin", false);

            byte[] data = value.getBytes(StandardCharsets.UTF_8);
            byte[] plainText = cipher.doFinal(data);

            stream.write(plainText);
            stream.flush();
            stream.close();
            return true;
        }
        catch (IOException | IllegalBlockSizeException | BadPaddingException exception){
            return false;
        }
    }

    public static SecretKey getSecretKey(String password, String salt) throws NoSuchAlgorithmException, InvalidKeySpecException {
            SecretKeyFactory factory = SecretKeyFactory.getInstance(alg);
            KeySpec spec = new PBEKeySpec(password.toCharArray(), salt.getBytes(), 65536, 256);
            return new SecretKeySpec(factory.generateSecret(spec).getEncoded(), "AES");
    }
}
