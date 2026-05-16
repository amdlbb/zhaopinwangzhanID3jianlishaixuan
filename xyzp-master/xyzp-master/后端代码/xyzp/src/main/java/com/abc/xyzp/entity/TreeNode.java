package com.abc.xyzp.entity;


import lombok.Data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@Data
public class TreeNode implements Serializable {
    private static final long serialVersionUID = 1L;
    String feature;  // 分裂特征
    String value;    // 分裂值
    Boolean result;  // 叶子节点的结果
    Map<String, TreeNode> children = new HashMap<>();
}
