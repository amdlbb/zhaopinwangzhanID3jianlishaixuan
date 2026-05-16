package com.abc.xyzp.service.impl;

import com.abc.xyzp.entity.TreeNode;
import com.abc.xyzp.service.ModelService;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;

@Service
public class ModelServiceImpl implements ModelService {
    @Value("${model.file.path}")
    private String modelFilePath;

    // 保存模型到文件
    public void saveModel(TreeNode model) {
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(modelFilePath))) {
            oos.writeObject(model);
            System.out.println("模型已保存到: " + modelFilePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 从文件加载模型
    public TreeNode loadModel() {
        File file = new File(modelFilePath);
        if (!file.exists()) {
            return null;
        }

        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(file))) {
            return (TreeNode) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
}
