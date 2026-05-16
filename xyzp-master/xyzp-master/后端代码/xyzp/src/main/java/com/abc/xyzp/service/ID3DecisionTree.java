package com.abc.xyzp.service;

import com.abc.xyzp.entity.Resume;
import com.abc.xyzp.entity.TreeNode;

import java.util.List;

public interface ID3DecisionTree {
     TreeNode train(List<Resume> data);
     String chooseBestFeature(List<Resume> data);
     Boolean predict(TreeNode root, Resume resume);
}
