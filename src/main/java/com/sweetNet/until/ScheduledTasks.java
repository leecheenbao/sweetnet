package com.sweetNet.until;

import java.text.SimpleDateFormat;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sweetNet.dto.MemberDTO;
import com.sweetNet.model.Images;
import com.sweetNet.service.ImagesService;
import com.sweetNet.service.MemberService;

@Component
public class ScheduledTasks {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

	@Autowired
	private MemberService memberService;
	@Autowired
	private ImagesService imagesService;

//	@Scheduled(fixedRate = 10000) // fixedRate = 60000 表示當前方法開始執行 60000ms(1分鐘) 後，Spring scheduling會再次呼叫該方法
	public void testFixedRate() {

		List<MemberDTO> memberDTOs = memberService.findAll();

		StringBuffer sb = new StringBuffer();
		int count = 1;
		for (MemberDTO memberDTO : memberDTOs) {
			String memUuid = memberDTO.getMemUuid();
			Images images = imagesService.findByMemUuid(memUuid);

			ImageUntil.deleteFile(memUuid);
			if (images != null) {

				sb.append("\n" + count++ + "====" + memUuid + "\n");
				if (images.getImage_1() != null) {
					logger.info(images.getImage_1());
				}
				if (images.getImage_2() != null) {

				}
				if (images.getImage_3() != null) {

				}
				if (images.getImage_4() != null) {

				}
				if (images.getImage_5() != null) {

				}
			}
		}
		logger.info("執行排程 >> 刪除圖片 ");
	}

	public static void main(String[] args) {

		String Url = "http://sugarbabytw.com:8083/sweetNetImg/images/9d2f276c-5f1f-4d92-ac33-43b20a1c8fc7/1656973683001.jpeg";

	}

}