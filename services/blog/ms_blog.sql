/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 50625
 Source Host           : localhost
 Source Database       : ms_blog

 Target Server Type    : MySQL
 Target Server Version : 50625
 File Encoding         : utf-8

 Date: 07/11/2016 20:59:06 PM
*/

SET NAMES utf8;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
--  Table structure for `comment`
-- ----------------------------
DROP TABLE IF EXISTS `comment`;
CREATE TABLE `comment` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_by` varchar(50) COLLATE utf8_bin NOT NULL,
  `created_date` datetime NOT NULL,
  `last_modified_by` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `last_modified_date` datetime DEFAULT NULL,
  `author` varchar(255) COLLATE utf8_bin NOT NULL,
  `author_email` varchar(255) COLLATE utf8_bin NOT NULL,
  `author_ip` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `author_url` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `content` varchar(512) COLLATE utf8_bin DEFAULT NULL,
  `post_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
--  Table structure for `post`
-- ----------------------------
DROP TABLE IF EXISTS `post`;
CREATE TABLE `post` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_by` varchar(50) COLLATE utf8_bin NOT NULL,
  `created_date` datetime NOT NULL,
  `last_modified_by` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `last_modified_date` datetime DEFAULT NULL,
  `author` varchar(255) COLLATE utf8_bin NOT NULL,
  `content` longtext COLLATE utf8_bin,
  `reads` int(11) DEFAULT NULL,
  `status` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `summary` longtext COLLATE utf8_bin,
  `title` varchar(255) COLLATE utf8_bin NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
--  Table structure for `post_tags`
-- ----------------------------
DROP TABLE IF EXISTS `post_tags`;
CREATE TABLE `post_tags` (
  `post_id` bigint(20) NOT NULL,
  `tag_id` bigint(20) NOT NULL,
  PRIMARY KEY (`post_id`,`tag_id`),
  KEY `FK_n8k2owli9ecanh4phj01mddvv` (`tag_id`),
  CONSTRAINT `FK_n8k2owli9ecanh4phj01mddvv` FOREIGN KEY (`tag_id`) REFERENCES `tag` (`id`),
  CONSTRAINT `FK_rf0kr7eqk5xoalmc4gigdwg3p` FOREIGN KEY (`post_id`) REFERENCES `post` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
--  Table structure for `tag`
-- ----------------------------
DROP TABLE IF EXISTS `tag`;
CREATE TABLE `tag` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `description` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `name` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_1wdpsed5kna2y38hnbgrnhi5b` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

SET FOREIGN_KEY_CHECKS = 1;
