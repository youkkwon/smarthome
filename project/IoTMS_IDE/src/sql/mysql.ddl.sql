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
	`security_settime` INT(11) NULL DEFAULT NULL,
	`lightoff_settime` INT(11) NULL DEFAULT NULL,
	`logging_duration` INT(11) NULL DEFAULT NULL,
	`malfunc_settime` INT(11) NULL DEFAULT NULL,
	`doorsensor_settime` INT(11) NULL DEFAULT NULL
) engine=InnoDB character set = utf8;

insert into iotmsdb.setting value (10,5,21600,10,4);


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
	`node_name` VARCHAR(50) NULL DEFAULT NULL,
	`url` VARCHAR(50) NULL DEFAULT NULL,
	`port` VARCHAR(50) NULL DEFAULT NULL,
	`serialnumber` VARCHAR(50) NULL DEFAULT NULL,
	`json` VARCHAR(2048) NULL DEFAULT NULL,
	`registered` INT(4) NULL DEFAULT '0',
	PRIMARY KEY (`node_id`)
) engine=InnoDB character set = utf8;

CREATE TABLE iotmsdb.thing_info (
	`node_id` VARCHAR(50) NOT NULL,
	`thing_id` VARCHAR(50) NOT NULL,
	`thing_name` VARCHAR(50)  NULL DEFAULT NULL,
	`type` VARCHAR(50) NULL DEFAULT NULL,
	`stype` VARCHAR(50) NULL DEFAULT NULL,
	`vtype` VARCHAR(50) NULL DEFAULT NULL,
	`vmin` VARCHAR(50) NULL DEFAULT NULL,
	`vmax` VARCHAR(50) NULL DEFAULT NULL,
	`value` VARCHAR(50) NULL DEFAULT NULL,
	`json` VARCHAR(2048) NULL DEFAULT NULL,
	PRIMARY KEY (`node_id`, `thing_id`),
	CONSTRAINT `FK_thing_info_node_info` FOREIGN KEY (`node_id`) REFERENCES `node_info` (`node_id`) ON UPDATE CASCADE ON DELETE CASCADE
) engine=InnoDB character set = utf8;


insert into iotmsdb.node_info(node_id,registered) value("00:11:22:33:44:55",0);
insert into iotmsdb.node_info(node_id,registered) value("01:12:23:34:45:56",0);

insert into iotmsdb.thing_info(node_id,thing_id,`type`,`stype`,`vtype`,`vmin`,`vmax`)
	value("00:11:22:33:44:55","0001","Door","Actuator","String","Open","Close");
insert into iotmsdb.thing_info(node_id,thing_id,`type`,`stype`,`vtype`,`vmin`,`vmax`)
	value("00:11:22:33:44:55","0002","Light","Actuator","String","On","Off");
insert into iotmsdb.thing_info(node_id,thing_id,`type`,`stype`,`vtype`,`vmin`,`vmax`)
	value("00:11:22:33:44:55","0003","Presence","Sensor","String","AtHome","Away");
insert into iotmsdb.thing_info(node_id,thing_id,`type`,`stype`,`vtype`,`vmin`,`vmax`)
	value("00:11:22:33:44:55","0004","Temperature","Sensor","Number","-50","50");
insert into iotmsdb.thing_info(node_id,thing_id,`type`,`stype`,`vtype`,`vmin`,`vmax`)
	value("00:11:22:33:44:55","0005","Humidity","Sensor","Number","0","100");
insert into iotmsdb.thing_info(node_id,thing_id,`type`,`stype`,`vtype`,`vmin`,`vmax`)
	value("00:11:22:33:44:55","0006","DoorSensor","Sensor","String","Open","Close");
insert into iotmsdb.thing_info(node_id,thing_id,`type`,`stype`,`vtype`,`vmin`,`vmax`)
	value("00:11:22:33:44:55","0008","Alarm","Actuator","String","Set","Unset");


insert into iotmsdb.thing_info(node_id,thing_id,`type`,`stype`,`vtype`,`vmin`,`vmax`)
	value("01:12:23:34:45:56","0007","MailBox","Sensor","String","Empty","Mail");




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
