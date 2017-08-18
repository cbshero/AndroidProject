package kr.or.camticharness.util;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.util.ArrayList;

import kr.or.camticharness.AutoModeActivity;
import kr.or.camticharness.Config;
import kr.or.camticharness.vo.DeviceData;

/**
 * Created by cbshero on 2016-11-09.
 */

public class ProtocolControl {
    public static final byte STX = (byte)0x02;
    public static final byte ETX = (byte)0x03;
    public static final byte[] LENGTH = {(byte)0x30, (byte)0x30, (byte)0x31, (byte)0x35};
    public static final byte[] MODE = {(byte)0x4D, (byte)0x4F, (byte)0x44, (byte)0x45, (byte)0x2D};
    public static final byte[] POWER = {(byte)0x50, (byte)0x4F, (byte)0x57, (byte)0x45, (byte)0x52};
    public static final byte[] START = {(byte)0x53, (byte)0x54, (byte)0x41, (byte)0x52, (byte)0x54};
    // 02:48:48:49:53:83:84:65:82:84:48:48:48:48:48
    // 0 0 1 5 S T A R T
    public static final byte[] STOP = {(byte)0x53, (byte)0x54, (byte)0x4F, (byte)0x50, (byte)0x2D};
    public static final byte[] UP = {(byte)0x55, (byte)0x50, (byte)0x2D, (byte)0x2D, (byte)0x2D};
    public static final byte[] DOWN = {(byte)0x44, (byte)0x4F, (byte)0x57, (byte)0x4E, (byte)0x2D};
    public static final byte[] FORWARD = {(byte)0x46, (byte)0x4F, (byte)0x52, (byte)0x57, (byte)0x41};
    public static final byte[] BACKWARD = {(byte)0x42, (byte)0x41, (byte)0x43, (byte)0x4B, (byte)0x57};
    public static final byte[] WEIGHT = {(byte)0x57, (byte)0x45, (byte)0x49, (byte)0x47, (byte)0x48};
    public static final byte[] MSPEED = {(byte)0x4D, (byte)0x2D, (byte)0x53, (byte)0x50, (byte)0x44};
    public static final byte DOT = (byte)0x2E;
    public static final byte ONE = (byte)0x31;
    public static final byte ZERO = (byte)0x30;

    public static ArrayList<DeviceData> m_arrDeviceData;
    public static ArrayList<String> m_arrStringData;

    private Socket m_socket;
    private PostThread m_pt;
    private PrintWriter m_pw;

    private AutoModeListener m_listener;

    public boolean m_blConnectServer = false;

    public ProtocolControl(AutoModeListener listener){
        m_listener = listener;
        m_arrDeviceData = new ArrayList<>();
        m_arrStringData = new ArrayList<>();
    // Thread로 웹서버에 접속
        new Thread() {
            public void run() {
                try {
                    m_socket = new Socket(Config.SVC_URL, Config.SVC_PORT);
                    if(m_socket.isConnected()) {
                        Log.e("connect", m_socket.getKeepAlive()+"==>connect");
                        m_blConnectServer = true;

                        OutputStream os = m_socket.getOutputStream();
                        m_pw = new PrintWriter(new OutputStreamWriter(os));

                        m_pt = new PostThread(m_socket, m_listener);
                        m_pt.start();
                    }else{
                        Log.e("not connect", m_socket.getKeepAlive()+"==>not connect");
                        m_blConnectServer = false;
                    }
                } catch (IOException e) {
                    m_blConnectServer = false;
                }
            }
        }.start();
    }

    public void sendCommand(byte[] bytes){
        String byteToString = null;
        try {
            byteToString = new String(bytes,0,bytes.length,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
//        Log.e("byte", byteToString);
        if(m_pw!=null) {
            m_pw.print(byteToString);
            m_pw.flush();
        }
    }

    public void close() throws IOException {
        if(m_pt!=null)
            m_pt.stop = true;
        if(m_pw != null)
            m_pw.close();
        if(m_socket != null)
            m_socket.close();
    }
}

class PostThread extends Thread{
    public PrintWriter  pw= null;
    public BufferedReader br = null;
    public boolean stop = false;
    public AutoModeListener m_listener;

    public PostThread(Socket socket, AutoModeListener listener){
        m_listener = listener;
        try{
            InputStream is = socket.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            this.br  = new BufferedReader(isr);
        }catch(Exception e){
            System.out.println("예외" + e.getMessage());
        }
    }
    public void run(){
        try{
            //동시작업 쓰레드 코드
            String msg = null;
            while((msg = br.readLine()) != null){
//                Log.e("test", msg);
                ProtocolControl.m_arrStringData.add(msg);
                if(stop)
                    break;
                if(msg.substring(0,1).getBytes()[0] == ProtocolControl.STX){
                    DeviceData deviceData = new DeviceData();
                    deviceData.setLoadcell(Double.parseDouble(msg.substring(5,10)));
                    deviceData.setForward_backward(Double.parseDouble(msg.substring(10,15)));
                    deviceData.setLeft_right(Double.parseDouble(msg.substring(15,20)));
                    deviceData.setSetting_weight(Double.parseDouble(msg.substring(20,25)));
                    deviceData.setPower(Integer.parseInt(msg.substring(25,26)));
                    deviceData.setAuto_mode(Integer.parseInt(msg.substring(26,27)));
                    deviceData.setManual_mode(Integer.parseInt(msg.substring(27,28)));
                    deviceData.setAuto_start(Integer.parseInt(msg.substring(28,29)));
                    deviceData.setStop(Integer.parseInt(msg.substring(29,30)));
                    deviceData.setUp(Integer.parseInt(msg.substring(30,31)));
                    deviceData.setDown(Integer.parseInt(msg.substring(31,32)));
                    deviceData.setForward(Integer.parseInt(msg.substring(32,33)));
                    deviceData.setBackward(Integer.parseInt(msg.substring(33,34)));
                    deviceData.setEmergency(Integer.parseInt(msg.substring(34,35)));
                    deviceData.setLog(msg);
                    Log.e("Start / Stop Data", msg.substring(28,29) + "/" + msg.substring(29,30));

                    ProtocolControl.m_arrDeviceData.add(deviceData);
                    m_listener.receiveData(deviceData);
//                    m_listener.receiveData(deviceData);
                    //처리
                }
            }
        }catch(Exception e){
            System.out.println("예외" + e.getMessage());
        }finally{
            try{
                pw.close();
                br.close();
            }catch(Exception e){}
        }
    }
}

