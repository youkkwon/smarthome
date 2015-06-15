 DROP TABLE iotmsdb.ruleset_info;

-- ruleset
create table iotmsdb.ruleset_info (
	`ruleset_id` INT(11) NOT NULL AUTO_INCREMENT,
	`ruleset` VARCHAR(512) NULL DEFAULT '0',
	PRIMARY KEY (`ruleset_id`)
) engine=InnoDB character set = utf8;

insert into iotmsdb.ruleset_info(ruleset) value ("if *@0010==Set#Alarm 															then !12:23:34:45:56:67@0001=Open#Door, 12:23:34:45:56:67@0001=Close#Door, 12:23:34:45:56:67@0007=On#AlarmLamp");
insert into iotmsdb.ruleset_info(ruleset) value ("if *@0010==UnSet#Alarm 														then 12:23:34:45:56:67@0007=Off#AlarmLamp");
insert into iotmsdb.ruleset_info(ruleset) value ("if 12:23:34:45:56:67@0003==Away#Presence 								then 12:23:34:45:56:67@0011=Confirm#Message, *@0010=Set#AlarmDelay, 12:23:34:45:56:67@0002=Off#LightDelay");
insert into iotmsdb.ruleset_info(ruleset) value ("if *@0010==Set#Alarm, 12:23:34:45:56:67@0003==AtHome#Presense 	then 12:23:34:45:56:67@0011=Emergency#Message");
insert into iotmsdb.ruleset_info(ruleset) value ("if *@0010==Set#Alarm, 12:23:34:45:56:67@0001==Open#Door 			then 12:23:34:45:56:67@0011=Emergency#Message");
insert into iotmsdb.ruleset_info(ruleset) value ("if 12:23:34:45:56:67@0010==Over100#Humidity							then 12:23:34:45:56:67@0011=Malfunction#Message");
insert into iotmsdb.ruleset_info(ruleset) value ("if 12:23:34:45:56:67@0010==Over100#Temperature						then 12:23:34:45:56:67@0011=Malfunction#Message");
insert into iotmsdb.ruleset_info(ruleset) value ("if 12:23:34:45:56:67@0010==Under0#Temperature							then 12:23:34:45:56:67@0011=Malfunction#Message");