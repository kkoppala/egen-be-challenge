package com.egen.weighttracker;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.egen.weighttracker.controller.AlertController;
import com.egen.weighttracker.controller.MetricController;

@RunWith(SpringRunner.class)
@SpringBootTest
public class EgenBeChallengeApplicationTests {
@Autowired
private MetricController metricController;
@Autowired
private AlertController alertController;
	
	@Test
	public void completeTest() {
		Map<String,String> input = new HashMap<String,String>();
		input.put("value", String.valueOf(150));
		input.put("timeStamp", String.valueOf(System.currentTimeMillis()));
		
		metricController.create(input);
		input.clear();
		input.put("value", String.valueOf(120));
		input.put("timeStamp", String.valueOf(System.currentTimeMillis()));
		metricController.create(input);
		input.clear();
		input.put("value", String.valueOf(175));
		input.put("timeStamp", String.valueOf(System.currentTimeMillis()));
		metricController.create(input);

		assertEquals("Metric ResultSize should be 3", 3, metricController.getAllMetrics().size());

		assertEquals("Alert ResultSize should be 2", 2, alertController.getAllAlerts().size());	
		
	}

}
