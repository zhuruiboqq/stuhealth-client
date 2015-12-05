-- 
update hw_checkitem set itemname='蠕虫卵阴性阳性' where itemname='蛔虫卵阴性阳性';
update vaccin set vaccinname='麻腮风疫苗' where vaccinname='麻疹疫苗';
-- 新增流脑疫苗和甲肝疫苗
insert into vaccin (id, vaccinid, vaccinname, fieldname, iscustom, isselected, sort, choosesort, choosestate, state) values(12, 12, '流脑疫苗', 'YMG', 0, 1, 6, 6, 1, 1);
insert into vaccin (id, vaccinid, vaccinname, fieldname, iscustom, isselected, sort, choosesort, choosestate, state) values(13, 13, '甲肝疫苗', 'YMH', 0, 1, 6, 6, 1, 1);
update vaccinitem set fieldmc='YMG' where vaccinid=12;
insert into vaccinitem (id, vaccitem, vaccitemid, vaccinid, fieldmc) values(30, '已全程接种', 0, 13, 'YMH');
insert into vaccinitem (id, vaccitem, vaccitemid, vaccinid, fieldmc) values(31, '未接种', 1, 13, 'YMH');
insert into vaccinitem (id, vaccitem, vaccitemid, vaccinid, fieldmc) values(32, '有漏种', 2, 13, 'YMH');
alter table result add YMG varchar(255);
alter table result add YMGJY varchar(255);
alter table result add YMH varchar(255);
alter table result add YMHJY varchar(255);
-- 身高体重保留1位小数
alter table result modify column SG decimal(19,1);
alter table result modify column TZ decimal(19,1);