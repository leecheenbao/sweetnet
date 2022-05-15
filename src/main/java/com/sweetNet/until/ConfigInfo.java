package com.sweetNet.until;

import java.util.Properties;

public class ConfigInfo {

	// 讀檔路徑
	public static String FILE_PATH = ConfigInfo.class.getResource("/sweetNet.properties").getPath();;
	public static Properties p = PropUtil.getProperty(FILE_PATH);

	public static void cleanProperty() {
		try {
			p = null;
			p = PropUtil.getProperty("/oracle/config/sweetNet.properties");
		} catch (Exception e) {
			e.printStackTrace();

		}

	}

	// 網站域名
	public static String DOMAIN_NAME = p.getProperty("DOMAIN_NAME");

	// Excel-本機測試位置
	public static String FILE_OUTPUT_DEST = p.getProperty("MLI_EXCEL_FILE_OUTPUT_DEST");

	// 訊息代碼定義:
	public static String DATA_OK = p.getProperty("CODE_OK");// 成功 200
	public static String DATA_FAIL = p.getProperty("CODE_FAIL");// 失敗 403
	public static String DATA_NORESULT = p.getProperty("CODE_NORESULT");// 成功，查無資料 404
	public static String DATA_ERR_SYS = p.getProperty("CODE_ERR_SYS");// 系統內部發生錯誤 500

	// 服務回應訊息定義
	public static String MSG_DB_OK_INSERT = p.getProperty("MSG_DB_OK_INSERT");// MLI 服務成功
	public static String MSG_DB_FAIL_INSERT = p.getProperty("MSG_DB_FAIL_INSERT");// MLI 服務發生錯誤
	public static String MSG_DB_FAIL_COUNT = p.getProperty("MSG_DB_FAIL_COUNT");// 新增失敗，當日發問已達三次

	public static String AP_DOMAIN = p.getProperty("AP_DOMAIN");// 站台所在位置

	// AP1、2位置
	public static String AP_SERVER = p.getProperty("AP_SERVER");
	// TOKEN 驗證狀態
	public static String TOKEN_FAIL = p.getProperty("TOKEN_FAIL");
	public static String TOKEN_SUCCESS = p.getProperty("TOKEN_SUCCESS");

	// mail Server 設定
	public static String MAIL_FROM_ADDRESS = p.getProperty("MAIL_FROM_ADDRESS");
	public static String MAIL_FROM_NAME = p.getProperty("MAIL_FROM_NAME");
	public static String MAIL_SMTP_HOST_NAME = p.getProperty("MAIL_SMTP_HOST_NAME");
	public static String MAIL_SMTP_AUTH_USER = p.getProperty("MAIL_SMTP_AUTH_USER");
	public static String MAIL_SMTP_AUTH_PWD = p.getProperty("MAIL_SMTP_AUTH_PWD");
	public static String MAIL_TO_USER = p.getProperty("MAIL_TO_USER");

	// 文字訊息
	public static String SYS_MESSAGE_SUCCESS = p.getProperty("SYS_MESSAGE_SUCCESS");

	private static String getProperty(String key) {
		return p.getProperty(key);
	}

}
