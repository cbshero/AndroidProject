package kr.or.camticharness.vo;

/**
 * Created by cbshero on 2016-11-09.
 */

public class DeviceData {
    private double loadcell;
    private double forward_backward;
    private double left_right;
    private double setting_weight;
    private int power;
    private int auto_mode;
    private int manual_mode;
    private int auto_start;
    private int stop;
    private int up;
    private int down;
    private int forward;
    private int backward;
    private int emergency;
    private String log;

    public String getLog() {
        return log;
    }

    public void setLog(String log) {
        this.log = log;
    }

    public int getAuto_mode() {
        return auto_mode;
    }

    public void setAuto_mode(int auto_mode) {
        this.auto_mode = auto_mode;
    }

    public int getAuto_start() {
        return auto_start;
    }

    public void setAuto_start(int auto_start) {
        this.auto_start = auto_start;
    }

    public int getBackward() {
        return backward;
    }

    public void setBackward(int backward) {
        this.backward = backward;
    }

    public int getDown() {
        return down;
    }

    public void setDown(int down) {
        this.down = down;
    }

    public int getEmergency() {
        return emergency;
    }

    public void setEmergency(int emergency) {
        this.emergency = emergency;
    }

    public int getForward() {
        return forward;
    }

    public void setForward(int forward) {
        this.forward = forward;
    }

    public double getForward_backward() {
        return forward_backward;
    }

    public void setForward_backward(double forward_backward) {
        this.forward_backward = forward_backward;
    }

    public double getLeft_right() {
        return left_right;
    }

    public void setLeft_right(double left_right) {
        this.left_right = left_right;
    }

    public double getLoadcell() {
        return loadcell;
    }

    public void setLoadcell(double loadcell) {
        this.loadcell = loadcell;
    }

    public int getManual_mode() {
        return manual_mode;
    }

    public void setManual_mode(int manual_mode) {
        this.manual_mode = manual_mode;
    }

    public int getPower() {
        return power;
    }

    public void setPower(int power) {
        this.power = power;
    }

    public double getSetting_weight() {
        return setting_weight;
    }

    public void setSetting_weight(double setting_weight) {
        this.setting_weight = setting_weight;
    }

    public int getStop() {
        return stop;
    }

    public void setStop(int stop) {
        this.stop = stop;
    }

    public int getUp() {
        return up;
    }

    public void setUp(int up) {
        this.up = up;
    }
}
