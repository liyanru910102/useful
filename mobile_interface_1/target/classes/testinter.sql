/*
 Navicat MySQL Data Transfer

 Source Server         : lyrinterface
 Source Server Type    : MySQL
 Source Server Version : 50624
 Source Host           : localhost:3306
 Source Schema         : light

 Target Server Type    : MySQL
 Target Server Version : 50624
 File Encoding         : 65001

 Date: 14/10/2017 19:59:46
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for testinter
-- ----------------------------
DROP TABLE IF EXISTS `testinter`;
CREATE TABLE `testinter`  (
  `url` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `khbz` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of testinter
-- ----------------------------
INSERT INTO `testinter` VALUES ('url1', 'khbz1');
INSERT INTO `testinter` VALUES ('url2', 'khbz2');

SET FOREIGN_KEY_CHECKS = 1;
