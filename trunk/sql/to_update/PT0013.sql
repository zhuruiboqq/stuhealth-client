-- 体检记录保存卡死的问题已解决，需要增加如下索引
CREATE INDEX idx_result_rpt_gradebh ON result(TJND,Term,XB,SCHOOLBH,gradebh);
CREATE INDEX idx_result_rpt_nl ON result(TJND,Term,XB,SCHOOLBH,nl);
CREATE INDEX idx_student_rpt_gradeNo ON student(schoolNo, sex, status, gradeNo);
CREATE INDEX idx_student_rpt_bornDate ON student(schoolNo, sex, status, bornDate);