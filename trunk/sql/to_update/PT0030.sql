-- 按用户测试反馈要求，重新排序体检项目
UPDATE hw_checkitem SET id=id*1000;
UPDATE hw_checkitem SET id='10', itemid=10, sort=1 WHERE itemname = '身高';
UPDATE hw_checkitem SET id='20', itemid=20, sort=2 WHERE itemname = '体重';
UPDATE hw_checkitem SET id='30', itemid=30, sort=3 WHERE itemname = '胸围';
UPDATE hw_checkitem SET id='40', itemid=40, sort=4 WHERE itemname = '肺活量';
UPDATE hw_checkitem SET id='50', itemid=50, sort=5 WHERE itemname = '收缩压';
UPDATE hw_checkitem SET id='60', itemid=60, sort=6 WHERE itemname = '舒张压';
UPDATE hw_checkitem SET id='70', itemid=70, sort=7 WHERE itemname = '脉搏';
UPDATE hw_checkitem SET id='80', itemid=80, sort=8 WHERE itemname = '左眼视力';
UPDATE hw_checkitem SET id='90', itemid=90, sort=9 WHERE itemname = '右眼视力';
UPDATE hw_checkitem SET id='100', itemid=100, sort=10 WHERE itemname = '左眼屈光不正';
UPDATE hw_checkitem SET id='110', itemid=110, sort=11 WHERE itemname = '右眼屈光不正';
UPDATE hw_checkitem SET id='120', itemid=120, sort=12 WHERE itemname = '左近视力';
UPDATE hw_checkitem SET id='130', itemid=130, sort=13 WHERE itemname = '右近视力';
UPDATE hw_checkitem SET id='140', itemid=140, sort=14 WHERE itemname = '弱视';
UPDATE hw_checkitem SET id='150', itemid=150, sort=15 WHERE itemname = '结膜炎';
UPDATE hw_checkitem SET id='160', itemid=160, sort=16 WHERE itemname = '左眼沙眼';
UPDATE hw_checkitem SET id='170', itemid=170, sort=17 WHERE itemname = '右眼沙眼';
UPDATE hw_checkitem SET id='180', itemid=180, sort=18 WHERE itemname = '辨色';
UPDATE hw_checkitem SET id='190', itemid=190, sort=19 WHERE itemname = '听力左';
UPDATE hw_checkitem SET id='200', itemid=200, sort=20 WHERE itemname = '听力右';
UPDATE hw_checkitem SET id='210', itemid=210, sort=21 WHERE itemname = '耳病';
UPDATE hw_checkitem SET id='220', itemid=220, sort=22 WHERE itemname = '眼病';
UPDATE hw_checkitem SET id='230', itemid=230, sort=23 WHERE itemname = '鼻病';
UPDATE hw_checkitem SET id='240', itemid=240, sort=24 WHERE itemname = '扁桃体';
UPDATE hw_checkitem SET id='250', itemid=250, sort=25 WHERE itemname = '龋失补牙数';
UPDATE hw_checkitem SET id='260', itemid=260, sort=26 WHERE itemname = '乳龋患';
UPDATE hw_checkitem SET id='270', itemid=270, sort=27 WHERE itemname = '乳龋失';
UPDATE hw_checkitem SET id='280', itemid=280, sort=28 WHERE itemname = '乳龋补';
UPDATE hw_checkitem SET id='290', itemid=290, sort=29 WHERE itemname = '恒龋患';
UPDATE hw_checkitem SET id='300', itemid=300, sort=30 WHERE itemname = '恒龋失';
UPDATE hw_checkitem SET id='310', itemid=310, sort=31 WHERE itemname = '恒龋补';
UPDATE hw_checkitem SET id='320', itemid=320, sort=32 WHERE itemname = '牙周疾病';
UPDATE hw_checkitem SET id='330', itemid=330, sort=33 WHERE itemname = '窝沟封闭';
UPDATE hw_checkitem SET id='340', itemid=340, sort=34 WHERE itemname = '牙龈';
UPDATE hw_checkitem SET id='350', itemid=350, sort=35 WHERE itemname = '心';
UPDATE hw_checkitem SET id='360', itemid=360, sort=36 WHERE itemname = '肺';
UPDATE hw_checkitem SET id='370', itemid=370, sort=37 WHERE itemname = '肝';
UPDATE hw_checkitem SET id='380', itemid=380, sort=38 WHERE itemname = '脾';
UPDATE hw_checkitem SET id='390', itemid=390, sort=39 WHERE itemname = '头部';
UPDATE hw_checkitem SET id='400', itemid=400, sort=40 WHERE itemname = '颈部';
UPDATE hw_checkitem SET id='410', itemid=410, sort=41 WHERE itemname = '胸廓';
UPDATE hw_checkitem SET id='420', itemid=420, sort=42 WHERE itemname = '脊柱侧弯';
UPDATE hw_checkitem SET id='430', itemid=430, sort=43 WHERE itemname = '四肢';
UPDATE hw_checkitem SET id='440', itemid=440, sort=44 WHERE itemname = '平足';
UPDATE hw_checkitem SET id='450', itemid=450, sort=45 WHERE itemname = '皮肤';
UPDATE hw_checkitem SET id='460', itemid=460, sort=46 WHERE itemname = '甲状腺';
UPDATE hw_checkitem SET id='470', itemid=470, sort=47 WHERE itemname = '淋巴结';
UPDATE hw_checkitem SET id='480', itemid=480, sort=48 WHERE itemname = '嗅觉';
UPDATE hw_checkitem SET id='490', itemid=490, sort=49 WHERE itemname = '肠道蠕虫';
UPDATE hw_checkitem SET id='500', itemid=500, sort=50 WHERE itemname = '皮肤病';
UPDATE hw_checkitem SET id='510', itemid=510, sort=51 WHERE itemname = '神经衰弱';
UPDATE hw_checkitem SET id='520', itemid=520, sort=52 WHERE itemname = '男性外生殖器';
UPDATE hw_checkitem SET id='530', itemid=530, sort=53 WHERE itemname = '血红蛋白';
UPDATE hw_checkitem SET id='540', itemid=540, sort=54 WHERE itemname = '结核菌素';
UPDATE hw_checkitem SET id='550', itemid=550, sort=55 WHERE itemname = '谷丙转氨酶';
UPDATE hw_checkitem SET id='560', itemid=560, sort=56 WHERE itemname = '总胆红素';
UPDATE hw_checkitem SET id='570', itemid=570, sort=57 WHERE itemname = '直胆红素';
UPDATE hw_checkitem SET id='580', itemid=580, sort=58 WHERE itemname = '肝功';
UPDATE hw_checkitem SET id='590', itemid=590, sort=59 WHERE itemname = '乙肝表面抗原';
UPDATE hw_checkitem SET id='600', itemid=600, sort=60 WHERE itemname = '乙肝表面抗体';
UPDATE hw_checkitem SET id='610', itemid=610, sort=61 WHERE itemname = '乙肝核心抗体';
UPDATE hw_checkitem SET id='620', itemid=620, sort=62 WHERE itemname = '乙肝e抗原';
UPDATE hw_checkitem SET id='630', itemid=630, sort=63 WHERE itemname = '乙肝e抗体';
UPDATE hw_checkitem SET id='640', itemid=640, sort=64 WHERE itemname = '血型';
UPDATE hw_checkitem SET id='650', itemid=650, sort=65 WHERE itemname = '红细胞';
UPDATE hw_checkitem SET id='660', itemid=660, sort=66 WHERE itemname = '胸透';
UPDATE hw_checkitem SET id='670', itemid=670, sort=67 WHERE itemname = '腰围';
UPDATE hw_checkitem SET id='680', itemid=680, sort=68 WHERE itemname = '臀围';
UPDATE hw_checkitem SET id='690', itemid=690, sort=69 WHERE itemname = '白细胞';
UPDATE hw_checkitem SET id='700', itemid=700, sort=70 WHERE itemname = '血小板';
UPDATE hw_checkitem SET id='710', itemid=710, sort=71 WHERE itemname = '骨盆宽';
UPDATE hw_checkitem SET id='720', itemid=720, sort=72 WHERE itemname = '握力';
UPDATE hw_checkitem SET id='730', itemid=730, sort=73 WHERE itemname = '其它眼病';
UPDATE hw_checkitem SET id='740', itemid=740, sort=74 WHERE itemname = '背肌力';
UPDATE hw_checkitem SET id='750', itemid=750, sort=75 WHERE itemname = '肱三头肌皮褶厚度';
UPDATE hw_checkitem SET id='760', itemid=760, sort=76 WHERE itemname = '肩胛下皮褶厚度';
UPDATE hw_checkitem SET id='770', itemid=770, sort=77 WHERE itemname = '自定义01';
UPDATE hw_checkitem SET id='780', itemid=780, sort=78 WHERE itemname = '自定义02';
UPDATE hw_checkitem SET id='790', itemid=790, sort=79 WHERE itemname = '自定义03';
UPDATE hw_checkitem SET id='800', itemid=800, sort=80 WHERE itemname = '自定义04';
UPDATE hw_checkitem SET id='810', itemid=810, sort=81 WHERE itemname = '自定义05';
UPDATE hw_checkitem SET id='820', itemid=820, sort=82 WHERE itemname = '自定义06';
UPDATE hw_checkitem SET id='830', itemid=830, sort=83 WHERE itemname = '自定义07';
UPDATE hw_checkitem SET id='840', itemid=840, sort=84 WHERE itemname = '自定义08';
UPDATE hw_checkitem SET id='850', itemid=850, sort=85 WHERE itemname = '自定义09';
UPDATE hw_checkitem SET id='860', itemid=860, sort=86 WHERE itemname = '自定义10';
-- 增加血小板和白细胞项目
INSERT INTO hw_checkitem (id, BanBen, EnCode, ItemID, Sort, Status, Type, Unit, ItemName, IsSelected, IsCustom, alias, Fieldname) VALUES('690', 1, '69', 690, 690, 'T', 0, '', '白细胞', 0, 0, null, 'BXP'),	  ('700', 1, '70', 700, 700, 'T', 0, '', '血小板', 0, 0, null, 'XXB');	  