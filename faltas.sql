-- MySQL dump 10.13  Distrib 5.7.17, for macos10.12 (x86_64)
--
-- Host: 79.157.119.161    Database: faltas
-- ------------------------------------------------------
-- Server version	5.5.5-10.1.23-MariaDB-9+deb9u1

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `alumno`
--

DROP TABLE IF EXISTS `alumno`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `alumno` (
  `id_alumno` int(11) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(45) NOT NULL,
  `apellidos` varchar(100) NOT NULL,
  `telefono` varchar(45) NOT NULL,
  `dni` varchar(9) NOT NULL,
  `correo` varchar(45) NOT NULL,
  `alta` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `baja` datetime DEFAULT NULL,
  PRIMARY KEY (`id_alumno`),
  UNIQUE KEY `dni_UNIQUE` (`dni`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `alumno`
--

LOCK TABLES `alumno` WRITE;
/*!40000 ALTER TABLE `alumno` DISABLE KEYS */;
INSERT INTO `alumno` VALUES (1,'Alumno1','Apellido1-1 Apellido1-2','123654789','12345678A','alumno1@campusaula.com','2018-04-26 17:30:26',NULL),(2,'Alumno2','Apellido2-1 Apellido2-2','234567890','23456789A','alumno1@campusaula.com','2018-04-26 17:31:23',NULL),(3,'Alumno3','Apellido3-1 Apellido3-2','345678901','34567890A','alumno3@campusaula.com','2018-04-26 17:34:11',NULL),(4,'Alumno4','Apellido4-1 Apellido4-2','456789012','45678901A','alumno4@campusaula.com','2018-04-26 17:35:25',NULL),(5,'Alumno5','Apellido5-1 Apellido5-2','4567890123','56789012A','alumno5@campusaula.com','2018-04-26 17:36:28',NULL),(6,'Isaac','Apellido6-1 Apellido6-2','567890123','67890123C','isaac@campusaula.com','2018-05-07 09:56:15','2018-05-06 22:00:00');
/*!40000 ALTER TABLE `alumno` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ciclo`
--

DROP TABLE IF EXISTS `ciclo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ciclo` (
  `id_ciclo` int(11) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(100) NOT NULL,
  `siglas` varchar(45) NOT NULL,
  `alta` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `baja` datetime DEFAULT NULL,
  PRIMARY KEY (`id_ciclo`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ciclo`
--

LOCK TABLES `ciclo` WRITE;
/*!40000 ALTER TABLE `ciclo` DISABLE KEYS */;
INSERT INTO `ciclo` VALUES (1,'Desarrollo de aplicaciones web','DAW','2018-04-26 16:19:37',NULL),(2,'Desarrollo de Aplicaciones Multiplataforma','DAM','2018-04-26 16:20:22',NULL),(3,'Administrador de Sistemas Informáticos y Redes','ASIR','2018-04-26 16:20:56',NULL),(4,'Sistemas Microinformáticos y Redes','SMR','2018-04-26 16:21:37',NULL);
/*!40000 ALTER TABLE `ciclo` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `curso`
--

DROP TABLE IF EXISTS `curso`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `curso` (
  `id_curso` int(11) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(45) NOT NULL,
  `alta` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `baja` datetime DEFAULT NULL,
  `id_ciclo` int(11) NOT NULL,
  PRIMARY KEY (`id_curso`),
  KEY `fk_curso_ciclo1_idx` (`id_ciclo`),
  CONSTRAINT `fk_curso_ciclo1` FOREIGN KEY (`id_ciclo`) REFERENCES `ciclo` (`id_ciclo`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `curso`
--

LOCK TABLES `curso` WRITE;
/*!40000 ALTER TABLE `curso` DISABLE KEYS */;
INSERT INTO `curso` VALUES (1,'Primero DAW','2018-04-26 16:25:23',NULL,1),(2,'Segundo DAW','2018-04-26 16:25:37',NULL,1),(3,'Primero DAM','2018-04-26 16:25:57',NULL,2),(4,'Segundo DAM','2018-04-26 16:26:07',NULL,2),(5,'Primero ASIR','2018-04-26 16:26:37',NULL,3),(6,'Segundo ASIR','2018-04-26 16:26:46',NULL,3),(7,'Primero SMR','2018-04-26 16:27:04',NULL,4),(8,'Segundo SMR','2018-04-26 16:27:25',NULL,4),(9,'Tercero (Modificado y borrado)','2018-04-26 16:32:49','2018-04-25 22:00:00',4);
/*!40000 ALTER TABLE `curso` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `falta`
--

DROP TABLE IF EXISTS `falta`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `falta` (
  `id_falta` int(11) NOT NULL AUTO_INCREMENT,
  `fecha` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `id_horario` int(11) NOT NULL,
  `id_modulo` int(11) NOT NULL,
  `id_alumno` int(11) NOT NULL,
  `alta` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `baja` datetime DEFAULT NULL,
  PRIMARY KEY (`id_falta`),
  KEY `fk_horario_has_modulo_modulo1_idx` (`id_modulo`),
  KEY `fk_horario_has_modulo_horario1_idx` (`id_horario`),
  KEY `fk_falta_alumno1_idx` (`id_alumno`),
  CONSTRAINT `fk_falta_alumno1` FOREIGN KEY (`id_alumno`) REFERENCES `alumno` (`id_alumno`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_horario_has_modulo_horario1` FOREIGN KEY (`id_horario`) REFERENCES `horario` (`id_horario`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_horario_has_modulo_modulo1` FOREIGN KEY (`id_modulo`) REFERENCES `modulo` (`id_modulo`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=76 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `falta`
--

LOCK TABLES `falta` WRITE;
/*!40000 ALTER TABLE `falta` DISABLE KEYS */;
INSERT INTO `falta` VALUES (1,'2018-05-17 20:29:42',1,10,5,'2018-05-17 20:29:42',NULL),(2,'2018-05-17 20:30:29',1,14,5,'2018-05-17 20:30:29',NULL),(3,'2018-05-17 20:30:52',1,13,5,'2018-05-17 20:30:52',NULL),(4,'2018-05-17 20:31:00',1,13,5,'2018-05-17 20:31:00',NULL),(5,'2018-05-17 20:31:31',1,4,1,'2018-05-17 20:31:31',NULL),(6,'2018-05-17 20:31:47',1,5,3,'2018-05-17 20:31:47',NULL),(7,'2018-05-17 20:56:05',1,4,1,'2018-05-17 20:56:05',NULL),(8,'2018-05-17 20:59:59',1,4,1,'2018-05-17 20:59:59',NULL),(9,'2018-05-17 21:00:29',1,4,1,'2018-05-17 21:00:29',NULL),(10,'2018-05-17 21:00:33',1,4,1,'2018-05-17 21:00:33',NULL),(11,'2018-05-17 21:00:36',1,4,1,'2018-05-17 21:00:36',NULL),(12,'2018-05-17 21:00:40',1,4,1,'2018-05-17 21:00:40',NULL),(13,'2018-05-17 21:00:46',1,4,1,'2018-05-17 21:00:46',NULL),(14,'2018-05-18 08:18:06',1,10,5,'2018-05-18 08:18:06',NULL),(15,'2018-05-18 08:18:46',1,14,5,'2018-05-18 08:18:46',NULL),(16,'2018-05-18 09:16:28',1,4,1,'2018-05-18 09:16:28',NULL),(17,'2018-05-18 14:14:48',1,4,1,'2018-05-18 14:14:48',NULL),(18,'2018-05-18 15:10:19',1,4,1,'2018-05-18 15:10:19',NULL),(19,'2018-05-19 13:10:11',1,4,1,'2018-05-19 13:10:11',NULL),(20,'2018-05-19 14:21:27',1,10,5,'2018-05-19 14:21:27',NULL),(21,'2018-05-19 14:26:06',1,4,1,'2018-05-19 14:26:06',NULL),(22,'2018-05-20 09:26:15',3,4,1,'2018-05-20 09:26:15',NULL),(23,'2018-05-20 09:27:04',2,4,1,'2018-05-20 09:27:04',NULL),(24,'2018-05-20 09:27:58',3,4,1,'2018-05-20 09:27:58',NULL),(25,'2018-05-20 09:28:00',3,4,1,'2018-05-20 09:28:00',NULL),(26,'2018-05-21 06:35:52',2,6,2,'2018-05-21 06:35:52',NULL),(27,'2018-05-21 06:35:55',2,6,2,'2018-05-21 06:35:55',NULL),(28,'2018-05-21 07:08:19',2,4,1,'2018-05-21 07:08:19',NULL),(29,'2018-05-21 08:13:28',3,4,1,'2018-05-21 08:13:28',NULL),(30,'2018-05-21 08:13:36',1,4,1,'2018-05-21 08:13:36',NULL),(31,'2018-05-21 08:18:14',3,4,1,'2018-05-21 08:18:14',NULL),(32,'2018-05-21 08:25:08',3,10,5,'2018-05-21 08:25:08',NULL),(33,'2018-05-21 08:25:12',2,10,5,'2018-05-21 08:25:12',NULL),(34,'2018-05-21 08:37:28',2,4,1,'2018-05-21 08:37:28',NULL),(35,'2018-05-21 08:37:44',1,4,4,'2018-05-21 08:37:44',NULL),(36,'2018-05-21 08:37:49',2,4,4,'2018-05-21 08:37:49',NULL),(37,'2018-05-21 11:04:36',3,4,1,'2018-05-21 11:04:36',NULL),(38,'2018-05-21 12:08:08',6,4,1,'2018-05-21 12:08:08',NULL),(39,'2018-05-21 12:08:13',2,4,1,'2018-05-21 12:08:13',NULL),(40,'2018-05-21 14:27:36',6,4,2,'2018-05-21 14:27:36',NULL),(41,'2018-05-21 14:27:44',3,4,3,'2018-05-21 14:27:44',NULL),(42,'2018-05-21 14:27:52',4,4,4,'2018-05-21 14:27:52',NULL),(43,'2018-05-21 15:29:50',1,8,1,'2018-05-21 15:29:50',NULL),(44,'2018-05-21 15:32:04',3,8,2,'2018-05-21 15:32:04',NULL),(45,'2018-05-21 15:32:27',1,10,5,'2018-05-21 15:32:27',NULL),(46,'2018-05-21 15:32:30',1,10,5,'2018-05-21 15:32:30',NULL),(47,'2018-05-21 15:34:36',4,6,4,'2018-05-21 15:34:36',NULL),(48,'2018-05-21 16:13:38',1,5,2,'2018-05-21 16:13:38',NULL),(49,'2018-05-21 16:24:24',1,4,1,'2018-05-21 16:24:24',NULL),(50,'2018-05-21 17:21:42',6,4,1,'2018-05-21 17:21:42',NULL),(51,'2018-05-22 07:26:38',4,4,1,'2018-05-22 07:26:38',NULL),(52,'2018-05-22 09:27:51',4,4,1,'2018-05-22 09:27:51',NULL),(53,'2018-05-22 09:27:58',2,4,1,'2018-05-22 09:27:58',NULL),(54,'2018-05-22 11:08:50',1,8,4,'2018-05-22 11:08:50',NULL),(55,'2018-05-22 11:10:09',4,8,2,'2018-05-22 11:10:09',NULL),(56,'2018-05-22 11:10:13',6,8,3,'2018-05-22 11:10:13',NULL),(57,'2018-05-22 11:10:32',2,5,1,'2018-05-22 11:10:32',NULL),(58,'2018-05-22 11:10:40',6,5,4,'2018-05-22 11:10:40',NULL),(59,'2018-05-22 11:10:46',1,6,1,'2018-05-22 11:10:46',NULL),(60,'2018-05-22 11:10:55',4,6,3,'2018-05-22 11:10:55',NULL),(61,'2018-05-22 11:11:06',1,7,1,'2018-05-22 11:11:06',NULL),(62,'2018-05-22 11:11:11',4,7,2,'2018-05-22 11:11:11',NULL),(63,'2018-05-22 11:11:15',5,7,3,'2018-05-22 11:11:15',NULL),(64,'2018-05-22 11:11:19',4,7,4,'2018-05-22 11:11:19',NULL),(65,'2018-05-22 11:28:47',3,7,1,'2018-05-22 11:28:47',NULL),(66,'2018-05-22 14:00:17',1,5,3,'2018-05-22 14:00:17',NULL),(67,'2018-05-22 14:20:49',6,4,1,'2018-05-22 14:20:49',NULL),(68,'2018-05-22 14:30:44',4,5,2,'2018-05-22 14:30:44',NULL),(69,'2018-05-22 14:30:57',1,5,4,'2018-05-22 14:30:57',NULL),(70,'2018-05-22 14:31:01',4,5,4,'2018-05-22 14:31:01',NULL),(71,'2018-05-22 14:31:02',4,5,4,'2018-05-22 14:31:02',NULL),(72,'2018-05-22 14:54:50',4,4,1,'2018-05-22 14:54:50',NULL),(73,'2018-05-22 17:12:45',2,4,1,'2018-05-22 17:12:45',NULL),(74,'2018-05-23 08:56:04',4,4,1,'2018-05-23 08:56:04',NULL),(75,'2018-05-24 14:33:45',1,6,3,'2018-05-24 14:33:45',NULL);
/*!40000 ALTER TABLE `falta` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `horario`
--

DROP TABLE IF EXISTS `horario`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `horario` (
  `id_horario` int(11) NOT NULL AUTO_INCREMENT,
  `inicio` varchar(5) NOT NULL,
  `fin` varchar(5) NOT NULL,
  `alta` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `baja` datetime DEFAULT NULL,
  PRIMARY KEY (`id_horario`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `horario`
--

LOCK TABLES `horario` WRITE;
/*!40000 ALTER TABLE `horario` DISABLE KEYS */;
INSERT INTO `horario` VALUES (1,'08:30','09:30','2018-05-07 10:24:27',NULL),(2,'09:30','10:20','2018-05-07 10:24:39',NULL),(3,'10:40','11:30','2018-05-07 10:24:53',NULL),(4,'11:30','12:30','2018-05-21 11:53:56',NULL),(5,'12:40','13:35','2018-05-21 12:03:15',NULL),(6,'13:35','14:30','2018-05-21 12:04:01',NULL);
/*!40000 ALTER TABLE `horario` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `matricula`
--

DROP TABLE IF EXISTS `matricula`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `matricula` (
  `id_matricula` int(11) NOT NULL AUTO_INCREMENT,
  `id_modulo` int(11) NOT NULL,
  `id_alumno` int(11) NOT NULL,
  `alta` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `baja` datetime DEFAULT NULL,
  PRIMARY KEY (`id_matricula`),
  KEY `fk_modulo_has_alumno_alumno1_idx` (`id_alumno`),
  KEY `fk_modulo_has_alumno_modulo1_idx` (`id_modulo`),
  CONSTRAINT `fk_modulo_has_alumno_alumno1` FOREIGN KEY (`id_alumno`) REFERENCES `alumno` (`id_alumno`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_modulo_has_alumno_modulo1` FOREIGN KEY (`id_modulo`) REFERENCES `modulo` (`id_modulo`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=28 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `matricula`
--

LOCK TABLES `matricula` WRITE;
/*!40000 ALTER TABLE `matricula` DISABLE KEYS */;
INSERT INTO `matricula` VALUES (2,4,1,'2018-05-14 19:35:54',NULL),(3,4,2,'2018-05-14 19:36:09',NULL),(4,4,3,'2018-05-14 19:36:17',NULL),(5,4,4,'2018-05-14 19:36:29',NULL),(6,5,1,'2018-05-15 08:48:54',NULL),(7,5,2,'2018-05-15 08:48:56',NULL),(8,5,3,'2018-05-15 08:49:07',NULL),(9,5,4,'2018-05-15 08:50:19',NULL),(10,5,4,'2018-05-15 08:50:21','2018-05-15 08:55:21'),(11,6,1,'2018-05-15 08:53:03',NULL),(12,6,2,'2018-05-15 08:53:08',NULL),(13,6,3,'2018-05-15 08:53:12',NULL),(14,6,4,'2018-05-15 08:53:15',NULL),(15,7,1,'2018-05-15 08:53:35',NULL),(16,7,2,'2018-05-15 08:53:38',NULL),(17,7,3,'2018-05-15 08:53:41',NULL),(18,7,4,'2018-05-15 08:53:44',NULL),(19,8,1,'2018-05-15 08:53:56',NULL),(20,8,2,'2018-05-15 08:53:59',NULL),(21,8,3,'2018-05-15 08:54:01',NULL),(22,8,4,'2018-05-15 08:54:03',NULL),(23,10,5,'2018-05-15 10:08:02',NULL),(24,11,5,'2018-05-15 10:08:41',NULL),(25,12,5,'2018-05-15 10:08:48',NULL),(26,13,5,'2018-05-15 10:08:58',NULL),(27,14,5,'2018-05-15 10:09:05',NULL);
/*!40000 ALTER TABLE `matricula` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `modulo`
--

DROP TABLE IF EXISTS `modulo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `modulo` (
  `id_modulo` int(11) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(45) NOT NULL,
  `siglas` varchar(10) NOT NULL,
  `alta` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `baja` datetime DEFAULT NULL,
  `id_usuario` int(11) NOT NULL,
  `id_curso` int(11) NOT NULL,
  PRIMARY KEY (`id_modulo`),
  KEY `fk_modulo_usuario1_idx` (`id_usuario`),
  KEY `fk_modulo_curso1_idx` (`id_curso`),
  CONSTRAINT `fk_modulo_curso1` FOREIGN KEY (`id_curso`) REFERENCES `curso` (`id_curso`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_modulo_usuario1` FOREIGN KEY (`id_usuario`) REFERENCES `usuario` (`id_usuario`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `modulo`
--

LOCK TABLES `modulo` WRITE;
/*!40000 ALTER TABLE `modulo` DISABLE KEYS */;
INSERT INTO `modulo` VALUES (4,'Entornos de Desarrollo','EDE','2018-04-26 16:45:29',NULL,10,1),(5,'Base de Datos','BD','2018-04-26 16:46:16',NULL,10,1),(6,'Lenguaje de marcas','LM','2018-04-26 16:50:06',NULL,10,1),(7,'Programación','PRO','2018-04-26 16:50:41',NULL,10,1),(8,'Sistemas Informáticos','SI','2018-04-26 16:51:28',NULL,10,1),(9,'Sistemas Informáticos2','SI3','2018-05-07 09:50:55','2018-05-07 09:50:55',10,3),(10,'Acceso a Dartos','AD','2018-05-08 12:06:33',NULL,12,2),(11,'Desarrollo de interfaces','DI','2018-05-08 12:07:07',NULL,12,2),(12,'Programación Multimedia y Móviles','PMM','2018-05-08 12:09:09',NULL,12,2),(13,'Programación de Servicios y Procesos','PSP','2018-05-08 12:10:59',NULL,12,2),(14,'Sistemas de Gestión Empresarial','SGE','2018-05-08 12:11:40',NULL,12,2);
/*!40000 ALTER TABLE `modulo` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `usuario`
--

DROP TABLE IF EXISTS `usuario`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `usuario` (
  `id_usuario` int(11) NOT NULL AUTO_INCREMENT,
  `usuario` varchar(45) DEFAULT NULL,
  `email` varchar(100) NOT NULL,
  `contrasena` varchar(45) NOT NULL,
  `admin` int(11) NOT NULL DEFAULT '0',
  `alta` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `baja` datetime DEFAULT NULL,
  PRIMARY KEY (`id_usuario`),
  UNIQUE KEY `email_UNIQUE` (`email`),
  UNIQUE KEY `usuario_UNIQUE` (`usuario`)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `usuario`
--

LOCK TABLES `usuario` WRITE;
/*!40000 ALTER TABLE `usuario` DISABLE KEYS */;
INSERT INTO `usuario` VALUES (10,'user1','user1@campusaula.com','A6xnQhbz4Vx2HuGl4lXwZ5U2I8iziLRFnhP5eNfIRvQ=',0,'2018-04-26 12:54:19',NULL),(11,'user2','user2@campusaula.com','A6xnQhbz4Vx2HuGl4lXwZ5U2I8iziLRFnhP5eNfIRvQ=',0,'2018-04-27 14:00:37',NULL),(12,'user3','user3@campusaula.com','A6xnQhbz4Vx2HuGl4lXwZ5U2I8iziLRFnhP5eNfIRvQ=',0,'2018-05-07 09:38:35',NULL),(13,'victorvace','victorvace95@gmail.com','A6xnQhbz4Vx2HuGl4lXwZ5U2I8iziLRFnhP5eNfIRvQ=',0,'2018-05-21 09:04:42',NULL),(14,'pepeviñuela','pepe@pepon.com','A6xnQhbz4Vx2HuGl4lXwZ5U2I8iziLRFnhP5eNfIRvQ=',0,'2018-05-21 10:25:18','2018-05-21 00:00:00');
/*!40000 ALTER TABLE `usuario` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2018-05-25  9:24:49
