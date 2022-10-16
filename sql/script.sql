CREATE DATABASE IF NOT EXISTS `spirabarber`;
USE `spirabarber`;

DROP TABLE IF EXISTS `profile`;
CREATE TABLE `profile` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `desc` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_PERFIL_DESCRICAO` (`desc`)
);

DROP TABLE IF EXISTS `cargos`;
CREATE TABLE `cargos`(
    `id` bigint NOT NULL AUTO_INCREMENT,
    `desc` varchar(255) NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `UK_PERFIL_DESCRICAO` (`desc`)
);

DROP TABLE IF EXISTS `user_cargo`;
CREATE TABLE `user_cargo`(
    `id` bigint NOT NULL AUTO_INCREMENT,
    `cargo_id` bigint NOT NULL,
    `dt_admissao` date NOT NULL,
    `dt_demissao` date DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY `FK_CARGO_ID` (`cargo_id`),
    CONSTRAINT `FK_CARGO_ID` FOREIGN KEY (`cargo_id`) REFERENCES `cargos` (`id`)
);

DROP TABLE IF EXISTS `users`;
CREATE TABLE `users` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_cargo_id` bigint DEFAULT NULL,
  `active` tinyint NOT NULL,
  `email` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_USER_CARGO_ID` (`user_cargo_id`),
  CONSTRAINT `FK_USER_CARGO_ID` FOREIGN KEY (`user_cargo_id`) REFERENCES `user_cargo` (`id`),
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

DROP TABLE IF EXISTS `pessoas`;
CREATE TABLE `pessoas` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `dt_nascimento` date NOT NULL,
  `name` varchar(255) NOT NULL,
  `user_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_CLIENTE_USUARIO_ID` (`user_id`),
  CONSTRAINT `FK_CLIENTE_USUARIO_ID` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
);

DROP TABLE IF EXISTS `cargo_historico`;
CREATE TABLE `cargo_historico`(
    `id` bigint NOT NULL AUTO_INCREMENT,
    `user_id` bigint NOT NULL,
    `cargo_id` bigint NOT NULL,
    `dt_admissao` date NOT NULL,
    `dt_demissao` date DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY `FK_HISTORICO_USUARIO_ID` (`user_id`),
    KEY `FK_HISTORICO_CARGO_ID` (`cargo_id`),
    CONSTRAINT `FK_HISTORICO_USUARIO_ID` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
    CONSTRAINT `FK_HISTORICO_CARGO_ID` FOREIGN KEY (`cargo_id`) REFERENCES `cargos` (`id`)
);

DROP TABLE IF EXISTS `servicos`;
CREATE TABLE `servicos`(
    `id` bigint NOT NULL AUTO_INCREMENT,
    `nome` varchar(255) NOT NULL,
    `tempo_conclusao_min` int NOT NULL,
    `descricao` varchar(255) NOT NULL,
    `preco` decimal(19,2) NOT NULL,
    `image_name` varchar(255) NOT NULL,
    PRIMARY KEY (`id`)
);

DROP TABLE IF EXISTS `user_has_servico`;
CREATE TABLE `user_has_servico`(
    `user_id` bigint NOT NULL,
    `servico_id` bigint NOT NULL,
    PRIMARY KEY(`user_id`, `servico_id`),
    KEY `FK_USER_HAS_SERVICO_ID` (`servico_id`),
    KEY `FK_SERVICO_HAS_USER_ID` (`user_id`),
    CONSTRAINT `FK_USER_HAS_SERVICO_ID` FOREIGN KEY (`servico_id`) REFERENCES `servicos` (`id`),
    CONSTRAINT `FK_SERVICO_HAS_USER_ID` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
);

INSERT INTO `cargos` VALUES (1,'BARBEIRO'), (2,'AUXILIAR ADMINISTRATIVO');
INSERT INTO `profile` VALUES (1,'ADMIN'), (2,'BARBEIRO'), (3,'CLIENTE');
INSERT INTO `users` VALUES(1, null, 1, 'harllem@gmail.com', '$2a$10$VyJ54HKenVfdaVr0tzuVwOxEq9pHdg9iwwmX.B3k7c3Eqb75QhbJW');
INSERT INTO `user_has_profile` VALUES(1,1);