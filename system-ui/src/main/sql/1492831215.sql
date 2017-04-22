CREATE DATABASE IF NOT EXISTS heystk;

USE heystk;

CREATE TABLE User (
  id INT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
  username VARCHAR(32) NOT NULL UNIQUE,
  salt VARCHAR(64) NOT NULL,
  hash VARCHAR(64) NOT NULL
) ENGINE=INNODB;