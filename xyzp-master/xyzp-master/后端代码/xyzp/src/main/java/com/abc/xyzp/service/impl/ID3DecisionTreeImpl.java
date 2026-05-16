package com.abc.xyzp.service.impl;


import com.abc.xyzp.entity.Resume;
import com.abc.xyzp.entity.TreeNode;
import com.abc.xyzp.service.ID3DecisionTree;
import com.abc.xyzp.service.ModelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ID3DecisionTreeImpl implements ID3DecisionTree {

    @Autowired
    private ModelService modelService;

    // 训练并保存模型
    public TreeNode trainAndSave(List<Resume> data) {
        TreeNode model = train(data);
        modelService.saveModel(model);
        return model;
    }

    // 获取模型（优先从文件加载，如果没有则训练）
    public TreeNode getModel() {
        TreeNode model = modelService.loadModel();
        if (model == null) {

            System.out.println("警告: 未找到已训练的模型文件");
            // Todo 从数据库加载训练数据

        }
        return model;
    }

    // 主训练方法
    public TreeNode train(List<Resume> data) {
        if (shouldStop(data)) {
            TreeNode leaf = new TreeNode();
            leaf.setResult(getMajorityClass(data));
            return leaf;
        }

        String bestFeature = chooseBestFeature(data);
        TreeNode node = new TreeNode();
        node.setFeature(bestFeature);

        // 按特征值分组
        Map<Object, List<Resume>> groups = data.stream()
                .collect(Collectors.groupingBy(r -> getFeatureValue(r, bestFeature)));

        for (Map.Entry<Object, List<Resume>> entry : groups.entrySet()) {
            TreeNode child = train(entry.getValue());
            child.setValue(entry.getKey().toString());
            node.getChildren().put(entry.getKey().toString(), child);
        }

        return node;
    }

    // 选择最佳特征
    public String chooseBestFeature(List<Resume> data) {
        String[] features = {"age", "education", "exceptionSalary", "major"};
        String best = features[0];
        double bestGain = -1;

        for (String feature : features) {
            double gain = calculateInfoGain(data, feature);
            if (gain > bestGain) {
                bestGain = gain;
                best = feature;
            }
        }
        return best;
    }

    // 计算信息增益
    private double calculateInfoGain(List<Resume> data, String feature) {
        double totalEntropy = calculateEntropy(data);

        // 按特征值分组
        Map<Object, List<Resume>> groups = data.stream()
                .collect(Collectors.groupingBy(r -> getFeatureValue(r, feature)));

        double remainder = 0;
        for (List<Resume> group : groups.values()) {
            double weight = (double) group.size() / data.size();
            remainder += weight * calculateEntropy(group);
        }

        return totalEntropy - remainder;
    }

    // 计算信息熵
    private double calculateEntropy(List<Resume> data) {
        if (data.isEmpty()) return 0;

        long positive = data.stream().filter(r -> r.getAdmit() != null && r.getAdmit()).count();
        long negative = data.size() - positive;

        double p1 = (double) positive / data.size();
        double p2 = (double) negative / data.size();

        double entropy = 0;
        if (positive > 0) entropy -= p1 * Math.log(p1) / Math.log(2);
        if (negative > 0) entropy -= p2 * Math.log(p2) / Math.log(2);

        return entropy;
    }

    // 获取特征值
    private Object getFeatureValue(Resume resume, String feature) {
        switch (feature) {
            case "age": return resume.getAge() == null ? "未知" : resume.getAge();
            case "education": return resume.getEducation() == null ? "未知" : resume.getEducation();
            case "exceptionSalary":
                BigDecimal salary = resume.getExceptionSalary();
                if (salary == null) return "未知";
                if (salary.compareTo(new BigDecimal("8000")) < 0) return "低";
                if (salary.compareTo(new BigDecimal("15000")) < 0) return "中";
                return "高";
            case "major": return resume.getMajor() == null ? "未知" : resume.getMajor();
            default: return "未知";
        }
    }

    // 判断是否停止分裂
    private boolean shouldStop(List<Resume> data) {
        if (data.isEmpty()) return true;

        // 如果所有标签相同
        boolean first = data.get(0).getAdmit();
        return data.stream().allMatch(r -> r.getAdmit() == first);
    }

    // 获取多数类
    private Boolean getMajorityClass(List<Resume> data) {
        long positive = data.stream().filter(r -> r.getAdmit() != null && r.getAdmit()).count();
        return positive > data.size() / 2;
    }

    // 预测方法
    public Boolean predict(TreeNode root, Resume resume) {
        if (root.getResult() != null) {
            return root.getResult();
        }

        Object value = getFeatureValue(resume, root.getFeature());
        TreeNode child = root.getChildren().get(value.toString());

        if (child == null) {
            // 如果没找到对应分支，返回多数类
            return root.getResult() != null ? root.getResult() : false;
        }

        return predict(child, resume);
    }
}
