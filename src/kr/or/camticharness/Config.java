package kr.or.camticharness;

import java.util.UUID;

public class Config {
	public static final int VERSION_NUM = 1;
	public static final String SEX[] = {"남성", "여성"};
	
//	public static final String SVC_URL = "http://tredio.iptime.org";
	public static final String SVC_URL = "169.254.207.32";
//	public static final String SVC_URL = "192.168.0.101";
	public static final int SVC_PORT = 6340;

	public static final String FILE_SAVE_PATH = "/CamticHarness/";
	// Service handler message key
	public static final String SERVICE_HANDLER_MSG_KEY_DEVICE_NAME = "device_name";
	public static final String SERVICE_HANDLER_MSG_KEY_DEVICE_ADDRESS = "device_address";
	public static final String SERVICE_HANDLER_MSG_KEY_TOAST = "toast";

	public static final double FORW_BACK_MAX = 1.34;
	public static final double FORW_BACK_MIN = 0.4;
	public static final double LEFT_RIGHT_MAX = 2.84;
	public static final double LEFT_RIGHT_MIN = 0.4;
    // Preference
	public static final String PREFERENCE_NAME = "btchatPref";
	public static final String PREFERENCE_KEY_BG_SERVICE = "BackgroundService";
	public static final String PREFERENCE_CONN_INFO_ADDRESS = "device_address";
	public static final String PREFERENCE_CONN_INFO_NAME = "device_name";
	
    // Message types sent from Service to Activity
    public static final int MESSAGE_CMD_ERROR_NOT_CONNECTED = -50;
    
    public static final int MESSAGE_BT_STATE_INITIALIZED = 1;
    public static final int MESSAGE_BT_STATE_LISTENING = 2;
    public static final int MESSAGE_BT_STATE_CONNECTING = 3;
    public static final int MESSAGE_BT_STATE_CONNECTED = 4;
    public static final int MESSAGE_BT_STATE_ERROR = 10;
    
    public static final int MESSAGE_READ_CHAT_DATA = 201;
    
	// Intent request codes
	public static final int REQUEST_CONNECT_DEVICE = 1;
	public static final int REQUEST_ENABLE_BT = 2;
	public static final int CONNECTED_DEVICE_BT = 3;
	public static final int CONNECTED_FAIL_BT = 4;
	public static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

	// game
	public static final int GAME_TOTAL_REPEAT = 100;
	public static final int PENUGUIN_TOTAL_HURDLE = 10;
	public static final int PENUGUIN_HURDLE_COUNT = 6;
	public static final int PENUGUIN_HURDLE_LINE = 5;

	//hurdle x,y
	public static final int[][][] PENGUIN_HURDLE_POSTION = {
			{{350, 450, 94, 83}, {280, 600, 120, 105}, {230, 750, 150, 132}, {170, 900, 180, 158}},//1번 라인
			{{580, 450, 94, 83}, {550, 600, 120, 105}, {520, 750, 150, 132}, {490, 900, 180, 158}},//2번 라인
			{{810, 450, 94, 83}, {800, 600, 120, 105}, {790, 750, 150, 132}, {780, 900, 180, 158}},//3번 라인
			{{1050, 450, 94, 83}, {1070, 600, 120, 105}, {1090, 750, 150, 132}, {1100, 900, 180, 158}},//4번 라인
			{{1280, 450, 94, 83}, {1300, 600, 120, 105}, {1370, 750, 150, 132}, {1400, 900, 180, 158}}//5번 라인
	};
}
