-- hw_school中的permit不能为空，否则在ORMapping时会报错
UPDATE HW_SCHOOL SET PERMIT=0 WHERE PERMIT IS NULL;
ALTER TABLE HW_SCHOOL CHANGE PERMIT PERMIT tinyint(1) DEFAULT 0;