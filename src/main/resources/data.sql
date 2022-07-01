# ************************************************************
# Sequel Pro SQL dump
# Version 4541
#
# http://www.sequelpro.com/
# https://github.com/sequelpro/sequelpro
#
# Host: 127.0.0.1 (MySQL 5.7.37)
# Database: testing
# Generation Time: 2022-07-01 06:14:13 +0000
# ************************************************************


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


# Dump of table answer
# ------------------------------------------------------------

DROP TABLE IF EXISTS `answer`;

CREATE TABLE `answer` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `is_correct` bit(1) DEFAULT NULL,
  `text` varchar(255) DEFAULT NULL,
  `question_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK8frr4bcabmmeyyu60qt7iiblo` (`question_id`),
  CONSTRAINT `FK8frr4bcabmmeyyu60qt7iiblo` FOREIGN KEY (`question_id`) REFERENCES `question` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

LOCK TABLES `answer` WRITE;
/*!40000 ALTER TABLE `answer` DISABLE KEYS */;

INSERT INTO `answer` (`id`, `is_correct`, `text`, `question_id`)
VALUES
	(1,b'0','Answer 1',1),
	(2,b'1','Answer 2',1),
	(3,b'0','Answer 3',1),
	(4,b'1','Answer 4',2),
	(5,b'0','Answer 5',2),
	(6,b'0','Answer 6',3),
	(7,b'0','Answer 7',3),
	(8,b'1','Answer 8',3);

/*!40000 ALTER TABLE `answer` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table question
# ------------------------------------------------------------

DROP TABLE IF EXISTS `question`;

CREATE TABLE `question` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `text` varchar(255) DEFAULT NULL,
  `subject_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKkfvh71q42645g7p9cgxjygbgc` (`subject_id`),
  CONSTRAINT `FKkfvh71q42645g7p9cgxjygbgc` FOREIGN KEY (`subject_id`) REFERENCES `subject` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

LOCK TABLES `question` WRITE;
/*!40000 ALTER TABLE `question` DISABLE KEYS */;

INSERT INTO `question` (`id`, `text`, `subject_id`)
VALUES
	(1,'Question 1',1),
	(2,'Question 2',1),
	(3,'Question 3',1),
	(4,'Question 4',2),
	(5,'Question 5',2);

/*!40000 ALTER TABLE `question` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table subject
# ------------------------------------------------------------

DROP TABLE IF EXISTS `subject`;

CREATE TABLE `subject` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `title` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

LOCK TABLES `subject` WRITE;
/*!40000 ALTER TABLE `subject` DISABLE KEYS */;

INSERT INTO `subject` (`id`, `title`)
VALUES
	(1,'Subject 1'),
	(2,'Subject 2');

/*!40000 ALTER TABLE `subject` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table testing
# ------------------------------------------------------------

DROP TABLE IF EXISTS `testing`;

CREATE TABLE `testing` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) NOT NULL,
  `ended_at` datetime(6) DEFAULT NULL,
  `total_score` int(11) DEFAULT NULL,
  `subject_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKo9xw7sn44rxhikj150n6xfktv` (`subject_id`),
  KEY `FK78xnn1bqbf7gv8ack02mvh877` (`user_id`),
  CONSTRAINT `FK78xnn1bqbf7gv8ack02mvh877` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
  CONSTRAINT `FKo9xw7sn44rxhikj150n6xfktv` FOREIGN KEY (`subject_id`) REFERENCES `subject` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table testing_question
# ------------------------------------------------------------

DROP TABLE IF EXISTS `testing_question`;

CREATE TABLE `testing_question` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `answer_id` int(11) DEFAULT NULL,
  `question_id` int(11) NOT NULL,
  `testing_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKru750al2byeai2avakp2bfj1w` (`answer_id`),
  KEY `FK7iksja441f46jtyrdcsqvcpdq` (`question_id`),
  KEY `FKco561nqle5lx1s44ayrsxybo8` (`testing_id`),
  CONSTRAINT `FK7iksja441f46jtyrdcsqvcpdq` FOREIGN KEY (`question_id`) REFERENCES `question` (`id`),
  CONSTRAINT `FKco561nqle5lx1s44ayrsxybo8` FOREIGN KEY (`testing_id`) REFERENCES `testing` (`id`),
  CONSTRAINT `FKru750al2byeai2avakp2bfj1w` FOREIGN KEY (`answer_id`) REFERENCES `answer` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table user
# ------------------------------------------------------------

DROP TABLE IF EXISTS `user`;

CREATE TABLE `user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `email` varchar(255) DEFAULT NULL,
  `login` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;

INSERT INTO `user` (`id`, `email`, `login`, `password`)
VALUES
	(1,'test@test.com','test','$2a$10$RCWf2A/z4qfij3q6EUigI.jDYnqlKaX5.b8/LVnXRbfS8RgQ4v3oi');

/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;



/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
