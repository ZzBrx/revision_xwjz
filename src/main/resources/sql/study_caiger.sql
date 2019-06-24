/*
Navicat MySQL Data Transfer

Source Server         : localhost_3306
Source Server Version : 50717
Source Host           : localhost:3306
Source Database       : study_caiger

Target Server Type    : MYSQL
Target Server Version : 50717
File Encoding         : 65001

Date: 2019-01-31 16:05:20
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for sys_dict_data
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict_data`;
CREATE TABLE `sys_dict_data` (
  `id` varchar(64) NOT NULL,
  `parent_id` varchar(64) DEFAULT NULL,
  `parent_ids` varchar(500) DEFAULT NULL,
  `name` varchar(100) DEFAULT NULL,
  `label` varchar(100) DEFAULT NULL COMMENT '字典标签',
  `value` varchar(1000) DEFAULT NULL COMMENT '字典键值',
  `type` varchar(100) DEFAULT NULL,
  `is_sys` varchar(1) DEFAULT NULL,
  `status` varchar(1) DEFAULT NULL,
  `css_style` varchar(255) DEFAULT NULL COMMENT 'css样式',
  `css_class` varchar(255) DEFAULT NULL COMMENT 'css类名',
  `create_by` varchar(64) DEFAULT NULL,
  `create_date` datetime DEFAULT NULL,
  `update_by` varchar(64) DEFAULT NULL,
  `update_date` datetime DEFAULT NULL,
  `remarks` varchar(1000) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_dict_data
-- ----------------------------
INSERT INTO `sys_dict_data` VALUES ('1', '1', '1,', '是否', '是', '1', 'sys_yes_no', '0', '0', null, null, 'admin', '2019-01-30 16:56:25', 'admin', '2019-01-30 09:00:01', '是否');
INSERT INTO `sys_dict_data` VALUES ('2', '1', '1,', '是否', '否', '0', 'sys_yes_no', '0', '0', null, null, 'admin', '2019-01-30 16:56:27', 'admin', '2019-01-30 09:00:01', '是否');
INSERT INTO `sys_dict_data` VALUES ('3', '2', '2,', '状态', '正常', '0', 'sys_status', '1', '0', null, null, 'admin', '2019-01-30 16:56:30', null, null, '状态');
INSERT INTO `sys_dict_data` VALUES ('4', '2', '2,', '状态', '停用', '1', 'sys_status', '1', '0', null, null, 'admin', '2019-01-28 13:10:52', null, null, '状态');

-- ----------------------------
-- Table structure for sys_dict_type
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict_type`;
CREATE TABLE `sys_dict_type` (
  `id` varchar(64) NOT NULL,
  `name` varchar(100) DEFAULT NULL COMMENT '字典名称',
  `type` varchar(100) DEFAULT NULL,
  `is_sys` varchar(1) DEFAULT NULL,
  `status` varchar(1) DEFAULT NULL,
  `create_by` varchar(64) DEFAULT NULL,
  `create_date` datetime DEFAULT NULL,
  `update_by` varchar(64) DEFAULT NULL,
  `update_date` datetime DEFAULT NULL,
  `remarks` varchar(1000) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_dict_type
-- ----------------------------
INSERT INTO `sys_dict_type` VALUES ('1', '是否', 'sys_yes_no', '0', '0', null, null, 'admin', '2019-01-30 09:00:01', '是否');
INSERT INTO `sys_dict_type` VALUES ('2', '状态', 'sys_status', '1', '0', 'admin', '2019-01-30 16:58:05', 'admin', '2019-01-30 08:53:31', '状态');

-- ----------------------------
-- Table structure for sys_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu` (
  `id` varchar(64) NOT NULL COMMENT '菜单编号',
  `parent_id` varchar(64) DEFAULT NULL COMMENT '父级编号',
  `parent_ids` varchar(500) DEFAULT NULL COMMENT '所有父级编号',
  `name` varchar(100) DEFAULT NULL COMMENT '菜单名',
  `sort` decimal(10,0) DEFAULT NULL COMMENT '排序',
  `tree_leaf` char(1) DEFAULT NULL COMMENT '是否最末级 0：否；1：是',
  `tree_level` decimal(10,0) DEFAULT NULL COMMENT '层级级别',
  `type` char(1) DEFAULT NULL COMMENT '菜单类型（1：目录 ，2：菜单，3：权限）',
  `menu_href` varchar(500) DEFAULT NULL COMMENT '菜单连接',
  `menu_icon` varchar(100) DEFAULT NULL COMMENT '菜单图标',
  `permission` varchar(100) DEFAULT NULL COMMENT '权限标识',
  `status` char(1) DEFAULT NULL COMMENT '状态（0正常 1停用）',
  `is_sys` char(1) DEFAULT NULL COMMENT '0:非系统内置，1：系统内置',
  `module_id` varchar(10) DEFAULT NULL COMMENT '所属模块',
  `home_sys` char(1) DEFAULT NULL COMMENT '归属系统（0：pc，1：mobile）',
  `create_by` varchar(100) DEFAULT NULL,
  `create_date` datetime DEFAULT NULL,
  `update_by` varchar(100) DEFAULT NULL,
  `update_date` datetime DEFAULT NULL,
  `remarks` varchar(1000) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_menu
-- ----------------------------
INSERT INTO `sys_menu` VALUES ('0', '0', '0,', '菜单根目录', '0', '0', '0', '0', null, null, null, '1', '1', '1', null, 'admin', '2018-12-21 08:26:29', 'admin', '2018-12-21 08:26:33', null);
INSERT INTO `sys_menu` VALUES ('1', '0', '0,', '系统管理', '1', '0', '1', '1', '#', 'fa fa-cog', null, '0', '1', '1', null, 'admin', '2018-12-18 12:55:13', 'admin', '2018-12-18 12:55:21', '');
INSERT INTO `sys_menu` VALUES ('10', '2', '0,1,2,', '机构管理', '10', '0', '3', '2', '/sys/org/list', null, null, '0', '1', '1', null, 'admin', '2018-12-18 13:08:38', 'admin', '2018-12-18 13:08:42', null);
INSERT INTO `sys_menu` VALUES ('11', '10', '0,1,2,10,', '查看', '11', '1', '4', '3', null, null, 'sys:org:view', '0', '1', '1', null, 'admin', '2018-12-18 13:09:36', 'admin', '2018-12-18 13:09:41', null);
INSERT INTO `sys_menu` VALUES ('12', '10', '0,1,2,10,', '编辑', '12', '1', '4', '3', null, null, 'sys:org:edit', '0', '1', '1', null, 'admin', '2018-12-18 13:11:07', 'admin', '2018-12-18 13:11:12', null);
INSERT INTO `sys_menu` VALUES ('13', '2', '0,1,2,', '岗位管理', '13', '0', '3', '2', '/sys/post/list', null, '', '0', '1', '1', null, 'admin', '2018-12-18 13:13:47', 'admin', '2018-12-18 13:13:54', null);
INSERT INTO `sys_menu` VALUES ('14', '13', '0,1,2,13,', '查看', '14', '1', '4', '3', null, null, 'sys:post:delete', '0', '1', '1', null, 'admin', '2018-12-18 13:14:42', 'admin', '2018-12-18 13:14:47', null);
INSERT INTO `sys_menu` VALUES ('15', '13', '0,1,2,13,', '编辑', '15', '1', '4', '3', null, null, 'sys:post:edit', '0', '1', '1', null, 'admin', '2018-12-18 13:15:21', 'admin', '2018-12-18 13:15:25', null);
INSERT INTO `sys_menu` VALUES ('16', '1', '0,1,', '权限管理', '16', '0', '2', '1', '#', null, null, '0', '1', '1', null, 'admin', '2018-12-18 13:16:53', 'admin', '2018-12-18 13:16:58', null);
INSERT INTO `sys_menu` VALUES ('17', '16', '0,1,16,', '角色管理', '17', '0', '3', '2', '/sys/role/list', null, '', '0', '1', '1', null, 'admin', '2018-12-18 13:19:06', 'admin', '2018-12-18 13:19:10', null);
INSERT INTO `sys_menu` VALUES ('18', '17', '0,1,16,17,', '查看', '18', '1', '4', '3', null, null, 'sys:role:view', '0', '1', '1', null, 'admin', '2018-12-18 13:19:45', 'admin', '2018-12-18 13:19:51', null);
INSERT INTO `sys_menu` VALUES ('19', '17', '0,1,16,17,', '编辑', '19', '1', '4', '3', null, null, 'sys:role:edit', '0', '1', '1', null, 'admin', '2018-12-18 13:20:35', 'admin', '2018-12-18 13:20:40', null);
INSERT INTO `sys_menu` VALUES ('2', '1', '0,1,', '组织管理', '2', '0', '2', '1', '#', null, null, '0', '1', '1', null, 'admin', '2018-12-18 12:56:33', 'admin', '2018-12-18 12:56:38', null);
INSERT INTO `sys_menu` VALUES ('20', '17', '0,1,16,17,', '数据权限', '20', '1', '4', '3', null, null, 'sys:role:datascope', '0', '1', '1', null, 'admin', '2018-12-18 13:22:42', 'admin', '2018-12-18 13:22:47', null);
INSERT INTO `sys_menu` VALUES ('21', '17', '0,1,16,17,', '分配用户', '21', '1', '4', '3', null, null, 'sys:role:user', '0', '1', '1', null, 'admin', '2018-12-18 13:25:31', 'admin', '2018-12-18 13:25:38', null);
INSERT INTO `sys_menu` VALUES ('22', '17', '0,1,16,17,', '功能菜单', '22', '1', '4', '3', null, null, 'sys:role:menu', '0', '1', '1', null, 'admin', '2018-12-18 13:25:33', 'admin', '2018-12-18 13:25:40', null);
INSERT INTO `sys_menu` VALUES ('23', '17', '0,1,16,17,', '启用停用', '23', '1', '4', '3', null, null, 'sys:role:status', '0', '1', '1', null, 'admin', '2018-12-18 13:25:35', 'admin', '2018-12-18 13:25:43', null);
INSERT INTO `sys_menu` VALUES ('24', '1', '0,1,', '系统设置', '24', '0', '2', '1', '#', null, null, '0', '1', '1', null, 'admin', '2018-12-18 14:00:56', 'admin', '2018-12-18 14:01:04', null);
INSERT INTO `sys_menu` VALUES ('25', '24', '0,1,24,', '菜单管理', '25', '1', '3', '2', '/sys/menu/list', null, null, '0', '1', '1', null, 'admin', '2018-12-18 14:00:58', 'admin', '2018-12-18 14:01:07', null);
INSERT INTO `sys_menu` VALUES ('26', '24', '0,1,24,', '模块管理', '26', '1', '3', '2', '/sys/module/list', null, null, '0', '1', '1', null, 'admin', '2018-12-18 14:02:36', 'admin', '2018-12-18 14:02:41', null);
INSERT INTO `sys_menu` VALUES ('27', '24', '0,1,24,', '参数设置', '27', '1', '3', '2', '/sys/param/list', null, null, '0', '1', '1', null, 'admin', '2018-12-18 14:03:43', 'admin', '2018-12-18 14:03:49', null);
INSERT INTO `sys_menu` VALUES ('28', '24', '0,1,24,', '字典管理', '28', '1', '3', '2', '/sys/dict/type/list', null, null, '0', '1', '1', null, 'admin', '2018-12-18 14:04:41', 'admin', '2018-12-18 14:04:49', null);
INSERT INTO `sys_menu` VALUES ('29', '24', '0,1,24,', '行政区域', '29', '1', '3', '2', '/sys/area/list', null, null, '0', '1', '1', null, 'admin', '2018-12-18 14:05:33', 'admin', '2018-12-18 14:05:45', null);
INSERT INTO `sys_menu` VALUES ('3', '2', '0,1,2,', '用户管理', '3', '0', '3', '2', '/sys/user/index', null, null, '0', '1', '1', null, 'admin', '2018-12-18 12:58:06', 'admin', '2018-12-18 12:58:12', null);
INSERT INTO `sys_menu` VALUES ('30', '24', '0,1,24,', '产品许可', '30', '1', '3', '2', '/sys/licence/list', null, null, '0', '1', '1', null, 'admin', '2018-12-18 14:10:23', 'admin', '2018-12-18 14:10:28', null);
INSERT INTO `sys_menu` VALUES ('4', '3', '0,1,2,3,', '查看', '4', '1', '4', '3', null, null, 'sys:user:view', '0', '1', '1', null, 'admin', '2018-12-18 12:59:27', 'admin', '2018-12-18 12:59:32', null);
INSERT INTO `sys_menu` VALUES ('5', '3', '0,1,2,3,', '编辑', '5', '1', '4', '3', null, null, 'sys:user:edit', '0', '1', '1', null, 'admin', '2018-12-18 13:01:00', 'admin', '2018-12-18 13:01:07', null);
INSERT INTO `sys_menu` VALUES ('6', '3', '0,1,2,3,', '数据权限', '6', '1', '4', '3', null, null, 'sys:user:datascope', '0', '1', '1', null, 'admin', '2018-12-18 13:04:10', 'admin', '2018-12-18 13:04:15', null);
INSERT INTO `sys_menu` VALUES ('7', '3', '0,1,2,3,', '分配角色', '7', '1', '4', '3', null, null, 'sys:user:role', '0', '1', '1', null, 'admin', '2018-12-18 13:04:52', 'admin', '2018-12-18 13:04:57', null);
INSERT INTO `sys_menu` VALUES ('8', '3', '0,1,2,3,', '启用停用', '8', '1', '4', '3', null, null, 'sys:user:status', '0', '1', '1', null, 'admin', '2018-12-18 13:06:14', 'admin', '2018-12-18 13:06:19', null);
INSERT INTO `sys_menu` VALUES ('9', '3', '0,1,2,3,', '重置密码', '9', '1', '4', '3', null, null, 'sys:user:resetpwd', '0', '1', '1', null, 'admin', '2018-12-18 13:07:14', 'admin', '2018-12-18 13:07:19', null);

-- ----------------------------
-- Table structure for sys_module
-- ----------------------------
DROP TABLE IF EXISTS `sys_module`;
CREATE TABLE `sys_module` (
  `id` varchar(64) NOT NULL,
  `name` varchar(100) DEFAULT NULL COMMENT '模块名',
  `description` varchar(500) DEFAULT NULL COMMENT '描述',
  `version` varchar(100) DEFAULT NULL COMMENT '版本号',
  `upgrade_info` varchar(500) DEFAULT NULL COMMENT '升级信息',
  `is_sys` char(1) DEFAULT NULL COMMENT '系统内置',
  `status` char(1) DEFAULT NULL COMMENT '状态',
  `create_by` varchar(100) DEFAULT NULL,
  `create_date` datetime DEFAULT NULL,
  `update_by` varchar(100) DEFAULT NULL,
  `update_date` datetime DEFAULT NULL,
  `remarks` varchar(500) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_module
-- ----------------------------
INSERT INTO `sys_module` VALUES ('1', '核心模块', '角色、组织、模块、菜单、字典、参数', '1.0', null, '1', '1', 'admin', '2019-01-31 08:50:47', 'admin', '2019-01-31 08:50:51', null);

-- ----------------------------
-- Table structure for sys_org
-- ----------------------------
DROP TABLE IF EXISTS `sys_org`;
CREATE TABLE `sys_org` (
  `id` varchar(64) NOT NULL COMMENT '组织机构编号',
  `parent_id` varchar(64) DEFAULT NULL COMMENT '父级ID',
  `parent_ids` varchar(1000) DEFAULT NULL COMMENT '所有父级ID',
  `name` varchar(100) DEFAULT NULL COMMENT '机构名称',
  `code` varchar(255) DEFAULT NULL COMMENT '机构代码',
  `full_name` varchar(255) DEFAULT NULL,
  `sort` decimal(10,0) DEFAULT NULL COMMENT '排序',
  `tree_leaf` tinyint(1) DEFAULT NULL COMMENT '是否最末级 0：否；1：是',
  `tree_level` decimal(10,0) DEFAULT NULL COMMENT '层级级别',
  `type` char(1) DEFAULT NULL COMMENT '机构类别(0:根目录；1：集团公司：2：公司；3：部门)',
  `leader` varchar(100) DEFAULT NULL COMMENT '负责人',
  `phone` varchar(20) DEFAULT NULL COMMENT '电话',
  `address` varchar(255) DEFAULT NULL COMMENT '地址',
  `zip_code` varchar(20) DEFAULT NULL COMMENT '邮编',
  `email` varchar(255) DEFAULT NULL COMMENT '邮箱',
  `status` char(1) DEFAULT NULL COMMENT '状态（0:正常 1:停用）',
  `is_sys` char(1) DEFAULT NULL COMMENT '0:非系统内置，1：系统内置',
  `create_by` varchar(100) DEFAULT NULL,
  `create_date` datetime DEFAULT NULL,
  `update_by` varchar(100) DEFAULT NULL,
  `update_date` datetime DEFAULT NULL,
  `remarks` varchar(1000) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_org
-- ----------------------------
INSERT INTO `sys_org` VALUES ('0', '0', '0,', '组织机构根目录', '0000000000', null, '0', '0', '0', '0', '', null, null, null, null, null, null, null, null, null, null, null);
INSERT INTO `sys_org` VALUES ('1', '0', '0,', '中国移动集团公司', '0000000100', '中国移动集团公司', '1', '0', '1', '1', null, null, null, null, null, null, null, null, null, null, null, null);
INSERT INTO `sys_org` VALUES ('10', '9', '0,1,9,', '企业发展部', '0000000131', '中国移动集团江苏分公司企业发展部', '10', '1', '3', '3', null, null, null, null, null, null, null, null, null, null, null, null);
INSERT INTO `sys_org` VALUES ('11', '9', '0,1,9,', '工程建设部', '0000000132', '中国移动集团江苏分公司工程建设部', '11', '1', '3', '3', null, null, null, null, null, null, null, null, null, null, null, null);
INSERT INTO `sys_org` VALUES ('2', '1', '0,1,', '总公司', '0000000110', '中国移动集团总公司', '2', '0', '2', '2', null, null, null, null, null, null, null, null, null, null, null, null);
INSERT INTO `sys_org` VALUES ('3', '2', '0,1,2,', '综合部', '0000000111', '中国移动集团总公司综合部', '3', '1', '3', '3', null, null, null, null, null, null, null, null, null, null, null, null);
INSERT INTO `sys_org` VALUES ('4', '2', '0,1,2,', '企业合作部', '0000000112', '中国移动集团总公司企业合作部', '4', '1', '3', '3', null, null, null, null, null, null, null, null, null, null, null, null);
INSERT INTO `sys_org` VALUES ('5', '2', '0,1,2,', '数据部', '0000000113', '中国移动集团总公司企业数据部', '5', '1', '3', '3', null, null, null, null, null, null, null, null, null, null, null, null);
INSERT INTO `sys_org` VALUES ('6', '1', '0,1,', '广东公司', '0000000120', '中国移动集团广东分公司', '6', '0', '2', '2', null, null, null, null, null, null, null, null, null, null, null, null);
INSERT INTO `sys_org` VALUES ('7', '6', '0,1,6,', '后勤服务中心', '0000000121', '中国移动集团广东分公司后勤服务中心', '7', '1', '3', '3', null, null, null, null, null, null, null, null, null, null, null, null);
INSERT INTO `sys_org` VALUES ('8', '6', '0,1,6,', '市场经营部', '0000000122', '中国移动集团广东分公司市场经营部', '8', '1', '3', '3', null, null, null, null, null, null, null, null, null, null, null, null);
INSERT INTO `sys_org` VALUES ('9', '1', '0,1,', '江苏公司', '0000000130', '中国移动集团江苏分公司', '9', '0', '2', '2', null, null, null, null, null, null, null, null, null, null, null, null);

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role` (
  `id` varchar(64) NOT NULL COMMENT '角色编码',
  `name` varchar(100) NOT NULL DEFAULT '' COMMENT '角色名称',
  `code` varchar(100) DEFAULT NULL COMMENT '角色编号',
  `data_scope` char(1) DEFAULT NULL COMMENT '数据范围设置（1：所有 2：:公司 3：部门 4：个人 5:自定义）',
  `status` varchar(1) DEFAULT NULL COMMENT '状态（0正常 1停用）',
  `is_sys` char(1) DEFAULT NULL COMMENT '系统内置（1是 0否）',
  `create_by` varchar(100) NOT NULL COMMENT '创建者',
  `create_date` datetime NOT NULL COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新者',
  `update_date` datetime NOT NULL COMMENT '更新时间',
  `remarks` varchar(1000) DEFAULT NULL COMMENT '备注信息',
  `org_id` varchar(64) DEFAULT NULL COMMENT '所属组织',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO `sys_role` VALUES ('1', '超级管理员', null, null, null, null, 'admin', '2018-12-14 16:16:58', 'admin', '2018-12-14 16:17:02', null, '10');
INSERT INTO `sys_role` VALUES ('2', '管理员', null, null, null, null, 'admin', '2018-12-14 16:17:19', 'admin', '2018-12-14 16:17:24', null, '11');

-- ----------------------------
-- Table structure for sys_role_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_menu`;
CREATE TABLE `sys_role_menu` (
  `role_id` varchar(255) NOT NULL COMMENT '角色编号',
  `menu_id` varchar(255) NOT NULL COMMENT '菜单编号',
  PRIMARY KEY (`role_id`,`menu_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_role_menu
-- ----------------------------
INSERT INTO `sys_role_menu` VALUES ('1', '1');
INSERT INTO `sys_role_menu` VALUES ('1', '10');
INSERT INTO `sys_role_menu` VALUES ('1', '11');
INSERT INTO `sys_role_menu` VALUES ('1', '12');
INSERT INTO `sys_role_menu` VALUES ('1', '13');
INSERT INTO `sys_role_menu` VALUES ('1', '14');
INSERT INTO `sys_role_menu` VALUES ('1', '15');
INSERT INTO `sys_role_menu` VALUES ('1', '16');
INSERT INTO `sys_role_menu` VALUES ('1', '17');
INSERT INTO `sys_role_menu` VALUES ('1', '18');
INSERT INTO `sys_role_menu` VALUES ('1', '19');
INSERT INTO `sys_role_menu` VALUES ('1', '2');
INSERT INTO `sys_role_menu` VALUES ('1', '20');
INSERT INTO `sys_role_menu` VALUES ('1', '21');
INSERT INTO `sys_role_menu` VALUES ('1', '22');
INSERT INTO `sys_role_menu` VALUES ('1', '23');
INSERT INTO `sys_role_menu` VALUES ('1', '24');
INSERT INTO `sys_role_menu` VALUES ('1', '25');
INSERT INTO `sys_role_menu` VALUES ('1', '26');
INSERT INTO `sys_role_menu` VALUES ('1', '27');
INSERT INTO `sys_role_menu` VALUES ('1', '28');
INSERT INTO `sys_role_menu` VALUES ('1', '29');
INSERT INTO `sys_role_menu` VALUES ('1', '3');
INSERT INTO `sys_role_menu` VALUES ('1', '4');
INSERT INTO `sys_role_menu` VALUES ('1', '5');
INSERT INTO `sys_role_menu` VALUES ('1', '6');
INSERT INTO `sys_role_menu` VALUES ('1', '7');
INSERT INTO `sys_role_menu` VALUES ('1', '8');
INSERT INTO `sys_role_menu` VALUES ('1', '9');

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user` (
  `id` varchar(64) NOT NULL COMMENT '用户编码',
  `username` varchar(100) NOT NULL COMMENT '登录账号',
  `password` varchar(100) NOT NULL COMMENT '登录密码',
  `salt` varchar(100) NOT NULL,
  `name` varchar(100) NOT NULL COMMENT '用户姓名',
  `email` varchar(255) DEFAULT NULL COMMENT '电子邮箱',
  `mobile` varchar(20) DEFAULT NULL COMMENT '手机号码',
  `phone` varchar(20) DEFAULT NULL COMMENT '办公电话',
  `sex` char(1) DEFAULT NULL COMMENT '用户性别',
  `avatar` varchar(1000) DEFAULT NULL COMMENT '头像路径',
  `last_login_ip` varchar(100) DEFAULT NULL COMMENT '最后登陆IP',
  `last_login_date` datetime DEFAULT NULL COMMENT '最后登陆时间',
  `status` char(1) DEFAULT NULL COMMENT '状态（0:正常 1:停用）',
  `is_sys` char(1) DEFAULT NULL COMMENT '0:非系统内置，1：系统内置',
  `create_by` varchar(100) NOT NULL COMMENT '创建者',
  `create_date` datetime NOT NULL COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新者',
  `update_date` datetime NOT NULL COMMENT '更新时间',
  `remarks` varchar(1000) DEFAULT NULL COMMENT '备注信息',
  `org_id` varchar(64) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `sys_user_username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES ('1', 'admin', 'ea48576f30be1669971699c09ad05c94', '123456', '黄凯杰', null, null, null, null, null, null, null, '0', null, 'admin', '2018-12-14 15:57:03', 'admin', '2018-12-14 15:57:09', null, '10');

-- ----------------------------
-- Table structure for sys_user_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role` (
  `user_id` varchar(64) NOT NULL COMMENT '用户编号',
  `role_id` varchar(64) NOT NULL COMMENT '角色编号',
  PRIMARY KEY (`user_id`,`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_user_role
-- ----------------------------
INSERT INTO `sys_user_role` VALUES ('1', '1');
INSERT INTO `sys_user_role` VALUES ('1', '2');
