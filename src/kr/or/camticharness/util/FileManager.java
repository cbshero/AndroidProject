package kr.or.camticharness.util;

import android.Manifest;
import android.bluetooth.BluetoothClass;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import kr.or.camticharness.Config;
import kr.or.camticharness.vo.DeviceData;

/**
 * Created by cbshero on 2016-11-10.
 */

public class FileManager {
//    public static final String FULL_DIR_PATH = Environment.getExternalStorageDirectory()+ Config.FILE_SAVE_PATH;

//    File m_dir;
    Context m_context;

    public  FileManager(Context context){
        m_context = context;
//        m_dir = new File(FULL_DIR_PATH);
//        if (!m_dir.exists()){
//            m_dir.mkdirs();
//        }
    }

    public String getStorageDir(){
        String strFolder = null;
        String ext = Environment.getExternalStorageState();
        if(ext.equals((Environment.MEDIA_MOUNTED))){
            strFolder = Environment.getExternalStorageDirectory().getAbsolutePath();
        }else{
            strFolder = Environment.getRootDirectory().getAbsolutePath();
        }
        File file = new File( strFolder, "/CAMTIC");
        Log.e("file", "file path is "+file.getPath());
        if(!file.exists()){
            file.mkdirs();
        }
        return strFolder+"/CAMTIC";
    }

    public String writeStringAsFile(ArrayList<DeviceData> arrayList) {
        StringBuilder fileContents = new StringBuilder();
        for(DeviceData deviceData:arrayList){
            fileContents.append(deviceData.getLog()+"\n");
//            fileContents.append(deviceData.getLog());
        }
        if(fileContents.length()>0) {
            Date currentTime = new Date();
            java.text.SimpleDateFormat fileFormat = new java.text.SimpleDateFormat("yyyyMMdd_hhmmss", Locale.KOREA);
            String strFileName = "device_data_"+fileFormat.format(currentTime)+".txt";
            String strFolderPath = null;
            try {
                strFolderPath = getStorageDir();
//                strFolderPath = "/Android/data/kr.or.camticharness";
//                File file = new File(strFolderPath);
//                if(!file.exists()){
//                    file.mkdirs();
//                }
                File fileTmp = new File(strFolderPath+"/"+strFileName);
                fileTmp.createNewFile();
                FileWriter out = new FileWriter(fileTmp);
//                Log.e("out1", fileTmp.getAbsolutePath());
//                Log.e("out2", fileTmp.getPath());
                out.write(fileContents.toString());
                out.close();
            } catch (IOException e) {
                Log.e("error", e.getMessage());
            }
            return strFolderPath+"/"+strFileName;
        }else{
            return null;
        }
    }

    public String writeStringAsFile4Log(ArrayList<String> arrayList) {
        StringBuilder fileContents = new StringBuilder();
        for(String str:arrayList){
            fileContents.append(str+"\n");
//            fileContents.append(deviceData.getLog());
        }
        if(fileContents.length()>0) {
            Date currentTime = new Date();
            java.text.SimpleDateFormat fileFormat = new java.text.SimpleDateFormat("yyyyMMdd_hhmmss", Locale.KOREA);
            String strFileName = "log_data_"+fileFormat.format(currentTime)+".txt";
            String strFolderPath = null;
            try {
                strFolderPath = getStorageDir();
//                strFolderPath = "/Android/data/kr.or.camticharness";
//                File file = new File(strFolderPath);
//                if(!file.exists()){
//                    file.mkdirs();
//                }
                File fileTmp = new File(strFolderPath+"/"+strFileName);
                fileTmp.createNewFile();
                FileWriter out = new FileWriter(fileTmp);
//                Log.e("out1", fileTmp.getAbsolutePath());
//                Log.e("out2", fileTmp.getPath());
                out.write(fileContents.toString());
                out.close();
            } catch (IOException e) {
                Log.e("error", e.getMessage());
            }
            return strFolderPath+"/"+strFileName;
        }else{
            return null;
        }
    }

}
