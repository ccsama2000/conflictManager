/*
SQLyog Ultimate v12.09 (64 bit)
MySQL - 8.0.31 : Database - conflict
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`conflict` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;

USE `conflict`;

/*Table structure for table `file` */

DROP TABLE IF EXISTS `file`;

CREATE TABLE `file` (
  `fileName` varchar(200) NOT NULL,
  `path` varchar(200) DEFAULT NULL,
  `ours` varbinary(8000) DEFAULT NULL,
  `theirs` varbinary(8000) DEFAULT NULL,
  `base` varbinary(8000) DEFAULT NULL,
  `isSolve` int DEFAULT '1',
  PRIMARY KEY (`fileName`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Data for the table `file` */

insert  into `file`(`fileName`,`path`,`ours`,`theirs`,`base`,`isSolve`) values ('anotherFile.java','D:\\tempRepository\\anotherFile.java','package junit.runner;\n\n/**\n * This class defines the current version of JUnit\n */\npublic class Version {\n	private Version() {\n		// don\'t instantiate\n	}\n\n	public static String id() {\n		return \"4.7-SNAPSHOT-20090601-1258\";\n	}\n	\n	public static void main(String[] args) {\n		System.out.println(id());\n	}\n}','package junit.runner;\n\n/**\n * This class defines the current version of JUnit\n */\npublic class Version {\n	private Version() {\n		// don\'t instantiate\n	}\n\n	public static String id() {\n		return \"4.7-SNAPSHOT-20090529-1159\";\n	}\n	\n	public static void main(String[] args) {\n		System.out.println(id());\n	}\n}\n',NULL,1),('file1',NULL,NULL,NULL,NULL,1),('sharedFile.java','D:\\tempRepository\\sharedFile.java','content1','content2',NULL,1);

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
