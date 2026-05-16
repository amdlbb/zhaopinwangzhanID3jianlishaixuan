package com.abc.xyzp.entity;

import lombok.Data;

import java.math.BigDecimal;

//训练集方法
@Data
public class Resume {

    private Long id;
    private Integer age;           // 年龄
    private String education;      // 学历
    private BigDecimal exceptionSalary; // 期望薪资
    private String major;          // 专业
    private Boolean admit;         // 是否录取（标签）

}
