package kr.or.camticharness.util;

import java.io.UnsupportedEncodingException;

/**
 * Created by cbshero on 2016-11-10.
 */

public class CommandManager {
    public static byte[] makeCommand(String strCommand, int data) {
        byte[] command = new byte[16];
        command[0] = ProtocolControl.STX;
        for (int i = 1; i < ProtocolControl.LENGTH.length+1; i++) {
            command[i] = ProtocolControl.LENGTH[i - 1];
        }
        if(strCommand.equals("weight")) {
            for (int i = 5; i < ProtocolControl.WEIGHT.length + 5; i++) {
                command[i] = ProtocolControl.WEIGHT[i - 5];
            }
            //data
            try {
                byte[] bytes = String.valueOf(data).getBytes("US-ASCII");
                if (data < 10) {
                    command[10] = 0x30;
                    command[11] = 0x30;
                    command[12] = 0x30;
                    command[13] = 0x30;
                    command[14] = bytes[0];
                } else if (data < 100) {
                    command[10] = 0x30;
                    command[11] = 0x30;
                    command[12] = 0x30;
                    command[13] = bytes[0];
                    command[14] = bytes[1];
                } else if (data < 1000) {
                    command[10] = 0x30;
                    command[11] = 0x30;
                    command[12] = bytes[0];
                    command[13] = bytes[1];
                    command[14] = bytes[2];
                } else if (data < 10000) {
                    command[10] = 0x30;
                    command[11] = bytes[0];
                    command[12] = bytes[1];
                    command[13] = bytes[2];
                    command[14] = bytes[3];
                } else {
                    command[10] = bytes[0];
                    command[11] = bytes[1];
                    command[12] = bytes[2];
                    command[13] = bytes[3];
                    command[14] = bytes[4];
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }else if(strCommand.equals("speed")){
            for (int i = 5; i < ProtocolControl.MSPEED.length + 5; i++) {
                command[i] = ProtocolControl.MSPEED[i - 5];
            }
            //data
            try {
                byte[] bytes = String.valueOf(data).getBytes("US-ASCII");
                if (data < 10) {
                    command[10] = 0x30;
                    command[11] = 0x30;
                    command[12] = 0x30;
                    command[13] = 0x30;
                    command[14] = bytes[0];
                } else {
                    command[10] = 0x30;
                    command[11] = 0x30;
                    command[12] = 0x30;
                    command[13] = bytes[0];
                    command[14] = bytes[1];
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }else {
            if (strCommand.equals("mode")) {
                for (int i = 5; i < ProtocolControl.MODE.length+5; i++) {
                    command[i] = ProtocolControl.MODE[i - 5];
                }
            } else if (strCommand.equals("power")) {
                for (int i = 5; i < ProtocolControl.POWER.length+5; i++) {
                    command[i] = ProtocolControl.POWER[i - 5];
                }
            } else if (strCommand.equals("start")) {
                for (int i = 5; i < ProtocolControl.START.length+5; i++) {
                    command[i] = ProtocolControl.START[i - 5];
                }
            } else if (strCommand.equals("stop")) {
                for (int i = 5; i < ProtocolControl.STOP.length+5; i++) {
                    command[i] = ProtocolControl.STOP[i - 5];
                }
            } else if (strCommand.equals("up")) {
                for (int i = 5; i < ProtocolControl.UP.length+5; i++) {
                    command[i] = ProtocolControl.UP[i - 5];
                }
            } else if (strCommand.equals("down")) {
                for (int i = 5; i < ProtocolControl.DOWN.length+5; i++) {
                    command[i] = ProtocolControl.DOWN[i - 5];
                }
            } else if (strCommand.equals("forward")) {
                for (int i = 5; i < ProtocolControl.FORWARD.length+5; i++) {
                    command[i] = ProtocolControl.FORWARD[i - 5];
                }
            } else if (strCommand.equals("backward")) {
                for (int i = 5; i < ProtocolControl.BACKWARD.length+5; i++) {
                    command[i] = ProtocolControl.BACKWARD[i - 5];
                }
            }
            //data
            for (int i = 10; i < 15; i++) {
                if (data == 0) {
                    command[i] = 0x30;
                } else {
                    if (i == 14) {
                        command[i] = 0x31;
                    } else {
                        command[i] = 0x30;
                    }
                }
            }
        }
        command[command.length-1] = ProtocolControl.ETX;
        return command;
    }
}
