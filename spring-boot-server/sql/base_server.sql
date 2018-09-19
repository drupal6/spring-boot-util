/*
Navicat MySQL Data Transfer

Source Server         : 110
Source Server Version : 50629
Source Host           : 192.168.2.110:3306
Source Database       : db_base_server

Target Server Type    : MYSQL
Target Server Version : 50629
File Encoding         : 65001

Date: 2018-09-19 11:37:51
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for t_role
-- ----------------------------
DROP TABLE IF EXISTS `t_role`;
CREATE TABLE `t_role` (
  `id` int(11) NOT NULL,
  `name` varchar(30) NOT NULL COMMENT '角色名称',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_role
-- ----------------------------
INSERT INTO `t_role` VALUES ('1', 'ROLE_ADMIN');
INSERT INTO `t_role` VALUES ('2', 'ROLE_USER');
INSERT INTO `t_role` VALUES ('3', 'ROLE_TOURIST');

-- ----------------------------
-- Table structure for t_user
-- ----------------------------
DROP TABLE IF EXISTS `t_user`;
CREATE TABLE `t_user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'uid',
  `user_name` varchar(50) NOT NULL COMMENT '账号',
  `password` varchar(100) NOT NULL COMMENT '密码',
  PRIMARY KEY (`id`),
  UNIQUE KEY `user_name` (`user_name`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_user
-- ----------------------------
INSERT INTO `t_user` VALUES ('2', 'ttt2', 'ttt1');
INSERT INTO `t_user` VALUES ('3', 'tttq', '$2a$10$JOOopM7b19vjkzAatcH4fet6mlSgr6scp.RGdcb2ClTpLJ6CBSgG6');
INSERT INTO `t_user` VALUES ('6', 'ttt1q', '$2a$10$CpOUlR9Uvlj9d3CPifYruedlDthwaDdq37QCYqYvl3sVZn/rp9XH2');

-- ----------------------------
-- Table structure for t_user_role
-- ----------------------------
DROP TABLE IF EXISTS `t_user_role`;
CREATE TABLE `t_user_role` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NOT NULL COMMENT '用户id',
  `role_id` int(11) NOT NULL COMMENT '角色id',
  PRIMARY KEY (`id`),
  KEY `userid` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_user_role
-- ----------------------------
INSERT INTO `t_user_role` VALUES ('1', '3', '2');
INSERT INTO `t_user_role` VALUES ('2', '6', '2');
