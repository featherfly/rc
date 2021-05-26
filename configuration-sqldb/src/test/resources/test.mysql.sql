drop database rc_persitence;
create database rc_persitence;
use rc_persitence;

SET FOREIGN_KEY_CHECKS=0;

CREATE TABLE RC_CONFIGURATION_DIFINITION
(
   NAME                 VARCHAR(100) NOT NULL,
   DESCP                VARCHAR(50),
   PRIMARY KEY (NAME)
);
CREATE TABLE RC_CONFIGURATION_VALUE
(
   CONFIG_NAME          VARCHAR(100),
   NAME                 VARCHAR(50),
   DESCP                VARCHAR(50),
   VALUE                VARCHAR(300),
   PRIMARY KEY (CONFIG_NAME, NAME)
);
ALTER TABLE RC_CONFIGURATION_VALUE ADD CONSTRAINT FK_REFERENCE_CONFIG_NAME FOREIGN KEY (CONFIG_NAME)
      REFERENCES RC_CONFIGURATION_DIFINITION (NAME) ON DELETE RESTRICT ON UPDATE RESTRICT;

INSERT INTO `rc_configuration_difinition` VALUES ('UserConfig2', null);
INSERT INTO `rc_configuration_value` VALUES ('UserConfig2', 'age', 'age', '12');
INSERT INTO `rc_configuration_value` VALUES ('UserConfig2', 'name', 'name', 'yufei');