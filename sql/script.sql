CREATE DATABASE IF NOT EXISTS `spiragym`;
USE `spiragym`;

DROP TABLE IF EXISTS `profile`;
CREATE TABLE `profile` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `desc` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_PERFIL_DESCRICAO` (`desc`)
);

DROP TABLE IF EXISTS `users`;
CREATE TABLE `users` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `active` tinyint NOT NULL,
  `email` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_USUARIO_EMAIL` (`email`)
);

DROP TABLE IF EXISTS `user_has_profile`;
CREATE TABLE `user_has_profile` (
  `user_id` bigint NOT NULL,
  `profile_id` bigint NOT NULL,
  PRIMARY KEY (`user_id`,`profile_id`),
  KEY `FK_USUARIO_TEM_PERFIL_ID` (`profile_id`),
  KEY `FK_PERFIL_TEM_USUARIO_ID` (`user_id`),
  CONSTRAINT `FK_PERFIL_TEM_USUARIO_ID` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
  CONSTRAINT `FK_USUARIO_TEM_PERFIL_ID` FOREIGN KEY (`profile_id`) REFERENCES `profile` (`id`)
);

INSERT INTO `profile` VALUES (1,'ADMIN');
INSERT INTO `profile` VALUES (2,'BARBEIRO');
INSERT INTO `profile` VALUES (3,'CLIENTE');
INSERT INTO `users` VALUES(1, 1, 'harllem@gmail.com', '$2a$10$VyJ54HKenVfdaVr0tzuVwOxEq9pHdg9iwwmX.B3k7c3Eqb75QhbJW');
INSERT INTO `user_has_profile` VALUES(1,1);