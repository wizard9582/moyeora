-- MySQL dump 10.13  Distrib 8.0.26, for Linux (x86_64)
--
-- Host: localhost    Database: ssafy_web_db
-- ------------------------------------------------------
-- Server version	8.0.26-0ubuntu0.20.04.2

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `conference`
--

DROP TABLE IF EXISTS `conference`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `conference` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `call_end_time` datetime(6) DEFAULT NULL,
  `call_start_time` datetime(6) DEFAULT NULL,
  `conference_category` varchar(255) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `is_active` bit(1) NOT NULL,
  `is_private` bit(1) NOT NULL,
  `title` varchar(255) DEFAULT NULL,
  `owner_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK38drjgkfcnv6a4m4ba3n41krm` (`owner_id`),
  CONSTRAINT `FK38drjgkfcnv6a4m4ba3n41krm` FOREIGN KEY (`owner_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `conference`
--

LOCK TABLES `conference` WRITE;
/*!40000 ALTER TABLE `conference` DISABLE KEYS */;
INSERT INTO `conference` VALUES (1,'2021-08-19 23:00:42.553000','2021-08-19 22:37:14.311000','mafia','환영합니다.',_binary '\0',_binary '\0','마피아게임1',6),(2,NULL,'2021-08-19 22:38:44.869000','video','환영합니다.',_binary '',_binary '\0','SSAFY',6),(3,NULL,'2021-08-19 22:38:54.248000','video','환영합니다.',_binary '',_binary '\0','밤샘 알고파티',8),(4,NULL,'2021-08-19 22:39:45.006000','mafia','환영합니다.',_binary '',_binary '\0','첫날투표국룰',8),(5,NULL,'2021-08-19 22:40:34.479000','mafia','환영합니다.',_binary '',_binary '','마피아게임2',8),(6,NULL,'2021-08-19 22:41:05.674000','video','같이공부해요~',_binary '',_binary '\0','CS스터디',8),(7,NULL,'2021-08-19 22:42:21.359000','mafia','환영합니다.',_binary '',_binary '','일단쏴',8),(8,NULL,'2021-08-19 22:42:53.397000','video','긴급버그!!!',_binary '',_binary '','3반6팀 긴급!!',8),(9,NULL,'2021-08-19 22:43:55.906000','mafia','마피아모여라',_binary '',_binary '','마피아24',8);
/*!40000 ALTER TABLE `conference` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `conference_category`
--

DROP TABLE IF EXISTS `conference_category`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `conference_category` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `conference_category`
--

LOCK TABLES `conference_category` WRITE;
/*!40000 ALTER TABLE `conference_category` DISABLE KEYS */;
/*!40000 ALTER TABLE `conference_category` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `conference_history`
--

DROP TABLE IF EXISTS `conference_history`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `conference_history` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `action` smallint NOT NULL,
  `inserted_time` datetime(6) DEFAULT NULL,
  `conference_id` bigint DEFAULT NULL,
  `user_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKda70ddbr5qsee4nr5e2h8yjhq` (`conference_id`),
  KEY `FKa7e25fsr5045birrnju69xkbs` (`user_id`),
  CONSTRAINT `FKa7e25fsr5045birrnju69xkbs` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
  CONSTRAINT `FKda70ddbr5qsee4nr5e2h8yjhq` FOREIGN KEY (`conference_id`) REFERENCES `conference` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `conference_history`
--

LOCK TABLES `conference_history` WRITE;
/*!40000 ALTER TABLE `conference_history` DISABLE KEYS */;
INSERT INTO `conference_history` VALUES (1,0,'2021-08-19 22:38:44.144000',1,2),(2,0,'2021-08-19 22:40:15.426000',2,4),(3,0,'2021-08-19 22:41:18.181000',4,6),(4,0,'2021-08-19 22:41:21.595000',4,4),(5,0,'2021-08-19 23:00:42.545000',1,6);
/*!40000 ALTER TABLE `conference_history` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `friend`
--

DROP TABLE IF EXISTS `friend`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `friend` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `from_user` varchar(255) DEFAULT NULL,
  `to_user` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `friend`
--

LOCK TABLES `friend` WRITE;
/*!40000 ALTER TABLE `friend` DISABLE KEYS */;
/*!40000 ALTER TABLE `friend` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `invite`
--

DROP TABLE IF EXISTS `invite`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `invite` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `day` int NOT NULL,
  `from_user` varchar(255) DEFAULT NULL,
  `hour` int NOT NULL,
  `min` int NOT NULL,
  `month` int NOT NULL,
  `to_user` varchar(255) DEFAULT NULL,
  `url` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `invite`
--

LOCK TABLES `invite` WRITE;
/*!40000 ALTER TABLE `invite` DISABLE KEYS */;
/*!40000 ALTER TABLE `invite` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `mafia`
--

DROP TABLE IF EXISTS `mafia`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `mafia` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `role` varchar(255) DEFAULT NULL,
  `status` int NOT NULL,
  `user_conference_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK1i8obydjsq0ht5qefhd9d4xdk` (`user_conference_id`),
  CONSTRAINT `FK1i8obydjsq0ht5qefhd9d4xdk` FOREIGN KEY (`user_conference_id`) REFERENCES `user_conference` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `mafia`
--

LOCK TABLES `mafia` WRITE;
/*!40000 ALTER TABLE `mafia` DISABLE KEYS */;
/*!40000 ALTER TABLE `mafia` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `match_history`
--

DROP TABLE IF EXISTS `match_history`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `match_history` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `total` int NOT NULL,
  `type` varchar(255) DEFAULT NULL,
  `win` int NOT NULL,
  `user_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK28246w0cxiyo9hyd010h5xkjm` (`user_id`),
  CONSTRAINT `FK28246w0cxiyo9hyd010h5xkjm` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=49 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `match_history`
--

LOCK TABLES `match_history` WRITE;
/*!40000 ALTER TABLE `match_history` DISABLE KEYS */;
INSERT INTO `match_history` VALUES (1,0,'mafia',0,1),(2,0,'doctor',0,1),(3,0,'police',0,1),(4,0,'citizen',0,1),(5,0,'mafia',0,2),(6,0,'doctor',0,2),(7,0,'police',0,2),(8,0,'citizen',0,2),(9,0,'mafia',0,3),(10,0,'doctor',0,3),(11,0,'police',0,3),(12,0,'citizen',0,3),(13,0,'mafia',0,4),(14,0,'doctor',0,4),(15,0,'police',0,4),(16,0,'citizen',0,4),(17,0,'mafia',0,5),(18,0,'doctor',0,5),(19,0,'police',0,5),(20,0,'citizen',0,5),(21,0,'mafia',0,6),(22,0,'doctor',0,6),(23,0,'police',0,6),(24,0,'citizen',0,6),(25,0,'mafia',0,7),(26,0,'doctor',0,7),(27,0,'police',0,7),(28,0,'citizen',0,7),(29,0,'mafia',0,8),(30,0,'doctor',0,8),(31,0,'police',0,8),(32,0,'citizen',0,8),(33,0,'mafia',0,9),(34,0,'doctor',0,9),(35,0,'police',0,9),(36,0,'citizen',0,9),(37,0,'mafia',0,10),(38,0,'doctor',0,10),(39,0,'police',0,10),(40,0,'citizen',0,10),(41,0,'mafia',0,11),(42,0,'doctor',0,11),(43,0,'police',0,11),(44,0,'citizen',0,11),(45,0,'mafia',0,12),(46,0,'doctor',0,12),(47,0,'police',0,12),(48,0,'citizen',0,12);
/*!40000 ALTER TABLE `match_history` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `post`
--

DROP TABLE IF EXISTS `post`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `post` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `date` varchar(255) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `notice` int DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  `user_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK72mt33dhhs48hf9gcqrq4fxte` (`user_id`),
  CONSTRAINT `FK72mt33dhhs48hf9gcqrq4fxte` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `post`
--

LOCK TABLES `post` WRITE;
/*!40000 ALTER TABLE `post` DISABLE KEYS */;
INSERT INTO `post` VALUES (1,'2021-08-19','게임 플레이 중 새로고침이나 새창 나가기를 누르시면 게임 진행에 오류가 발생함으로 유의 부탁드립니다!',1,'[필독] 마피아 게임시 유의사항.',2),(2,'2021-08-19','모여라 서비스는 화상회의 뿐만아니라 화상 채팅을 이용한 많은 엔터테이먼트를 제공합니다.',1,'[모여라 서비스 안내] ',2),(3,'2021-08-19','공지사항은 일반 유저에게 글 작성 및 수정 권한이 주어지지 않습니다.\n관리자 계정으로만 공지사항에 대한 권한이 부여됩니다.',1,'공지사항 규칙',2),(4,'2021-08-19','마피아인게 보이는데도 아니라고 발언하는게 너무 뻔뻔핵;',0,'이XX 플레이어 부정 프로그램 사용에 대해 고발합니다',2),(5,'2021-08-19','저번 게임 하실 때 얼굴에 김 묻히고 게임하시던데 다음 부터는 좀 때주시고 게임해주세요\n잘생김이요 엌ㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋ',0,'재상님 좀 보세요',9),(6,'2021-08-19','네... 잊지말고 기억해주세요....',0,'저를 기억해주세요....',10),(7,'2021-08-19','게임이 너무 쉽네요 초고수 모십니다\n저 이기시면 BBQ 황올 기프티콘 드림 ㄹㅇ',0,'마피아 게임 핵고수만',11),(8,'2021-08-19','부천 송내로 오시면 닭발에 소주 사드립니다~\n',0,'정모 하실분 구함',12);
/*!40000 ALTER TABLE `post` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `department` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `position` varchar(255) DEFAULT NULL,
  `user_id` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (1,'','mafia','$2a$10$uqvpLwTL8TCKhYTV3gk/euZ3yDegKZDNsck/QQpZA.Iv2gfInnLO.','','mafia'),(2,'','ssafy','$2a$10$.Lp1s4iTjODhtcGApfTYyO/mUpWpDOEOTN1dxWZqmuoye6zSIyhKC','','ssafy'),(3,'','김닥터','$2a$10$lBfuGSFz23VcSyc/3vsxcelS8Fx78khO0BxW.cnan.evUwCyoy.AK','','doctor'),(4,'','이재상','$2a$10$JFyLyEscYeVRspTd0NW0Uun8Pki4BbDW4F4SlVNR.RJA7n0Gf9on2','','이재상'),(5,'','김시민','$2a$10$jltkSgYXjI3B6MfiXhipV.xbUFq/AwjmkMCcYDbHSnmW/5nykmyPq','','citizen'),(6,'ssafy','상욱','$2a$10$fdkQ3KgFGftf1VfPenUU3.8gG5g4WIIdfvea5mL5S0O74bK7qu8Ku','교육생','상욱'),(7,'','최경찰','$2a$10$sOsp.4gFs8J3aLCUsdgqquAPb1oiWiDsh2qYMfZqvk62jrmCHIWQe','','police'),(8,'','codeking','$2a$10$0NCdBpOW/O2Ir/6wqXHFue.4Im7SeNR/K/jlunnNEmc7RGj/dCxUa','','codeking'),(9,'','탈주닌자','$2a$10$.0pfaXM0/WbDr7vvLDDDiOwrkRpPY4/RgRX769ihEsfGNxWXrf04m','','user01'),(10,'','최성훈','$2a$10$vXxi0qrGvRyxkq/MbL6D3OC3dTknnoxLO4wpsYabVIXq370JyQ6fi','','전팀장'),(11,'','박상욱','$2a$10$.PTO0gBUwHRsA3A1N9RaAe0Wu4os/j4BdTnJ4JJR.w2JJbuAn0ERi','','박상욱'),(12,'','노진혁','$2a$10$FowoJNbaokUgQEkgQ7GOiOiUBdMI.YQhyFk4GDT58/dBcaYuV4WFq','','노진혁');
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_conference`
--

DROP TABLE IF EXISTS `user_conference`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_conference` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `conference_id` bigint DEFAULT NULL,
  `user_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKixwgtx9i00w7rblemcwmtgt54` (`conference_id`),
  KEY `FK31od1ca8dy6piesngukay5udr` (`user_id`),
  CONSTRAINT `FK31od1ca8dy6piesngukay5udr` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
  CONSTRAINT `FKixwgtx9i00w7rblemcwmtgt54` FOREIGN KEY (`conference_id`) REFERENCES `conference` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_conference`
--

LOCK TABLES `user_conference` WRITE;
/*!40000 ALTER TABLE `user_conference` DISABLE KEYS */;
INSERT INTO `user_conference` VALUES (3,2,6),(4,3,8),(5,4,8),(7,5,8),(8,1,2),(10,6,8),(12,7,8),(13,8,8),(14,9,8);
/*!40000 ALTER TABLE `user_conference` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2021-08-19 23:04:21
