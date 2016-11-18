package kr.or.camticharness.vo;

public class Statics {
	private int sid;
	private int user_sid;
	private int practice_type;
	private double forward_backward;
	private double left_right;
	private int speed;
	private double weight_rate;
	private int practice_time;
	private String regdate;

	public int getPractice_type() {
		return practice_type;
	}

	public void setPractice_type(int practice_type) {
		this.practice_type = practice_type;
	}

	public double getForward_backward() {
		return forward_backward;
	}

	public void setForward_backward(double forward_backward) {
		this.forward_backward = forward_backward;
	}

	public int getSpeed() {
		return speed;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}

	public double getLeft_right() {
		return left_right;
	}

	public void setLeft_right(double left_right) {
		this.left_right = left_right;
	}

	public int getPractice_time() {
		return practice_time;
	}

	public void setPractice_time(int practice_time) {
		this.practice_time = practice_time;
	}

	public String getRegdate() {
		return regdate;
	}

	public void setRegdate(String regdate) {
		this.regdate = regdate;
	}

	public int getSid() {
		return sid;
	}

	public void setSid(int sid) {
		this.sid = sid;
	}

	public int getUser_sid() {
		return user_sid;
	}

	public void setUser_sid(int user_sid) {
		this.user_sid = user_sid;
	}

	public double getWeight_rate() {
		return weight_rate;
	}

	public void setWeight_rate(double weight_rate) {
		this.weight_rate = weight_rate;
	}
}
