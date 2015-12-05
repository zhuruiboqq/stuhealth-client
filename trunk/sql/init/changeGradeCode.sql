/* 
   年级编码由原来的101、102、103...改为11、12、13
**/
-- 更新学校树中班级编码
update schooltree set 
	code=concat(subString(code,1,1),subString(code,3)),
	longnumber=concat(substring(longnumber, 1, length(longnumber)-4), substring(longnumber, length(longnumber)-2))
where type =40;

-- 更新学生表中班级编码和班级长编码
update student set 
	classNo=concat(substring(classNo, 1, 1), substring(classNo, 3)),
	classlongnumber=concat(substring(classlongnumber, 1, length(classlongnumber)-4), substring(classlongnumber, length(classlongnumber)-2))
;

-- 更新体检结果表中班级编码和班级长编码
update result set 
	gradebh=concat(subString(gradebh,1,1),subString(gradebh,3)),
	CLASSBH=concat(substring(CLASSBH, 1, 1), substring(CLASSBH, 3)),
	CLASSLONGNO=concat(substring(CLASSLONGNO, 1, length(CLASSLONGNO)-4), substring(CLASSLONGNO, length(CLASSLONGNO)-2))
;
-- 疫苗定性结果表中增加疫苗字段名列
update vaccinitem a set a.fieldmc= (select b.fieldname from vaccin b where b.vaccinid=a.vaccinid);

commit;