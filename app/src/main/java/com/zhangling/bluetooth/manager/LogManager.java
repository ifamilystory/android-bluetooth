package com.zhangling.bluetooth.manager;

import com.zhangling.bluetooth.Application;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class LogManager {
    private static volatile LogManager instance=null;
    private LogManager (){

    }
    public static  LogManager getInstance(){
        if(instance==null){
            synchronized(LogManager.class){
                instance=new LogManager ();
            }
        }
        return instance;
    }
    public void createFile(String fileName){
        addSuffix(fileName);
        File file = new File(Application.getApplication().getFilesDir(),fileName);
        if (!file.exists()) {
            boolean result; // 文件是否创建成功
            try {
                result = file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
            if (!result) {
                return;
            }
        }
    }

    public void writeLog(String log,String fileName) {
        log = log + " \n";
        FileOutputStream outputStream;
        createFile(fileName);
        try {
            outputStream = Application.getApplication().openFileOutput(fileName, Application.getApplication().MODE_APPEND);
            outputStream.write(log.getBytes());
            outputStream.flush();
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addSuffix(String fileName){
        if (!fileName.endsWith(".log")){
            fileName = fileName + ".log";
        }
    }


}
