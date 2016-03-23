
-- 创建位运算使用的表
DROP TABLE IF EXISTS study_bit_agent;
CREATE TABLE study_bit_agent (
    id int(11) AUTO_INCREMENT COMMENT '自增主键',
    tag bit(8) NOT NULL DEFAULT b'00000000' COMMENT '第一位：是否大区服务商；第二位：是否省服务商；第三位：是否市服务商；第四位：是否县服务商；第五位：是否姓氏服务商；第六位：是否镇级服务商；第七位：是否村级服务商；第八位预留',
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT '位运算使用的表';