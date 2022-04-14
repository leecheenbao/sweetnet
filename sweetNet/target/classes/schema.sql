CREATE TABLE memberinfo (
	memId varchar(50) NOT null,
	memMail varchar(60) NOT NULL,
	memPwd	varchar(40)	NOT null,
	memName varchar(50) NOT NULL,
	memSex smallint NOT NULL,
	memNickname varchar(20) NOT null,
	memPhone varchar(10) NOT null,
	memBirthday date NOT NULL,
	memAge smallint NOT NULL,
	memAddress varchar(20) NOT NULL,
	memHeight smallint NOT NULL,
	memWeight smallint NOT NULL,
	memEdu smallint NOT NULL,
	memMarry smallint NOT NULL,
	memAlcohol smallint NOT NULL,
	memSmoke smallint NOT NULL,
	memIncome smallint NOT NULL,
	memAssets smallint NOT NULL,
	memIsvip smallint NOT NULL,
	memPic blob ,
	memRdate timestamp NOT NULL,
	memLgd smallint NOT NULL,
	memSta smallint NOT NULL
);


INSERT INTO memberinfo
(memId, memMail, memPwd,memName, memSex, memNickname, memPhone, memBirthday, memAge, memAddress, memHeight, memWeight, memEdu, memMarry, memAlcohol, memSmoke, memIncome, memAssets, memIsvip, memPic, memRdate, memLgd, memSta)
VALUES(UUID(), 'leecheenbao@gmail.com','123456789', '李慶寶', 1, '阿寶', '0919268790', '1991-10-23', 30, '基隆市南榮路', 174, 80, 2, 1, 2, 1, 2, 100, 2, 0, '2022-10-23 00:00:00', 1, 1);

INSERT INTO memberinfo
(memId, memMail, memPwd,memName, memSex, memNickname, memPhone, memBirthday, memAge, memAddress, memHeight, memWeight, memEdu, memMarry, memAlcohol, memSmoke, memIncome, memAssets, memIsvip, memPic, memRdate, memLgd, memSta)
VALUES(UUID(), 'leecheenbao@gmail.com','123456789', '李慶寶', 1, '阿寶', '0919268790', '1991-10-23', 30, '基隆市南榮路', 174, 80, 2, 1, 2, 1, 2, 100, 2, 0, '2022-10-23 00:00:00', 1, 1);


INSERT INTO memberinfo
(memId, memMail, memPwd,memName, memSex, memNickname, memPhone, memBirthday, memAge, memAddress, memHeight, memWeight, memEdu, memMarry, memAlcohol, memSmoke, memIncome, memAssets, memIsvip, memPic, memRdate, memLgd, memSta)
VALUES(UUID(), 'leecheenbao@gmail.com','123456789', '李慶寶', 1, '阿寶', '0919268790', '1991-10-23', 30, '基隆市南榮路', 174, 80, 2, 1, 2, 1, 2, 100, 2, 0, '2022-10-23 00:00:00', 1, 1);
