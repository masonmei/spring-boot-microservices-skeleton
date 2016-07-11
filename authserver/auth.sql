/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 50625
 Source Host           : localhost
 Source Database       : auth

 Target Server Type    : MySQL
 Target Server Version : 50625
 File Encoding         : utf-8

 Date: 07/11/2016 20:34:02 PM
*/

SET NAMES utf8;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
--  Table structure for `audit_evt_data`
-- ----------------------------
DROP TABLE IF EXISTS `audit_evt_data`;
CREATE TABLE `audit_evt_data` (
  `event_id` bigint(20) NOT NULL,
  `value` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `name` varchar(255) COLLATE utf8_bin NOT NULL,
  PRIMARY KEY (`event_id`,`name`),
  CONSTRAINT `FK_2ecyyy1x42shota22p271whl` FOREIGN KEY (`event_id`) REFERENCES `persistent_audit_event` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
--  Table structure for `authority`
-- ----------------------------
DROP TABLE IF EXISTS `authority`;
CREATE TABLE `authority` (
  `id` varchar(255) COLLATE utf8_bin NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
--  Records of `authority`
-- ----------------------------
BEGIN;
INSERT INTO `authority` VALUES ('ROLE_ADMIN'), ('ROLE_DEVELOPER'), ('ROLE_EDITOR'), ('ROLE_USER');
COMMIT;

-- ----------------------------
--  Table structure for `oauth_access_token`
-- ----------------------------
DROP TABLE IF EXISTS `oauth_access_token`;
CREATE TABLE `oauth_access_token` (
  `token_id` varchar(256) COLLATE utf8_bin DEFAULT NULL,
  `token` blob,
  `authentication_id` varchar(256) COLLATE utf8_bin DEFAULT NULL,
  `user_name` varchar(256) COLLATE utf8_bin DEFAULT NULL,
  `client_id` varchar(256) COLLATE utf8_bin DEFAULT NULL,
  `authentication` blob,
  `refresh_token` varchar(256) COLLATE utf8_bin DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
--  Table structure for `oauth_client_details`
-- ----------------------------
DROP TABLE IF EXISTS `oauth_client_details`;
CREATE TABLE `oauth_client_details` (
  `client_id` varchar(128) COLLATE utf8_bin NOT NULL,
  `resource_ids` varchar(256) COLLATE utf8_bin DEFAULT NULL,
  `client_secret` varchar(256) COLLATE utf8_bin DEFAULT NULL,
  `scope` varchar(256) COLLATE utf8_bin DEFAULT NULL,
  `authorized_grant_types` varchar(256) COLLATE utf8_bin DEFAULT NULL,
  `web_server_redirect_uri` varchar(256) COLLATE utf8_bin DEFAULT NULL,
  `authorities` varchar(256) COLLATE utf8_bin DEFAULT NULL,
  `access_token_validity` int(11) DEFAULT NULL,
  `refresh_token_validity` int(11) DEFAULT NULL,
  `additional_information` varchar(4000) COLLATE utf8_bin DEFAULT NULL,
  `autoapprove` varchar(256) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`client_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
--  Records of `oauth_client_details`
-- ----------------------------
BEGIN;
INSERT INTO `oauth_client_details` VALUES ('client', 'UAA,COMMENT,BLOG,GATEWAY', '$2a$10$gqVHvGp0N/SLZRyfUkM.eemmXvQDO252Y2rNJKM/XIDEwIUsS7pOq', 'UAA_READ,UAA_WRITE,COMMENT_READ,COMMENT_WRITE,BLOG_READ,BLOG_WRITE,GATEWAY_READ,GATEWAY_WRITE', 'authorization_code,password,client_credentials,refresh_token', null, 'ROLE_CLIENT, UAA_READ', '60', '2592000', '{\"anyProperty\":\"anyValue\"}', '');
COMMIT;

-- ----------------------------
--  Table structure for `oauth_client_token`
-- ----------------------------
DROP TABLE IF EXISTS `oauth_client_token`;
CREATE TABLE `oauth_client_token` (
  `token_id` varchar(256) COLLATE utf8_bin DEFAULT NULL,
  `token` blob,
  `authentication_id` varchar(128) COLLATE utf8_bin NOT NULL,
  `user_name` varchar(256) COLLATE utf8_bin DEFAULT NULL,
  `client_id` varchar(128) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`authentication_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
--  Table structure for `oauth_code`
-- ----------------------------
DROP TABLE IF EXISTS `oauth_code`;
CREATE TABLE `oauth_code` (
  `code` varchar(256) COLLATE utf8_bin DEFAULT NULL,
  `authentication` blob
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
--  Table structure for `oauth_refresh_token`
-- ----------------------------
DROP TABLE IF EXISTS `oauth_refresh_token`;
CREATE TABLE `oauth_refresh_token` (
  `token_id` varchar(256) COLLATE utf8_bin DEFAULT NULL,
  `token` blob,
  `authentication` blob
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
--  Table structure for `persistent_audit_event`
-- ----------------------------
DROP TABLE IF EXISTS `persistent_audit_event`;
CREATE TABLE `persistent_audit_event` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `event_date` datetime DEFAULT NULL,
  `event_type` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `principal` varchar(255) COLLATE utf8_bin NOT NULL,
  PRIMARY KEY (`id`),
  KEY `principal_idx` (`principal`),
  KEY `principal_audit_date_idx` (`principal`,`event_date`),
  KEY `audit_date_idx` (`event_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
--  Table structure for `user`
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_by` varchar(50) COLLATE utf8_bin NOT NULL,
  `created_date` datetime NOT NULL,
  `last_modified_by` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `last_modified_date` datetime DEFAULT NULL,
  `activated` bit(1) NOT NULL,
  `activation_key` varchar(20) COLLATE utf8_bin DEFAULT NULL,
  `email` varchar(100) COLLATE utf8_bin DEFAULT NULL,
  `first_name` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `lang_key` varchar(5) COLLATE utf8_bin DEFAULT NULL,
  `last_name` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `login` varchar(50) COLLATE utf8_bin NOT NULL,
  `password_hash` varchar(60) COLLATE utf8_bin NOT NULL,
  `reset_date` datetime DEFAULT NULL,
  `reset_key` varchar(20) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_ew1hvam8uwaknuaellwhqchhb` (`login`),
  UNIQUE KEY `UK_ob8kqyqqgmefl0aco34akdtpe` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
--  Records of `user`
-- ----------------------------
BEGIN;
INSERT INTO `user` VALUES ('1', 'mason', '2016-06-06 00:00:00', 'mason', '2016-06-06 00:00:00', b'1', '58810252782992301219', 'mason@igitras.com', '东旭', 'zh-cn', '梅', 'mason', '$2a$10$FTLXXEJzLuQoybxJaqDXPOHnkLZC4xCHh7v5tiDuSvTHP.al94cIq', null, null);
COMMIT;

-- ----------------------------
--  Table structure for `user_authority`
-- ----------------------------
DROP TABLE IF EXISTS `user_authority`;
CREATE TABLE `user_authority` (
  `user_id` bigint(20) NOT NULL,
  `authority_name` varchar(255) COLLATE utf8_bin NOT NULL,
  PRIMARY KEY (`user_id`,`authority_name`),
  KEY `FK_tnnyxjpcvg2aj0d0i6ufnabm2` (`authority_name`),
  CONSTRAINT `FK_5losscgu02yaej7prap7o6g5s` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
  CONSTRAINT `FK_tnnyxjpcvg2aj0d0i6ufnabm2` FOREIGN KEY (`authority_name`) REFERENCES `authority` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

SET FOREIGN_KEY_CHECKS = 1;
