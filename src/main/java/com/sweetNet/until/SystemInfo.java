package com.sweetNet.until;

public class SystemInfo {
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

}
