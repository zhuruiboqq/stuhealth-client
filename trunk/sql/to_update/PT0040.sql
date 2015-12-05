update ItemResult set itemResult='未见异常' where itemResult='正常';
-- 同步体检项目表与对应枚举值表的ID
UPDATE itemresult a SET a.itemid=( SELECT b.itemid FROM hw_checkitem b WHERE b.fieldname=a.fieldmc );