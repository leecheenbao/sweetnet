package com.sweetNet.until;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.sweetNet.dto.MemberDTO;
import com.sweetNet.model.Images;
import com.sweetNet.model.Member;
import com.sweetNet.service.ImagesService;
import com.sweetNet.service.MemberService;
import com.sweetNet.serviceImpl.MemberServiceImpl;

@Component
public class ScheduledTasks {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private MemberService memberService;
	@Autowired
	private ImagesService imagesService;

	@Scheduled(fixedRate = 60 * 1000 * 60) // fixedRate = 60000 表示當前方法開始執行 60000ms(1分鐘) 後，Spring scheduling會再次呼叫該方法
	public void checkImage() {

		List<MemberDTO> memberDTOs = memberService.findAll();

		logger.info("執行排程 >> deleteImgStart ");
		for (MemberDTO memberDTO : memberDTOs) {
			String memUuid = memberDTO.getMemUuid();
			Images images = imagesService.findByMemUuid(memUuid);

			if (images != null) {
				List<String> list = new ArrayList<String>();

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

	private void checkFile(String fileDir, List<String> list) {

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

//	@Scheduled(fixedRate = 60 * 1000 * 60 * 24) // fixedRate = 60000 表示當前方法開始執行 60000ms(1分鐘) 後，Spring scheduling會再次呼叫該方法
	public void checkAge() {

		List<MemberDTO> memberDTOs = memberService.findAll();
		try {
			logger.info("執行排程 >> updateAge ");
			for (MemberDTO memberDTO : memberDTOs) {
				String memUuid = memberDTO.getMemUuid();
				String birthdayStr = memberDTO.getMemBirthday();
				String todayStr = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				if (!birthdayStr.equals("")) {

					Date today = sdf.parse(todayStr);
					Date birthday = sdf.parse(birthdayStr);
					Long time = today.getTime() - birthday.getTime();
					Integer age = Math.round(time / 1000 / 60 / 60 / 24 / 365);
					System.out.println(age);
					memberDTO.setMemAge(age);
					Member member = new MemberServiceImpl().getMemberFromMemberDTO(memberDTO);
					memberService.save(member);
				}

			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		logger.info("執行排程 >> updateAge ");
	}

	public static void main(String[] args) {
		List<String> list = new ArrayList<String>();
		list.add("1657167855433.png");
		list.add("1657167872874.png");
		list.add("1657167872894.png");
		list.add("1657167872923.png");
		list.add("1657167872962.png");
		ScheduledTasks s = new ScheduledTasks();
		s.checkFile(ConfigInfo.IMAGE_PATH + "/" + "e8bd588c-8bc0-4b06-bf14-db8049c821de", list);
	}
}