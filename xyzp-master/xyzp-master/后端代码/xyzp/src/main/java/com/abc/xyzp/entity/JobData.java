package com.abc.xyzp.entity;

import lombok.Data;

import java.util.List;

@Data
public class JobData {

    private String name;

    private Long id;

    private List<JobData> list;


}
