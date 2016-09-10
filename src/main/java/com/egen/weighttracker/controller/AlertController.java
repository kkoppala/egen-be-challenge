package com.egen.weighttracker.controller;

import java.sql.Timestamp;
import java.util.List;

import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.query.Query;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.egen.weighttracker.database.MongoMorphiaDBHelper;
import com.egen.weighttracker.entity.Alerts;

@RestController
@RequestMapping("alerts")
public class AlertController {
	
	@RequestMapping(value = "/read", method = RequestMethod.GET, produces = "application/json")
	public List<Alerts> getAllAlerts() {
		Datastore datastore = MongoMorphiaDBHelper.getInstance().getDatastore();		
		Query<Alerts> query = datastore.createQuery(Alerts.class);
		List<Alerts> alerts = query.asList();
		return alerts;
	}
	
	@RequestMapping(value = "/readByTimeRange", method = RequestMethod.GET, produces = "application/json")
	public List<Alerts> getDateRangeAlerts(@RequestParam String startTime, @RequestParam String endTime) {

		Timestamp startTimeStamp =new Timestamp(Long.parseLong(startTime));
		Timestamp endTimeStamp =new Timestamp(Long.parseLong(endTime));
		
		Datastore datastore = MongoMorphiaDBHelper.getInstance().getDatastore();		
		Query<Alerts> query = datastore.createQuery(Alerts.class);
		query.and(
				  query.criteria("timestamp").greaterThan(startTimeStamp),
				  query.criteria("timestamp").lessThan(endTimeStamp));		
		
		List<Alerts> alerts = query.asList();
		return alerts;
	}

}
