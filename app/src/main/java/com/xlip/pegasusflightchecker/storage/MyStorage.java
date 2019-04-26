package com.xlip.pegasusflightchecker.storage;

import android.content.Context;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class MyStorage {
    private static MyData data;
    private final static String fileName = "pegasusFlightData.dt";


    private static void loadData(Context context) {
        File file = new File(context.getFilesDir(), fileName);
        if(!file.exists()){
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(file));
            data = ((MyData) inputStream.readObject());
        } catch (IOException e) {
            data=null;
            e.printStackTrace();
        }catch (ClassNotFoundException e2) {
            data=null;
            e2.printStackTrace();
        }
    }

    public static MyData getData(Context context) {
        loadData(context);
        return data;
    }

    public static void saveData(Context context, MyData data) {
        File file = new File(context.getFilesDir(), fileName);
        try {
            ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(file));
            outputStream.writeObject(data);
            outputStream.close();
            loadData(context);
        } catch (IOException e) {
            data=null;
            e.printStackTrace();
        }
    }

    public static void clear(Context context){
        saveData(context, null);
    }
}
