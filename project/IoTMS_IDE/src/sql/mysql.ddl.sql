create user 'iotms'@'localhost' identified by 'iotms';

create database iotmsdb character set=utf8;

grant all privileges on iotmsdb.* to 'iotms'@'localhost';

-- DROP TABLE iotmsdb.eventhistory;

-- eventhistory
create table iotmsdb.eventhistory (
	eventdate DATETIME  primary key,
	eventcontent varchar(100)
) engine=InnoDB character set = utf8;

insert into iotmsdb.eventhistory value ('2015-06-07 09:05','user command:door open');
insert into iotmsdb.eventhistory value ('2015-06-08 19:20','user command:door close');
insert into iotmsdb.eventhistory value ('2015-06-09 11:07','user command:Alarm Set');


-- DROP TABLE iotmsdb.messagehistory;

-- eventhistory
create table iotmsdb.messagehistory (
	messagedate DATETIME  primary key,
	messagecontent varchar(100)
) engine=InnoDB character set = utf8;

insert into iotmsdb.messagehistory value ('2015-06-07 09:05','sent Alarm E-Mail to hanbell');
insert into iotmsdb.messagehistory value ('2015-06-08 19:20','sent Alarm Twitter DM to @hanbell');
insert into iotmsdb.messagehistory value ('2015-06-09 11:07','sent Confirm Twitter DM to @hanbell79');


-- DROP TABLE iotmsdb.setting;

-- setting
create table iotmsdb.setting (
	security_settime int,
	lightoff_settime int,
	logging_duration int
) engine=InnoDB character set = utf8;

insert into iotmsdb.setting value (10,5,21600);


-- DROP TABLE iotmsdb.user_info;
-- DROP TABLE iotmsdb.user_authority;

-- user
create table iotmsdb.user_info (
	user_id varchar(50) primary key,
	user_passwd varchar(50),
	user_name varchar(50),
	user_mail varchar(50),
	user_twitter varchar(50)
) engine=InnoDB character set = utf8;

CREATE TABLE iotmsdb.user_authority (
	user_id varchar(50) NOT NULL,
	autohrity VARCHAR(50) NOT NULL,
	UNIQUE INDEX user_id_autohrity (user_id, autohrity),
	CONSTRAINT FK_user_authority_user_info FOREIGN KEY (user_id) REFERENCES user_info (user_id) ON UPDATE CASCADE ON DELETE CASCADE
) engine=InnoDB character set = utf8;

insert into iotmsdb.user_info value ("admin","admin","admin","admin@itoms.com","@itoms");
insert into iotmsdb.user_info value ("hanbell","hanbell","hanbell","hanbell@gmail.com","@hanbell79");

insert into iotmsdb.user_authority value ("admin","ADMIN");
insert into iotmsdb.user_authority value ("admin","USER");
insert into iotmsdb.user_authority value ("hanbell","USER");


-- DROP TABLE iotmsdb.thing_info;
-- DROP TABLE iotmsdb.node_info;

CREATE TABLE iotmsdb.node_info (
	`node_id` VARCHAR(50) NOT NULL,
	`node_name` VARCHAR(50) NOT NULL,
	`url` VARCHAR(50) NULL DEFAULT NULL,
	`port` VARCHAR(50) NULL DEFAULT NULL,
	`serialnumber` VARCHAR(50) NULL DEFAULT NULL,
	`json` VARCHAR(512) NULL DEFAULT NULL,
	PRIMARY KEY (`node_id`)
) engine=InnoDB character set = utf8;

CREATE TABLE iotmsdb.thing_info (
	`node_id` VARCHAR(50) NOT NULL,
	`thing_id` VARCHAR(50) NOT NULL,
	`thing_name` VARCHAR(50) NOT NULL,
	`type` VARCHAR(50) NULL DEFAULT NULL,
	`stype` VARCHAR(50) NULL DEFAULT NULL,
	`vtype` VARCHAR(50) NULL DEFAULT NULL,
	`vmin` VARCHAR(50) NULL DEFAULT NULL,
	`vmax` VARCHAR(50) NULL DEFAULT NULL,
	`value` VARCHAR(50) NULL DEFAULT NULL,
	`json` VARCHAR(512) NULL DEFAULT NULL,
	PRIMARY KEY (`node_id`, `thing_id`),
	CONSTRAINT `FK_thing_info_node_info` FOREIGN KEY (`node_id`) REFERENCES `node_info` (`node_id`) ON UPDATE CASCADE ON DELETE CASCADE
) engine=InnoDB character set = utf8;

insert into iotmsdb.node_info(node_id,node_name,serialnumber) value("00:11:22:33:44:55:66:77","node1","01234567");
insert into iotmsdb.node_info(node_id,node_name,serialnumber) value("01:12:23:34:45:56:67:78","node2","76543210");

insert into iotmsdb.thing_info(node_id,thing_id,thing_name,`type`,`stype`,`vtype`,`vmin`,`vmax`,`value`)
	value("00:11:22:33:44:55:66:77","1","Door","Door","ACTUATOR","STRING","Close","Open","Open");
insert into iotmsdb.thing_info(node_id,thing_id,thing_name,`type`,`stype`,`vtype`,`vmin`,`vmax`,`value`)
	value("00:11:22:33:44:55:66:77","2","Door","Door","ACTUATOR","STRING","Close","Open","Close");

	
insert into iotmsdb.thing_info(node_id,thing_id,thing_name,`type`,`stype`,`vtype`,`vmin`,`vmax`,`value`)
	value("01:12:23:34:45:56:67:78","1","MailBox","Mailbox","SENSOR","STRING","EMPTY","EXIST","EMPTY");



-- DROP TABLE iotmsdb.ruleset_info;

-- ruleset
create table iotmsdb.ruleset_info (
	`ruleset_id` INT(11) NOT NULL AUTO_INCREMENT,
	`ruleset` VARCHAR(512) NULL DEFAULT '0',
	PRIMARY KEY (`ruleset_id`)
) engine=InnoDB character set = utf8;

insert into iotmsdb.ruleset_info(ruleset) value ("if *@10==Set#Alarm 							then !0@1=Open#Door, 0@1=Close#Door, 0@7=On#AlarmLamp");
insert into iotmsdb.ruleset_info(ruleset) value ("if *@10==UnSet#Alarm 						then 0@7=Off#AlarmLamp");
insert into iotmsdb.ruleset_info(ruleset) value ("if 0@3==Away#Presense 						then 0@11=Confirm#Message, *@10=Set#AlarmDelay, 0@2=Off#LightDelay");
insert into iotmsdb.ruleset_info(ruleset) value ("if *@10==Set#Alarm, 0@3==AtHome#Presense 	then 0@11=Emergency#Message");
insert into iotmsdb.ruleset_info(ruleset) value ("if *@10==Set#Alarm, 0@1==Open#Door 			then 0@11=Emergency#Message");
insert into iotmsdb.ruleset_info(ruleset) value ("if 12:23:34:45:56:67@0010==Over100#Humidity						then 12:23:34:45:56:67@0011=Alarm#Message");
