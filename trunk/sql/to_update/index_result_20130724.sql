-- 2013-07-24
CREATE INDEX idx_itemresult_field_code ON itemresult(FieldMc,ItemCode);
CREATE INDEX idx_result_xm ON result(xm);
CREATE INDEX idx_result_xb ON result(xb);
CREATE INDEX idx_result_gradebh ON result(gradebh);