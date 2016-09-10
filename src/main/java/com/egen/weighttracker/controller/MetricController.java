package com.egen.weighttracker.controller;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bson.types.ObjectId;
import org.easyrules.api.RulesEngine;
import org.easyrules.core.RulesEngineBuilder;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.query.Query;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.egen.weighttracker.database.MongoMorphiaDBHelper;
import com.egen.weighttracker.entity.Metrics;
import com.egen.weighttracker.rules.WeightRules;
import com.egen.weighttracker.utils.Utility;

@RestController
@RequestMapping("metrics")
public class MetricController {

	public static Double baseWeight;
	public static Double underWeight;
	public static Double overWeight;

	@RequestMapping(value = "/create", method = RequestMethod.POST, headers="Accept=application/json")
	public void create(@RequestBody Map<String, String> weightParams){
		Double weight = Double.valueOf(weightParams.get("value"));

		if(baseWeight == null){			
			baseWeight = weight;
			underWeight = baseWeight - Utility.weightPercentage(baseWeight, 10);
			overWeight =  baseWeight + Utility.weightPercentage(baseWeight, 10);
		}

		Timestamp timestamp =new Timestamp(Long.parseLong(weightParams.get("timeStamp")));
		System.out.println("BaseWeight: " + baseWeight + " CurrentWeight:  " + weight + ", CurrentTime:  "+ timestamp);

		WeightRules wr= new WeightRules();
		wr.setInput(weight);
		RulesEngine rulesEngine = RulesEngineBuilder.aNewRulesEngine().withSilentMode(true).build();		
		rulesEngine.registerRule(wr);		
		rulesEngine.fireRules();


		Datastore datastore = MongoMorphiaDBHelper.getInstance().getDatastore();		
		Metrics metr = new Metrics();
		metr.setWeight(weight);
		metr.setTimestamp(timestamp);
		metr.setId(new ObjectId());
		datastore.save(metr);     

	}


	@RequestMapping(value = "/read", method = RequestMethod.GET, produces = "application/json")
	public List<Metrics> getAllMetrics() {
		Datastore datastore = MongoMorphiaDBHelper.getInstance().getDatastore();		
		Query<Metrics> query = datastore.createQuery(Metrics.class);
		List<Metrics> metrics = query.asList();
		return metrics;
	}

	@RequestMapping(value = "/readByTimeRange", method = RequestMethod.GET, produces = "application/json")
	public List<Metrics> getDateRangeMetrics(@RequestParam String startTime, @RequestParam String endTime) {

		Timestamp startTimeStamp =new Timestamp(Long.parseLong(startTime));
		Timestamp endTimeStamp =new Timestamp(Long.parseLong(endTime));
		
		Datastore datastore = MongoMorphiaDBHelper.getInstance().getDatastore();		
		Query<Metrics> query = datastore.createQuery(Metrics.class);		
		query.and(
				  query.criteria("timestamp").greaterThan(startTimeStamp),
				  query.criteria("timestamp").lessThan(endTimeStamp));
		
		List<Metrics> metrics = query.asList();
				
		return metrics;

	}
}
