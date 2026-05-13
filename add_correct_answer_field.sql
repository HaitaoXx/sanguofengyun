-- 添加 correct_answer 字段到 t_question 表
ALTER TABLE t_question ADD COLUMN correct_answer VARCHAR(500) COMMENT '正确答案';
