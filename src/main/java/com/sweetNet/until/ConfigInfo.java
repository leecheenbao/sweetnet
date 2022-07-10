package com.sweetNet.until;

import java.util.Properties;

public class ConfigInfo {
	public static String target = "dev";

	// 讀檔路徑
//	public static String FILE_PATH = "/home/hsa/config/";
	public static String REAL_PATH = "http://sugarbabytw.com:8083";
	public static String FILE_PATH = "D:/config/";
//	public static String REAL_PATH = "http://localhost:8083";

	public static Properties p = PropUtil.getProperty(FILE_PATH + "sweetNet.properties");

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
}
