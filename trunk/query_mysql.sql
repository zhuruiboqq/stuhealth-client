delete from report_zb_grade;
delete from report_zb_age;
delete from report_tb_grade;
delete from report_tb_age;
delete from result where tjnd=2013;

select xm, xz,mb, GRADEBH,SCHOOLBH,CLASSBH,classLongNo,ssy,ssypj from result
where tjnd=2012
;

CREATE INDEX idx_student_classlongnumber ON student(classlongnumber); 
CREATE INDEX idx_result_studentcode ON result(studentcode);
CREATE INDEX idx_result_xm ON result(xm);
CREATE INDEX idx_result_xb ON result(xb);
CREATE INDEX idx_result_gradebh ON result(gradebh);

CREATE INDEX idx_result_rpt_gradebh ON result(TJND,Term,XB,SCHOOLBH,gradebh);
CREATE INDEX idx_result_rpt_nl ON result(TJND,Term,XB,SCHOOLBH,nl);


CREATE INDEX idx_student_schoolNo ON student(schoolNo);
CREATE INDEX idx_student_gradeNo ON student(gradeNo);
CREATE INDEX idx_student_classNo ON student(classNo);
CREATE INDEX idx_student_classLongNumber ON student(classLongNumber);
drop index idx_student_schoolNo on student;
drop index idx_student_gradeNo on student;
drop index idx_student_classNo on student;
drop index idx_student_classLongNumber on student;

CREATE INDEX idx_student_rpt_gradeNo ON student(schoolNo, sex, status, gradeNo);
CREATE INDEX idx_student_rpt_bornDate ON student(schoolNo, sex, status, bornDate);


select * from result where schoolbh = '4452220130778260448' and tjnd = 2012 and classbh like '11%'
--1107

select * from schooltree where parentCode = '4452220130778260448'
order by code

select count(*) from Student 
where Student.schoolNo='4452220130556760446'
     and (Student.status is null or Student.status ='T') and Student.sex='男'
     and Student.gradeNo = '21'

select * from Report_TB_grade where schoolBh = '4452220130556760446' and TJND=2013 and rid = '01'

select * from Report_TB_Grade where xs = -1
 update Report_TB_Grade rpt set XS = 
 (select count(*) from Student where Student.schoolNo=rpt.schoolBH 
     and (Student.status is null or Student.status ='T') and Student.sex=rpt.XB and Student.gradeNo = rpt.Grade)  
 where XS = -1 and term = 1 and tjnd = 2013

select (select Grade.Name from Grade where rpt.grade=Grade.GradeCode) grade
 , grade orderFiled, XB, sum(case when RID='01' then XS else 0 end) XS
 from Report_TB_grade rpt 
 where 1=1  and SCHOOLBH = '4452220130556760446' and (TJND=2013 and Term=1)
 group by grade, XB
 
SELECT * FROM schooltree WHERE longNumber like '%4402820103549057517%'
 
select *
FROM HW_OrgUser a
INNER JOIN HW_School b ON b.OrgCode = a.OrgCode
where a.XX_UserName = 'M-SZC001'  and a.XX_Pass = '0EC5D980B429C2E2C92EEE0E00D9FFD8' 
and b.SecCode = '2B4F871D7249274FAEA95751FA748273'