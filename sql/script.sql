CREATE DATABASE IF NOT EXISTS `spirabarber`;
USE `spirabarber`;

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

DROP TABLE IF EXISTS `clientes`;
CREATE TABLE `clientes` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `dt_nascimento` date NOT NULL,
  `dt_cad` date NOT NULL,
  `name` varchar(255) NOT NULL,
  `user_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_CLIENTE_USUARIO_ID` (`user_id`),
  CONSTRAINT `FK_CLIENTE_USUARIO_ID` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
);

DROP TABLE IF EXISTS `barbeiros`;
CREATE TABLE `barbeiros` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `dt_nascimento` date NOT NULL,
  `dt_admissao` date NOT NULL,
  `dt_desligamento` date,
  `name` varchar(255) NOT NULL,
  `user_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_BARBEIRO_USUARIO_ID` (`user_id`),
  CONSTRAINT `FK_BARBEIRO_USUARIO_ID` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
);

INSERT INTO `profile` VALUES (1,'ADMIN'), (2,'BARBEIRO'), (3,'CLIENTE');
INSERT INTO `users` VALUES(1, 1, 'harllem@gmail.com', '$2a$10$VyJ54HKenVfdaVr0tzuVwOxEq9pHdg9iwwmX.B3k7c3Eqb75QhbJW');
INSERT INTO `user_has_profile` VALUES(1,1);