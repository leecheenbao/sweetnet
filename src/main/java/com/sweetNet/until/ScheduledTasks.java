package com.sweetNet.until;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.sweetNet.dto.MemberDTO;
import com.sweetNet.model.Images;
import com.sweetNet.service.ImagesService;
import com.sweetNet.service.MemberService;

@Component
public class ScheduledTasks {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private MemberService memberService;
	@Autowired
	private ImagesService imagesService;

	@Scheduled(fixedRate = 60 * 1000 * 60) // fixedRate = 60000 表示當前方法開始執行 60000ms(1分鐘) 後，Spring scheduling會再次呼叫該方法
	public void testFixedRate() {

		List<MemberDTO> memberDTOs = memberService.findAll();

		logger.info("執行排程 >> deleteImgStart ");
		for (MemberDTO memberDTO : memberDTOs) {
			String memUuid = memberDTO.getMemUuid();
			Images images = imagesService.findByMemUuid(memUuid);

			if (images != null) {
				List list = new ArrayList();

				String path = ConfigInfo.REAL_PATH + "/sweetNetImg/images/" + memUuid;
				/* 檢查該會員的圖片DB欄位是否是空的 */
				list.add(path);
				if (images.getImage_1() != null) {
					list.add(images.getImage_1().substring(path.length() + 1));
				}
				if (images.getImage_2() != null) {
					list.add(images.getImage_2().substring(path.length() + 1));
				}
				if (images.getImage_3() != null) {
					list.add(images.getImage_3().substring(path.length() + 1));
				}
				if (images.getImage_4() != null) {
					list.add(images.getImage_4().substring(path.length() + 1));
				}
				if (images.getImage_5() != null) {
					list.add(images.getImage_5().substring(path.length() + 1));
				}
				checkFile(ConfigInfo.IMAGE_PATH + "/" + memUuid, list);
			}
		}
		logger.info("執行排程 >> deleteImgEnd ");
	}

	private void checkFile(String fileDir, List list) {

		List<File> fileList = new ArrayList<File>();
		File file = new File(fileDir);
		File[] files = file.listFiles();// 獲取目錄下的所有檔案或資料夾
		if (files == null) {// 如果目錄為空，直接退出
			return;
		}
		// 遍歷，目錄下的所有檔案
		for (File f : files) {
			if (f.isFile()) {
				fileList.add(f);
			}
		}
		boolean check = true;
		for (File f1 : fileList) {
			for (Object o : list) {
				if (f1.getName().equals(o.toString())) {
					check = false;
				}

			}
			logger.info("檔名：" + f1.getAbsolutePath() + "\t" + "是否被刪除：" + check);
			if (check) {
				File deleteFile = new File(f1.getAbsolutePath());
				if (deleteFile.exists()) {// 判斷路徑是否存在
					if (deleteFile.isFile()) {// boolean isFile():測試此抽象路徑名錶示的檔案是否是一個標準檔案。
						deleteFile.delete();
					}
					deleteFile.delete();
				}
			}
			check = true;
		}
	}

	public static void main(String[] args) {
		List list = new ArrayList();
		list.add("1657167855433.png");
		list.add("1657167872874.png");
		list.add("1657167872894.png");
		list.add("1657167872923.png");
		list.add("1657167872962.png");
		ScheduledTasks s = new ScheduledTasks();
		s.checkFile(ConfigInfo.IMAGE_PATH + "/" + "e8bd588c-8bc0-4b06-bf14-db8049c821de", list);
	}
}