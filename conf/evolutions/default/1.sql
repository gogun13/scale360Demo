# Task schema

# --- !Ups
CREATE TABLE `tbl_task` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `subject` varchar(200) DEFAULT NULL,
  `detail` text,
  `status` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

# --- !Downs
drop table `tbl_task`