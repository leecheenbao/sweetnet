package com.sweetNet.until;

import java.util.Properties;

public class ConfigInfo {
	public static String target = "PROD";
//	public static String target = "DEV";

	// 讀檔路徑
	public static String CONFIG_PATH = configPath(target);
	public static String REAL_PATH = realPath(target);

	public static Properties p = PropUtil.getProperty(CONFIG_PATH + "sweetNet.properties");

	public static String DASHBOARD_IMG_CONNECT_PATH = "/sweetNetImg/images_dashboard";
	public static String DASHBOARD_IMG_PATH = "/home/hsa" + DASHBOARD_IMG_CONNECT_PATH;

	public static String IMAGE_PATH = "/home/hsa/sweetNetImg/images";

	public static String PHONE_REGEX = "(09)+[\\d]{8}";
	public static String MAIL_REGEX = "^\\w{1,63}@[a-zA-Z0-9]{2,63}\\.[a-zA-Z]{2,63}(\\.[a-zA-Z]{2,63})?$";

	public static String DATA_OK = "200";
	public static String DATA_FAIL = "403";
	public static String DATA_NORESULT = "404";
	public static String DATA_ERR_SYS = "500";

	public static String SYS_MESSAGE_SUCCESS = "SUCCESS";
	public static String SYS_MESSAGE_ERROR = "ERROR";

	public static String ERROR_PHONE = "手機號碼格式錯誤";
	public static String ALREADY_REGISTER = "此信箱已經註冊過";
	public static String ERROR_MAIL = "請檢察Email";
	public static String ERROR_PWD = "請檢察密碼，需至少八碼。";
	public static String LOST_NICKNAME = "暱稱不得為空。";

	public static String MAIL_USER = p.getProperty("MAIL_USER");
	public static String MAIL_USERNAME = p.getProperty("MAIL_USERNAME");
	public static String MAIL_PASSWORD = p.getProperty("MAIL_PASSWORD");
	public static String MAIL_SUBTITLE_SINGN = "歡迎加入SugarDaddy";
	public static String MAIL_SUBTITLE_FORGET = "SugarDaddy【忘記密碼】";
	public static String MAIL_SUBTITLE_CONNECT = "SugarDaddy【來自用戶的問題】";

	public static String MAIL_SUBTITLE_REPORT_SEND = "SugarDaddy【你的檢舉已經成立】";
	public static String MAIL_SUBTITLE_REPORT_REC = "SugarDaddy【你的行為已經被檢舉】";

	public static String configPath(String init) {
		String configPath = "";
		if (init.equals("DEV")) {
			configPath = "D:/config/";
		} else if (init.equals("PROD")) {
			configPath = "/home/hsa/config/";
		}
		return configPath;
	}

	public static String realPath(String init) {
		String realPath = "";
		if (init.equals("DEV")) {
			realPath = "http://localhost:8083";
		} else if (init.equals("PROD")) {
			realPath = "https://sugarbabytw.com:8443";
		}
		return realPath;
	}
}
