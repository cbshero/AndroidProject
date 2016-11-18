package kr.or.camticharness.util;

import android.bluetooth.BluetoothClass;
import android.content.Context;
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
//        File file = new File( Environment.getExternalStoragePublicDirectory( Environment.DIRECTORY_DOWNLOADS), albumName);
        String strFolder = m_context.getFilesDir().getAbsolutePath();
        File file = new File(strFolder);
        Log.e("file", "file path is "+file.getPath());
        if(!file.exists()){
            file.mkdirs();
            strFolder = file.getAbsolutePath();
        }
        return strFolder;
    }

    public String writeStringAsFile(ArrayList<DeviceData> arrayList) {
        String fileContents = null;
        for(DeviceData deviceData:arrayList){
            fileContents += deviceData.getLog()+"\n\r";
        }
        if(fileContents!=null) {
            Date currentTime = new Date();
            java.text.SimpleDateFormat fileFormat = new java.text.SimpleDateFormat("yyyyMMdd_hhmmss", Locale.KOREA);
            String strFileName = "device_data_"+fileFormat.format(currentTime)+".csv";
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
                out.write(fileContents);
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
