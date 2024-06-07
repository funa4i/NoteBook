package org.example;

import java.io.File;

public class Manager {
    private String userName;

    public Manager (String userName){
        try {
            FileWR.createNewUserFile(userName, "123");
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

}
