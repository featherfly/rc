drop database rc_persitence;
create database rc_persitence;
use rc_persitence;

SET FOREIGN_KEY_CHECKS=0;
DROP TABLE IF EXISTS `rc_configuration_difinition`;
CREATE TABLE `rc_configuration_difinition`  (
  `name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `descp` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`NAME`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

DROP TABLE IF EXISTS `rc_configuration_value`;
CREATE TABLE `rc_configuration_value`  (
  `config_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '',
  `name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '',
  `descp` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `value` varchar(300) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`config_name`, `name`) USING BTREE,
  CONSTRAINT `FK_REFERENCE_CONFIG_NAME` FOREIGN KEY (`config_name`) REFERENCES `rc_configuration_difinition` (`name`) ON DELETE
   RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

INSERT INTO `rc_configuration_difinition` VALUES ('UserConfig2', null);
INSERT INTO `rc_configuration_value` VALUES ('UserConfig2', 'age', 'age', '12');
INSERT INTO `rc_configuration_value` VALUES ('UserConfig2', 'name', 'name', 'yufei');