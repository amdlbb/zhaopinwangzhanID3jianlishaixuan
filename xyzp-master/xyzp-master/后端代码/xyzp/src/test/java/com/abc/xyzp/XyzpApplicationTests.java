package com.abc.xyzp;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import java.util.concurrent.TimeUnit;
import com.abc.xyzp.entity.Resume;
import com.abc.xyzp.entity.TreeNode;
import com.abc.xyzp.service.impl.ID3DecisionTreeImpl;
import com.abc.xyzp.service.impl.ModelServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.servlet.ServletContext;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


@RunWith(SpringRunner.class)
@SpringBootTest
@EnableAutoConfiguration(exclude = {
		DataSourceAutoConfiguration.class,
		org.springframework.boot.autoconfigure.websocket.servlet.WebSocketServletAutoConfiguration.class,
		org.springframework.boot.autoconfigure.websocket.reactive.WebSocketReactiveAutoConfiguration.class
})

/*(exclude = {
org.springframework.boot.autoconfigure.websocket.servlet.WebSocketServletAutoConfiguration.class,
org.springframework.boot.autoconfigure.websocket.reactive.WebSocketReactiveAutoConfiguration.class
})*/
class XyzpApplicationTests {

	@MockBean
	private ServletContext servletContext;

	@Autowired
	private ID3DecisionTreeImpl decisionTree;

	@Autowired
	private ModelServiceImpl modelService;

	@Test
	void contextLoads() {

	}

	@Test
	void testTrainAndSaveModel() {
		List<Resume> trainingData = new ArrayList<>();

		Resume r1 = new Resume();
		r1.setAge(22);
		r1.setEducation("本科");
		r1.setExceptionSalary(new BigDecimal("6000"));
		r1.setMajor("计算机类");
		r1.setAdmit(false);
		trainingData.add(r1);

		Resume r2 = new Resume();
		r2.setAge(23);
		r2.setEducation("本科");
		r2.setExceptionSalary(new BigDecimal("7000"));
		r2.setMajor("管理类");
		r2.setAdmit(false);
		trainingData.add(r2);

		Resume r3 = new Resume();
		r3.setAge(24);
		r3.setEducation("本科");
		r3.setExceptionSalary(new BigDecimal("6500"));
		r3.setMajor("商科类");
		r3.setAdmit(false);
		trainingData.add(r3);

		Resume r4 = new Resume();
		r4.setAge(22);
		r4.setEducation("本科");
		r4.setExceptionSalary(new BigDecimal("5500"));
		r4.setMajor("文科类");
		r4.setAdmit(false);
		trainingData.add(r4);

		Resume r5 = new Resume();
		r5.setAge(23);
		r5.setEducation("本科");
		r5.setExceptionSalary(new BigDecimal("7500"));
		r5.setMajor("艺术类");
		r5.setAdmit(false);
		trainingData.add(r5);

		Resume r6 = new Resume();
		r6.setAge(25);
		r6.setEducation("硕士");
		r6.setExceptionSalary(new BigDecimal("12000"));
		r6.setMajor("计算机类");
		r6.setAdmit(true);
		trainingData.add(r6);

		Resume r7 = new Resume();
		r7.setAge(26);
		r7.setEducation("硕士");
		r7.setExceptionSalary(new BigDecimal("15000"));
		r7.setMajor("管理类");
		r7.setAdmit(true);
		trainingData.add(r7);

		Resume r8 = new Resume();
		r8.setAge(24);
		r8.setEducation("硕士");
		r8.setExceptionSalary(new BigDecimal("13000"));
		r8.setMajor("商科类");
		r8.setAdmit(true);
		trainingData.add(r8);

		Resume r9 = new Resume();
		r9.setAge(27);
		r9.setEducation("博士");
		r9.setExceptionSalary(new BigDecimal("20000"));
		r9.setMajor("计算机类");
		r9.setAdmit(true);
		trainingData.add(r9);

		Resume r10 = new Resume();
		r10.setAge(25);
		r10.setEducation("硕士");
		r10.setExceptionSalary(new BigDecimal("14000"));
		r10.setMajor("文科类");
		r10.setAdmit(true);
		trainingData.add(r10);

		System.out.println("=== 开始训练决策树模型 ===");
		System.out.println("训练数据量: " + trainingData.size());

		TreeNode model = decisionTree.trainAndSave(trainingData);

		System.out.println("=== 模型训练完成 ===");
		System.out.println("根节点特征: " + model.getFeature());
		System.out.println("子节点数量: " + model.getChildren().size());

		System.out.println("\n=== 测试预测功能 ===");

		Resume testResume1 = new Resume();
		testResume1.setAge(23);
		testResume1.setEducation("硕士");
		testResume1.setExceptionSalary(new BigDecimal("13000"));
		testResume1.setMajor("计算机应用技术");
		Boolean prediction1 = decisionTree.predict(model, testResume1);
		System.out.println("测试样本1 (硕士, 13000): " + (prediction1 ? "通过" : "不通过"));

		Resume testResume2 = new Resume();
		testResume2.setAge(22);
		testResume2.setEducation("本科");
		testResume2.setExceptionSalary(new BigDecimal("6000"));
		testResume2.setMajor("工商管理");
		Boolean prediction2 = decisionTree.predict(model, testResume2);
		System.out.println("测试样本2 (本科, 6000): " + (prediction2 ? "通过" : "不通过"));

		System.out.println("\n=== 测试模型加载功能 ===");
		TreeNode loadedModel = modelService.loadModel();
		if (loadedModel != null) {
			System.out.println("模型加载成功!");
			System.out.println("加载的模型根节点特征: " + loadedModel.getFeature());

			Boolean prediction3 = decisionTree.predict(loadedModel, testResume1);
			System.out.println("使用加载的模型预测: " + (prediction3 ? "通过" : "不通过"));
		} else {
			System.out.println("模型加载失败!");
		}
	}


}
