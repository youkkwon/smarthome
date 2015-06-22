 DROP TABLE iotmsdb.ruleset_info;

-- ruleset
create table iotmsdb.ruleset_info (
	`ruleset_id` INT(11) NOT NULL AUTO_INCREMENT,
	`ruleset` VARCHAR(512) NULL DEFAULT '0',
	PRIMARY KEY (`ruleset_id`)
) engine=InnoDB character set = utf8;

insert into iotmsdb.ruleset_info(ruleset) value ("if *@0010==Setting#Alarm 											then 78:c4:e:1:7f:f9@0001=Close#Door, *@0011=Malfunction#MessageDelay, *@0010=UnSet#AlarmDelay");
insert into iotmsdb.ruleset_info(ruleset) value ("if *@0010==Setting#Alarm, 78:c4:e:1:7f:f9@0006==Close#DoorSensor	then *@0010=Set#Alarm");
insert into iotmsdb.ruleset_info(ruleset) value ("if *@0010==Set#Alarm 												then !78:c4:e:1:7f:f9@0001=Open#Door, 78:c4:e:1:7f:f9@0008=On#AlarmLamp");
insert into iotmsdb.ruleset_info(ruleset) value ("if *@0010==UnSet#Alarm 											then 78:c4:e:1:7f:f9@0008=Off#AlarmLamp");
insert into iotmsdb.ruleset_info(ruleset) value ("if 78:c4:e:1:7f:f9@0003==Away#Presence 							then *@0011=Confirm#Message, *@0010=Setting#AlarmDelay, 78:c4:e:1:7f:f9@0002=Off#LightDelay");
insert into iotmsdb.ruleset_info(ruleset) value ("if *@0010==Set#Alarm, 78:c4:e:1:7f:f9@0003==AtHome#Presence 		then *@0011=Emergency#Message");
insert into iotmsdb.ruleset_info(ruleset) value ("if *@0010==Set#Alarm, 78:c4:e:1:7f:f9@0006==Open#DoorSensor 		then *@0011=Emergency#Message");
insert into iotmsdb.ruleset_info(ruleset) value ("if 78:c4:e:1:7f:f9@0003==Away#Presence, 78:c4:e:1:7f:f9@0010==Over100#Humidity		then *@0011=Malfunction#Message");
insert into iotmsdb.ruleset_info(ruleset) value ("if 78:c4:e:1:7f:f9@0003==Away#Presence, 78:c4:e:1:7f:f9@0010==Over50#Temperature		then *@0011=Malfunction#Message");
insert into iotmsdb.ruleset_info(ruleset) value ("if 78:c4:e:1:7f:f9@0003==Away#Presence, 78:c4:e:1:7f:f9@0010==Under0#Temperature		then *@0011=Malfunction#Message");
insert into iotmsdb.ruleset_info(ruleset) value ("if 78:c4:e:1:7f:f9@0003==AtHome#Presence, 78:c4:e:1:7f:f9@0010==Over40#Temperature	then 78:c4:e:1:7f:f9@0001=Open#Door");
insert into iotmsdb.ruleset_info(ruleset) value ("if 78:c4:e:2:5c:a3@0001==Mail#MailBox								then *@0011=POST#Message");
