package com.abc.xyzp.service;

import com.abc.xyzp.entity.TreeNode;

public interface ModelService {

    void saveModel(TreeNode model);
    TreeNode loadModel();
}
