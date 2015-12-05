-- 增加BMI标准
CREATE TABLE standard_bmi (id varchar(255) NOT NULL, age int(11) DEFAULT NULL, p1 decimal(19,1) DEFAULT NULL, p2 decimal(19,1) DEFAULT NULL, sex varchar(255) DEFAULT NULL, version varchar(255) DEFAULT NULL,  PRIMARY KEY (`id`));
 INSERT INTO standard_bmi ( id, age, sex, p1, p2, version ) VALUES (  '1','7','男',17.4,19.2,'1995' );
 INSERT INTO standard_bmi ( id, age, sex, p1, p2, version ) VALUES (  '2','8','男',18.1,20.3,'1995' );
 INSERT INTO standard_bmi ( id, age, sex, p1, p2, version ) VALUES (  '3','9','男',18.9,21.4,'1995' );
 INSERT INTO standard_bmi ( id, age, sex, p1, p2, version ) VALUES (  '4','10','男',19.6,22.5,'1995' );
 INSERT INTO standard_bmi ( id, age, sex, p1, p2, version ) VALUES (  '5','11','男',20.3,23.6,'1995' );
 INSERT INTO standard_bmi ( id, age, sex, p1, p2, version ) VALUES (  '6','12','男',21,24.7,'1995' );
 INSERT INTO standard_bmi ( id, age, sex, p1, p2, version ) VALUES (  '7','13','男',21.9,25.7,'1995' );
 INSERT INTO standard_bmi ( id, age, sex, p1, p2, version ) VALUES (  '8','14','男',22.6,26.4,'1995' );
 INSERT INTO standard_bmi ( id, age, sex, p1, p2, version ) VALUES (  '9','15','男',23.1,26.9,'1995' );
 INSERT INTO standard_bmi ( id, age, sex, p1, p2, version ) VALUES (  '10','16','男',23.5,27.4,'1995' );
 INSERT INTO standard_bmi ( id, age, sex, p1, p2, version ) VALUES (  '11','17','男',23.8,27.8,'1995' );
 INSERT INTO standard_bmi ( id, age, sex, p1, p2, version ) VALUES (  '12','18','男',24,28,'1995' );
 INSERT INTO standard_bmi ( id, age, sex, p1, p2, version ) VALUES (  '13','7','女',17.2,18.9,'1995' );
 INSERT INTO standard_bmi ( id, age, sex, p1, p2, version ) VALUES (  '14','8','女',18.1,19.9,'1995' );
 INSERT INTO standard_bmi ( id, age, sex, p1, p2, version ) VALUES (  '15','9','女',19,21,'1995' );
 INSERT INTO standard_bmi ( id, age, sex, p1, p2, version ) VALUES (  '16','10','女',20,22.1,'1995' );
 INSERT INTO standard_bmi ( id, age, sex, p1, p2, version ) VALUES (  '17','11','女',21.1,23.3,'1995' );
 INSERT INTO standard_bmi ( id, age, sex, p1, p2, version ) VALUES (  '18','12','女',21.9,24.5,'1995' );
 INSERT INTO standard_bmi ( id, age, sex, p1, p2, version ) VALUES (  '19','13','女',22.6,25.6,'1995' );
 INSERT INTO standard_bmi ( id, age, sex, p1, p2, version ) VALUES (  '20','14','女',23,26.3,'1995' );
 INSERT INTO standard_bmi ( id, age, sex, p1, p2, version ) VALUES (  '21','15','女',23.4,26.9,'1995' );
 INSERT INTO standard_bmi ( id, age, sex, p1, p2, version ) VALUES (  '22','16','女',23.7,27.4,'1995' );
 INSERT INTO standard_bmi ( id, age, sex, p1, p2, version ) VALUES (  '23','17','女',23.8,27.7,'1995' );
 INSERT INTO standard_bmi ( id, age, sex, p1, p2, version ) VALUES (  '24','18','女',24,28,'1995' );